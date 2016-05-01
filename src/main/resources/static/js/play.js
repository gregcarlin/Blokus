//GAME SETTINGS

//The colors the viewer is controlling.  0 is first for convenient indexing.
youControl = [0,true,false,true,false]; 

timed = true; // Timed or Untimed
maxTime = 15; // Time per move in seconds
gameStarted = true; // whether the game has started
gameOver = false;   // whether the game is over

startTime = null;
update = null;

remainingPieces = [0,[],[],[],[]]; //0 is here for convenient player indexing
grid = []					    	//current game board

curPlayer = 1;  //players are 1,2,3,4
nextPlayer = 1;



//BOARD PARAMETERS

boardRatio = 1.5;               //canvas width/height
SIZE = 25;     					//size of squares
supplyLeftEdge = 21*SIZE; 
size2 = Math.floor(SIZE/2);     //size for supply pieces
dotSize = SIZE/3.3;

colors = ["#EDEEEF"];
$(".playerInfo").each(function() {
  colors.push($(this).css("background-color"));
});


board = document.getElementById("board");
ctx = board.getContext("2d");
url = window.location.href; 
gameID = null;


curPiece = 0;   //pieces range from 0 to 20
rotate = [1,0,0,1,1]; //2x2 matrix followed by parity, see orient function

mode = "relaxing"; 
//modes include "relaxing" -> "dragging" -> "positioning" 
//also mode can be "notYourTurn"

hovering = 0; // which player is being hovered

curPieceX = 0; //grid locations
curPieceY = 0;

curMouseX = 0; //mouse locations within grid from bottom left 
curMouseY = 0;

// last system time delay between server and client
var delay = 0;

// id of last submitted piece
var lastSubmitted = -1;

function init() {    //sets up grid and remainingPieces
	
	for (i = 1; i <= 4; i++) {
		highlightInfo(i,false);
	}
	$(".gameResults").hide();
	$.get(url+"/info", initRequest);
}

function initRequest(data) {
	var foo = [];
	for (i = 0; i < 21; i++) {
		foo[i] = 0;
	}
	remainingPieces = [0,foo.slice(0),foo.slice(0),foo.slice(0),foo.slice(0)];
	
	var response = JSON.parse(data);
	gameID = response._id;
	grid = response.board;
	var s = response.state;
	if (s == 0) gameStarted = false;
	if (s == 1) gameStarted = true;
	if (s == 2) gameOver = true;
	
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

    _.each(p.pieces, function(idx) {
			var p1 = p.pieces[idx];
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
		
	});
	
	nextPlayer = response.curr_move.turn+1;
	curPlayer = nextPlayer;

	for (i = 1; i <= 4; i++) {
		$("#player" + i).css("border-color", colors[i]);
	}
	if (!gameStarted) {
		drawGrid();
		$("i").hide();
		$(".timed").hide();
		$("#alert").html("GAME NOT STARTED");
		mode = "notYourTurn";
		$("#link").html(url.replace(/play/i, 'join'));
		return;
	}
	if (gameStarted) {
		$(".linkPart").hide();
	}
	if (gameOver) {
		drawGrid();
		$("i").hide();
		$(".timed").hide();
		$("#alert").html("GAME COMPLETED");
		
		
		var scoreSort = function(a,b) {
   		 return a.score - b.score;
		}
		
		var scores = []
		
		for (i = 0; i < response.players.length; i++) {
			var p = response.players[i];
			scores[i] = {name:p.name,score:score(i+1)};
		}
		scores = scores.sort(scoreSort);
		for (i = 0; i < 4; i++) {
			$("#username"+(4-i)).html(scores[i].name);
			$("#score"+(4-i)).html(scores[i].score);
		}
			
		
		$(".gameResults").show();
		
		mode = "notYourTurn";
		update = null;
		return;
	}
	if (timed) {
		update = setInterval(processTime, 1000);
    $(".timed").show();
	}
	else {
		$(".timed").hide();
	}
	startNewTurn(false);
}

// humanizes a duration (given in seconds)
function humanize(time) {
  return time < 60 ? time : moment.duration(time * 1000).humanize();
}

function startNewTurn(resetTime) {
	rotate = [1,0,0,1,1];
	//var newPlayer = (curPlayer + 1) % 5;
	//if (newPlayer == 0) newPlayer++;
	var newPlayer = nextPlayer;
		
	highlightInfo(curPlayer,false);
	highlightInfo(newPlayer,true);
	curPlayer = newPlayer;

	if (youControl[curPlayer])  {
		mode = "relaxing";
		$("#alert").html("YOUR MOVE!");
	}
	else {
		mode = "notYourTurn";
		$("#alert").html("Awaiting Opponent...");
	}

	drawGrid();
	$(".icon-group").hide();

	if (resetTime) startTime = Date.now() - delay;

	var d = Date.now() - delay;
	var remaining = Math.floor(maxTime - (d - startTime) / 1000);
  $("#time").html(humanize(remaining));
}

function processTime() {
	var d = Date.now() - delay;
	var remaining = Math.floor(maxTime - (d - startTime) / 1000);
	if (remaining < 0) {
		$.get(url + "/info", initRequest);
	} else {
    $("#time").html(humanize(remaining));
  }
}

function score(player) {
	var score = 0;
	var pList = remainingPieces[player];
	for (p in pList) {
		if (pList[p] == 0) {
			score += pieces[p].length/2; 
		}
	}
	return score;
}

