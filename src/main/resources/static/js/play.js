// GAME SETTINGS

// The colors the viewer is controlling.  0 is first for convenient indexing.
var youControl = [0,true,false,true,false]; 

var timed = true; // Timed or Untimed
var maxTime = 15; // Time per move in seconds
var gameStarted = true; // whether the game has started
var gameOver = false; // whether the game is over

var startTime = null;
var update = null;

// 0 is here for convenient player indexing
var remainingPieces = [0, [], [], [], []];
var grid = []; //current game board
var players = []; // player data

var curPlayer = 1; // players are 1,2,3,4
var nextPlayer = 1;

var playerCount = 4; // max num of players
var isLocal = false; // true if local, false if networked


//BOARD PARAMETERS

var boardRatio = 1.5;               // canvas width/height
var SIZE = 25;                      // size of squares
var supplyLeftEdge = 21 * SIZE; 
var size2 = Math.floor(SIZE / 2);     // size for supply pieces
var dotSize = SIZE / 3.3;

var colors = ["#EDEEEF"];
$(".playerInfo").each(function() {
  colors.push($(this).css("background-color"));
});

var board = document.getElementById("board");
var ctx = board.getContext("2d");
var url = window.location.href; 
var gameID = null;

var curPiece = 0; // pieces range from 0 to 20
var rotate = [1, 0, 0, 1, 1]; // 2x2 matrix followed by parity, see orient function

var mode = "relaxing"; 
// modes include "relaxing" -> "dragging" -> "positioning" 
// also mode can be "notYourTurn"

var hovering = 0; // which player is being hovered

var curPieceX = 0; // grid locations
var curPieceY = 0;

var curMouseX = 0; // mouse locations within grid from bottom left 
var curMouseY = 0;

// last system time delay between server and client
var delay = 0;

// id of last submitted piece
var lastSubmitted = -1;

// whether each player is active or not. 0-indexed because fuck you.
var active = [true, true, true, true];

var dragCorners = [];

function init() {    //sets up grid and remainingPieces
  for (var i = 1; i <= 4; i++) {
    highlightInfo(i, false);
  }
  $.get(url + "/info", initRequest);
}

const ordinal = ['1st', '2nd', '3rd', '4th'];
var endGameDisplayed = false;

function initRequest(data) {
  var foo = [];
  for (var i = 0; i < 21; i++) {
      foo[i] = 0;
  }
  remainingPieces = [0, foo.slice(0), foo.slice(0), foo.slice(0), foo.slice(0)];

  var response = JSON.parse(data);
  gameID = response._id;
  grid = response.board;
  players = response.players;
  var s = response.state;
  if (s == 0) gameStarted = false;
  if (s == 1) gameStarted = true;
  if (s == 2) gameOver = true;

  isLocal = response.params.privacy == 2;
  playerCount = response.params['num-players'];

  maxTime = response.params.timer;
  if (maxTime == 0) timed = false;

  var serverTime = parseInt(response.curr_move.timestamp.$numberLong);
  delay = Date.now() - response.sent;
  var curTime = Date.now() - delay;
  var difference = Math.ceil((curTime - serverTime) / 1000);
  if (difference < maxTime) startTime = serverTime;

  var loadedBy = response.loaded_by;
  _.each(response.players, function(p, i) {
    if (!p) return;

    _.each(p.pieces, function(p1) {
      remainingPieces[i+1][p1] = 1;
    });
    youControl[i + 1] = loadedBy == p._id;

    var name = p.name;
    // if local game
    if (response.params.privacy == 2) {
      // if 2 player game, else 4 player game
      if (response.params['num-players'] == 2) {
        name = "Player " + ((i % 2) + 1);
      } else {
        name = "Player " + (i + 1);
      }
    }
    $("#playerName" + (i + 1)).html(name);
    $("#playerScore" + (i + 1)).html(p.score);

    active[i] = p.playing;
    if (!active[i]) {
      $('#player' + (i + 1)).css('background-color', '#EEE');
    }
  });

  // if all your colors are inactive
  if (_.every(youControl, function(ctrl, i) {
    return !ctrl || !active[i - 1];
  })) {
    $('#inactive').show();
  }

  nextPlayer = response.curr_move.turn + 1;
  curPlayer = nextPlayer;

  for (var i = 1; i <= 4; i++) {
    $("#player" + i).css("border-color", colors[i]);
  }
  
  if (gameOver) {
    drawGrid();
    $(".icon-group").hide();
    $(".progress").hide();
    $("#alert").html("GAME COMPLETED");

    showGameResults();

    mode = "notYourTurn";
    update = null;
    $("#linkDiv").hide();
    return;
  }

  if (gameStarted) {
      $("#linkDiv").hide();
  } else {
    drawGrid();
    $(".icon-group").hide();
    $(".progress").hide();
    $("#alert").html("GAME NOT STARTED");
    mode = "notYourTurn";
    $("#link").val(url.replace(/play/i, 'join'));
    return;
  }

  if (timed) {
    update = setInterval(processTime, 1000);
  }
  startNewTurn(false);
}

const colorNames = ["Blue", "Yellow", "Red", "Green"];
function colorOf(player) {
  return colorNames[player - 1];
}

var showGameResults = function() {
  var scoreSort = function(a, b) {
    return b.score - a.score;
  }

  var scores = [];

  if (playerCount == 4) {
    for (var i = 0; i < players.length; i++) {
      var p = players[i];

      var pName = p.name;
      if (isLocal) pName = "Player " + (i+1);

      scores[i] = {
        name: pName,
        score: p.score,
        color: colorOf(i + 1)
      };
    }
  } else {
    var score1 = players[0].score + players[2].score;
    var score2 = players[1].score + players[3].score;
    var name1 = players[0].name;
    var name2 = players[1].name;
    if (isLocal) {
      name1 = "Player 1";
      name2 = "Player 2";
    }
    scores[0] = {
      name: name1,
      score: score1,
      color: "Blue/Red"
    };
    scores[1] = {
      name: name2,
      score: score2,
      color: "Yellow/Green"
    };
  }

  scores = scores.sort(scoreSort);

  var html = '';
  _.each(scores, function(score, i) {
    $("#results" + i).html( '<td>' +
      '<span class="ordinal">' + ordinal[i] + '</span></td>' +
      "<td>" + score.color + "</td>" +
      "<td>" + score.name + "</td>" + 
      "<td>" + score.score + ' pts' + "</td>");
  });

  if (!endGameDisplayed) {
    endGameDisplayed = true;
    $("#gameResults").modal('show');
  }
};

// humanizes a duration (given in seconds)
function humanize(time) {
  return time < 60 ? time : moment.duration(time * 1000).humanize();
}

function startNewTurn(resetTime) {
  rotate = [1, 0, 0, 1, 1];
  var newPlayer = nextPlayer;

  highlightInfo(curPlayer, false);
  highlightInfo(newPlayer, true);
  curPlayer = newPlayer;

  if (youControl[curPlayer])  {
    mode = "relaxing";
    $("#alert").html("YOUR MOVE!");
  } else {
    mode = "notYourTurn";
    $("#alert").html("Awaiting Opponent...");
  }

  drawGrid();
  $(".icon-group").hide();

  if (resetTime) startTime = Date.now() - delay;

  if (timed) {
    var d = Date.now() - delay;
    var remaining = Math.floor(maxTime - (d - startTime) / 1000);
    $(".progress").hide();
    $("#player" + curPlayer + " .progress").show();
    $("#player" + curPlayer + " .progress-bar")
      .attr("aria-valuenow", remaining)
      .attr("aria-valuemax", maxTime)
      .css("width", (((maxTime - remaining) / maxTime) * 100) + '%')
      .html(humanize(remaining));
  }
}

function processTime() {
  var d = Date.now() - delay;
  var remaining = Math.floor(maxTime - (d - startTime) / 1000);
  if (remaining < 0) {
      $.get(url + "/info", initRequest);
  } else {
    $(".progress").hide();
    $("#player" + curPlayer + " .progress").show();
    $("#player" + curPlayer + " .progress-bar")
      .attr("aria-valuenow", remaining)
      .attr("aria-valuemax", maxTime)
      .css("width", (((maxTime - remaining) / maxTime) * 100) + '%')
      .html(humanize(remaining));
  }
}

function score(player) {
  var score = 0;
  var pList = remainingPieces[player];
  for (var p = 0; p < pList.length; p++) {
    if (pList[p] == 0) {
      score += pieces[p].length / 2; 
    }
  }
  return score;
}