function orient(piece) {         // yay math
	var p = pieces[piece].slice(0);
	for (i = 0; i < p.length/2; i++) {
		temp1 = rotate[0]*p[2*i] 
			+rotate[1]*p[2*i+1];
		temp2 = rotate[2]*p[2*i] 
			+rotate[3]*p[2*i+1];
		p[2*i] = temp1;
		p[2*i+1] = temp2;
	}
	return p;
}

function rotLeft(r) {
	var temp1 = r[1];
	var temp2 = -1*r[0];
	var temp3 = r[3];
	var temp4 = -1*r[2];
	var temp5 = r[4];
	return [temp1,temp2,temp3,temp4,temp5];
}

function rotRight(r) {
	var temp1 = -1*r[1];
	var temp2 = r[0];
	var temp3 = -1*r[3];
	var temp4 = r[2];
	return [temp1,temp2,temp3,temp4,r[4]];
}

function flipVert(r) {
	return [r[0],r[1],-1*r[2],-1*r[3],(r[4] + 1) % 2];
}

function flipHoriz(r) {
	return [-1*r[0],-1*r[1],r[2],r[3],(r[4] + 1) % 2];
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
	
	for (i = 0;i<21;i++)
	{
	ctx.moveTo(SIZE*i,0);
	ctx.lineTo(SIZE*i,20*SIZE);
	ctx.moveTo(0,SIZE*i);
	ctx.lineTo(20*SIZE,SIZE*i);
	}
	
	ctx.stroke();

	for (i=0;i<20;i++) {
	for (j = 0;j<20;j++) {
		fillGridSquare(grid[i][j], j, grid.length - 1 - i);
	}}
	
	if (score(1) == 0) drawDot(1,0,19);
	if (score(2) == 0) drawDot(2,19,19);
	if (score(3) == 0) drawDot(3,19,0);
	if (score(4) == 0) drawDot(4,0,0);
	
	drawSupply();
}

function drawSupply() {
	
	hoveredPlayer = curPlayer;
	if (hovering != 0) {
		hoveredPlayer = hovering;
	}
	
	for (m = 0; m < 3; m++) {
	for (n = 0; n < 7; n++) {
		var piece = 3*n + m;
		if (remainingPieces[hoveredPlayer][piece] == 1) {
			var xloc = supplyLeftEdge + (m*6+2)*size2;
			var yloc = (n*6+2)*size2;
			drawPiece(pieces[piece], colors[hoveredPlayer], xloc, yloc, size2);
		}
	}}
	
}

function fillSquare(color,x,y,width) {  // x and y are bottom left corner
	ctx.save();
	ctx.fillStyle = color;
	ctx.fillRect(x,board.height-y-width,width,width);
	ctx.restore();
}


function fillPieceSquare(color,x,y,width) {  // x and y are bottom left corner
	ctx.save();
		
	var grd = ctx.createLinearGradient(x,board.height-y-width, x, board.height-y-width+4*width);
	grd.addColorStop(0, color);
	grd.addColorStop(1, "black");

	ctx.fillStyle = grd;
	ctx.fillRect(x,board.height-y-width,width,width);
	
	ctx.restore();
}

function fillGridSquare(player, x, y) {   //fills row x col y gridsquare with color of player
	if (player == 0) fillSquare(colors[player],SIZE*x+1,SIZE*y+1,SIZE-1,SIZE-1);
	//if (hovering != 0 && hovering != player) return;
	else fillPieceSquare(colors[player],SIZE*x+1,SIZE*y+1,SIZE-1,SIZE-1);
}

function drawDot(player, x, y) { 
	ctx.save();
	ctx.beginPath();
    ctx.arc(SIZE*(x+.5), board.height-SIZE*(y+.5), dotSize, 0, 2 * Math.PI, false);
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
function shade(color, percent){
  if (color.length > 7) return shadeRGBColor(color,percent);
  else return shadeColor2(color,percent);
}

function drawCurPiece() {
	drawPiece(orient(curPiece), shade(colors[curPlayer], -0.5),
            SIZE * curPieceX, SIZE * curPieceY, SIZE);
}

function mouseOnPiece() {
	var p = orient(curPiece);
	var x = Math.floor(curMouseX/SIZE);
	var y = Math.floor(curMouseY/SIZE);
	for (i = 0; i < p.length/2; i++) {
		if (x == curPieceX + p[2*i] && y == curPieceY + p[2*i+1])
			return true;
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

//mostRecentX = null;
//mostRecentY = null;

$(document).ready(function(){
  $('#slow .close').click(function() {
    $(this).parent().hide();
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
        //if (json.x == mostRecentX && json.y == mostRecentY) return;
        if (json.game_id != gameID) return;

        console.log("NETWORKING RESPONSE:",json);

        if (lastSubmitted != json.piece && youControl[json.turn + 1]) {
          $('#slow').show();
        }
        lastSubmitted = -1;

        //mostRecentX = json.x;
        //mostRecentY = json.y;

        curPiece = json.piece;
        curPieceX = json.x;
        curPieceY = json.y;
        rotate = getRotate(json.orientation);

        curPlayer = json.turn + 1;
        nextPlayer = json.next_player + 1;

        submitMove();
        break;
      case 1:
        $.get(url+"/info", initRequest);
        break;
      default:
        alert('Unsupported socket code ' + json.code);
    }
  };
});