function orient(piece) { // yay math
  var p = pieces[piece].slice(0);
  for (var i = 0; i < p.length / 2; i++) {
    temp1 = rotate[0] * p[2 * i] 
        + rotate[1] * p[2 * i + 1];
    temp2 = rotate[2] * p[2 * i] 
        + rotate[3] * p[2 * i + 1];
    p[2 * i] = temp1;
    p[2 * i + 1] = temp2;
  }
  return p;
}

function rotLeft(r) {
  var temp1 = r[1];
  var temp2 = -1 * r[0];
  var temp3 = r[3];
  var temp4 = -1 * r[2];
  var temp5 = r[4];
  return [temp1, temp2, temp3, temp4, temp5];
}

function rotRight(r) {
  var temp1 = -1 * r[1];
  var temp2 = r[0];
  var temp3 = -1 * r[3];
  var temp4 = r[2];
  return [temp1, temp2, temp3, temp4, r[4]];
}

function flipVert(r) {
  return [r[0], r[1], -1 * r[2], -1 * r[3], (r[4] + 1) % 2];
}

function flipHoriz(r) {
  return [-1 * r[0], -1 * r[1], r[2], r[3], (r[4] + 1) % 2];
}

function highlightInfo(player, shouldHighlight) {
  if (shouldHighlight) {
    $("#player" + player).addClass("active");
  } else {
    $("#player" + player).removeClass("active");
  }
}

function drawGrid() {
  ctx.clearRect(0, 0, board.width, board.height);
  ctx.fillStyle = "white";
  ctx.fillRect(0,0,board.width,board.height);
  ctx.beginPath();

  for (var i = 0; i < 21; i++) {
    ctx.moveTo(SIZE * i, 0);
    ctx.lineTo(SIZE * i, 20 * SIZE);
    ctx.moveTo(0, SIZE * i);
    ctx.lineTo(20 * SIZE, SIZE * i);
  }

  ctx.stroke();

  for (var i = 0; i < 20; i++) {
    for (var j = 0; j < 20; j++) {
      fillGridSquare(grid[i][j], j, grid.length - 1 - i);
    }
  }

  if (mode == 'dragging' || mode == 'positioning') {
    _.each(dragCorners, function(square) {
      drawDot(curPlayer, square.y, grid.length - square.x - 1);
    });
  } else {
    var toDraw = hovering || curPlayer;
    if (!gameOver) {
      _.each(players[toDraw - 1].playable, function(square) {
        drawDot(toDraw, square.x, square.y);
      });
    }
  }

  drawSupply();
}

function drawSupply() {
  var hoveredPlayer = curPlayer;
  if (hovering != 0) {
    hoveredPlayer = hovering;
  }

  for (var m = 0; m < 3; m++) {
    for (var n = 0; n < 7; n++) {
      var piece = 3 * n + m;
      if (remainingPieces[hoveredPlayer][piece] == 1) {
        var xloc = supplyLeftEdge + (m * 6 + 2) * size2;
        var yloc = (n * 6 + 2) * size2;
        var color = players[hoveredPlayer - 1].playable_pieces[piece]
        ? colors[hoveredPlayer] : '#EEE';
        drawPiece(pieces[piece], color, xloc, yloc, size2);
      }
    }
  }
}

function fillSquare(color,x,y,width) { // x and y are bottom left corner
  ctx.save();
  ctx.fillStyle = color;
  ctx.fillRect(x, board.height - y - width, width, width);
  ctx.restore();
}


function fillPieceSquare(color, x, y, width) { // x and y are bottom left corner
  ctx.save();

  var grd = ctx.createLinearGradient(x, board.height - y - width, x, board.height - y - width + 4 * width);
  grd.addColorStop(0, color);
  grd.addColorStop(1, "black");

  ctx.fillStyle = grd;
  ctx.fillRect(x, board.height - y - width, width, width);

  ctx.restore();
}

// fills row x col y gridsquare with color of player
function fillGridSquare(player, x, y) { 
  if (player == 0) {
   fillSquare(colors[player], SIZE * x + 1, SIZE * y + 1, SIZE - 1, SIZE - 1);
  } else {
    fillPieceSquare(colors[player], SIZE * x + 1, SIZE * y + 1, SIZE - 1, SIZE - 1);
  }
}

function drawDot(player, x, y) { 
  ctx.save();
  ctx.beginPath();
  ctx.arc(SIZE * (x + .5), board.height - SIZE * (y + .5), dotSize, 0, 2 * Math.PI, false);
  ctx.fillStyle = colors[player];
  ctx.fill();
  ctx.restore();
}

// x and y are bottom left corner of 0,0 in the piece
function drawPiece(piece, color, x, y, squareSize) {
  for (var i = 0; i < piece.length / 2; i++) {
    fillPieceSquare(color, x + squareSize * piece[2 * i],
      y + squareSize * piece[2 * i + 1],squareSize);
  }
}

// http://stackoverflow.com/a/13542669/720889
function shadeColor2(color, percent) {
  var f=parseInt(color.slice(1),16),t=percent<0?0:255,p=percent<0?percent*-1:percent,R=f>>16,G=f>>8&0x00FF,B=f&0x0000FF;
  return "#"+(0x1000000+(Math.round((t-R)*p)+R)*0x10000+(Math.round((t-G)*p)+G)*0x100+(Math.round((t-B)*p)+B)).toString(16).slice(1);
}
function shadeRGBColor(color, percent) {
  var f=color.split(","),t=percent<0?0:255,p=percent<0?percent*-1:percent,R=parseInt(f[0].slice(4)),G=parseInt(f[1]),B=parseInt(f[2]);
  return "rgb("+(Math.round((t-R)*p)+R)+","+(Math.round((t-G)*p)+G)+","+(Math.round((t-B)*p)+B)+")";
}
function shade(color, percent) {
  if (color.length > 7) return shadeRGBColor(color,percent);
  else return shadeColor2(color,percent);
}

function drawCurPiece() {
  ctx.globalAlpha = 0.7;
  drawPiece(orient(curPiece), shade(colors[curPlayer], -0.5),
    SIZE * curPieceX, SIZE * curPieceY, SIZE);
  ctx.globalAlpha = 1;
}

function mouseOnPiece() {
  var p = orient(curPiece);
  var x = Math.floor(curMouseX / SIZE);
  var y = Math.floor(curMouseY / SIZE);
  for (var i = 0; i < p.length / 2; i++) {
    if (x == curPieceX + p[2 * i] && y == curPieceY + p[2 * i + 1]) {
      return true;
    }
  }
  return false;
}

function getMousePos(canvas, evt) {
  var rect = board.getBoundingClientRect();

  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}

// http://www.w3schools.com/js/js_cookies.asp
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

$(document).ready(function(){
  $('#slow .close').click(function() {
    $(this).parent().hide();
  });

  $('#linkDiv input').click(function() {
    this.select();
  });

  board.width = board.height * boardRatio;
  init();

  var wsProt = window.location.protocol == 'https:' ? 'wss' : 'ws';
  var conn = new WebSocket(wsProt + '://' + window.location.host + '/live');
  conn.onopen = function() {
    conn.send(getCookie('session'));
  };
  conn.onerror = function(error) {
    console.log('Connection error: ' + error);
    // TODO report to user?
  };
  // received when someone else makes a move
  conn.onmessage = function(msg) {
    var json = JSON.parse(msg.data);

    switch (json.code) {
      case 0:
        if (json.game_id != gameID) return;

        if (lastSubmitted != json.piece && youControl[json.turn + 1]) {
          $('#slow').show();
        }
        lastSubmitted = -1;

        curPiece = json.piece;
        curPieceX = json.x;
        curPieceY = json.y;
        rotate = getRotate(json.orientation);

        curPlayer = json.turn + 1;
        nextPlayer = json.next_player + 1;

        for (var i = 0; i < json.players.length; i++) {
          players[i].playable = json.players[i].playable;
          players[i].playable_pieces = json.players[i].playable_pieces;
        }

        active = json.active;
        _.each(active, function(act, i) {
          if (!act) {
            $('#player' + (i + 1)).css('background-color', '#EEE');
          }
        });

        // if all your colors are inactive
        if (_.every(youControl, function(ctrl, i) {
          return !ctrl || !active[i - 1];
        })) {
          $('#inactive').show();
        }

        if (nextPlayer == 0) {
          showGameResults();
        }

        submitMove();
        break;
      case 1:
        $.get(url + "/info", initRequest);
        break;
      default:
        alert('Unsupported socket code ' + json.code);
    }
  };
});
