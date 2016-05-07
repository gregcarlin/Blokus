$(".playerInfo").hover(function(e){
  var p = $(this).attr('id').slice(-1);
  hovering = p;
  drawGrid();
  if (mode == "positioning") drawCurPiece();
}, function(e) {
  var p = $(this).attr('id').slice(-1);
  hovering = 0;
  drawGrid();
  if (mode == "positioning") drawCurPiece();
});

function setCursor(newCursor) {
  $("#board").css("cursor", newCursor);
}
function fixCursor() {
  switch (mode) {
    case "notYourTurn":
      setCursor("default");
      break;
    case "relaxing":
      if (curMouseX > supplyLeftEdge
          && curMouseX < supplyLeftEdge + 3 * 6 * size2) {
        setCursor("move");
      } else {
        setCursor("default");
      }
      break;
    case "dragging":
      setCursor("move");
      break;
    case "positioning":
      if ((curMouseX > supplyLeftEdge
           && curMouseX < supplyLeftEdge + 3 * 6 * size2)
          || mouseOnPiece()) {
        setCursor("move");
      } else {
        setCursor("default");
      }
      break;
  }
}

$("#board").mousemove(function(e) {
  var mousePos = getMousePos(board, e);
  curMouseX = mousePos.x;
  curMouseY = board.height - mousePos.y;

  if (mode == "dragging") {
    curPieceX = Math.floor(curMouseX / SIZE);
    curPieceY = Math.floor(curMouseY / SIZE);
    drawGrid();
    drawCurPiece();
  }

  fixCursor();
});

$("#board").mousedown(function(e) {
  if (mode == "positioning" && mouseOnPiece()) {
      mode = "dragging";
      $(".icon-group").hide();
      return false;
  }

  if ((mode != "notYourTurn") &&
          (curMouseX > supplyLeftEdge
           && curMouseX < supplyLeftEdge + 3 * 6 * size2)) {
    var supplyX = Math.floor((curMouseX - supplyLeftEdge) / (6 * size2));
    var supplyY = Math.floor(curMouseY / (6 * size2));
    var newPiece = supplyX + 3 * supplyY;
    if (remainingPieces[curPlayer][newPiece] == 0) return;
    curPiece = newPiece;
    curPieceX = Math.floor(curMouseX/SIZE);
    curPieceY = Math.floor(curMouseY/SIZE);
    drawGrid();
    drawCurPiece();
    mode = "dragging";
    dragCorners = cornersForPiece();

    $(".icon-group").hide();
    rotate = [1,0,0,1,1];
  }
  return false;
});

function toGrid(x) {
  return Math.floor(x / SIZE) * SIZE;
}

$(window).resize(function() {
  var group = $('.icon-group');
  var offset = $('#board').offset();
  group.css("left", (curPieceX * SIZE + offset.left + 75) + "px");
  group.css("top", ((grid.length - curPieceY) * SIZE + offset.top - 50) + "px");
});

$("#board").mouseup(function(e) {
  if (mode == "dragging") {
    $("#board").css("cursor", "default");
    if (curMouseX < 20 * SIZE) {
      mode = "positioning";

      curPieceX = Math.floor(curMouseX / SIZE);
      curPieceY = Math.floor(curMouseY / SIZE);

      $('#loading').hide();
      $('.icon').show();
      var group = $('.icon-group');
      group.removeClass().addClass('icon-group');
      group.addClass('piece-' + curPiece);
      var offset = $('#board').offset();
      group.css("left", (curPieceX * SIZE + offset.left + 75) + "px");
      group.css("top",
                ((grid.length - curPieceY) * SIZE + offset.top - 50) + "px");
      group.show();

      displaySubmit();
    } else {
      mode = "relaxing";
      drawGrid();
    }
  }
});

$(".icon").mousedown(function(e) {
  return false;
});

$("#rot-left").on('click', function() {
  if (rotate[4]) rotate = rotLeft(rotate);
  else rotate = rotRight(rotate);
  drawGrid();
  drawCurPiece();
  displaySubmit(); 
});

$("#rot-right").on('click', function() {
  if (!rotate[4]) rotate = rotLeft(rotate);
  else rotate = rotRight(rotate);
  drawGrid();
  drawCurPiece();
  displaySubmit(); 
});

$("#flip-vert").on('click', function() {
  rotate = flipVert(rotate);
  drawGrid();
  drawCurPiece();
  displaySubmit(); 
});

$("#flip-horiz").on('click', function() {
  rotate = flipHoriz(rotate);
  drawGrid();
  drawCurPiece();
  displaySubmit(); 
});

function displaySubmit() {
  if (isLegal()) {
      $("#submit").show();
  } else {
      $("#submit").hide();
  }
}

function isColor(color,row,column) {
  if (row < 0 || column < 0 || row >= 20 || column >= 20) return false;
  return (grid[row][column] == color);
}

// Gets corners where the currently selected piece can be played
function cornersForPiece() {
  var corners = getCorners();
  var moveCorners = [];
  var rotateBak = rotate;
  var curPieceXBak = curPieceX;
  var curPieceYBak = curPieceY;
  for (var o = 0; o < 8; o++) {
    rotate = getRotate(o);
    for (curPieceX = 0; curPieceX < 20; curPieceX++) {
      for (curPieceY = 0; curPieceY < 20; curPieceY++) {
        if (isLegal()) {
          var locs = orient(curPiece);
          for (var i = 0; i < locs.length / 2; i++) {
            var row = grid.length - 1 - (curPieceY + locs[2 * i + 1]);
            var col = curPieceX + locs[2 * i];
            var s = {x: row, y: col};
            if (_.some(corners, _.partial(_.isEqual, s))) {
              moveCorners.push(s);
            }
          }
        }
      }
    }
  }
  rotate = rotateBak;
  curPieceX = curPieceXBak;
  curPieceY = curPieceYBak;
  return moveCorners;
}

function getPlaces() {
  var places = new Set();
  for (var r = 0; r < 20; r++) {
    for (var c = 0; c < 20; c++) {
      if (grid[r][c] == curPlayer) {
        places.add(String([r, c]));
      }
    }
  }
  return places;
}

// Get all corners
function getCorners() {
  var corners = [];
  if (score(curPlayer) == 0) {
    if (curPlayer == 1) {
      corners.push({x: 0, y: 0});
    } else if (curPlayer == 2) {
      corners.push({x: 0, y: 19});
    } else if (curPlayer == 3) {
      corners.push({x: 19, y: 19});
    } else {
      corners.push({x: 19, y: 0});
    }
    return corners;
  }
  var places = getPlaces();
  places.forEach(function(place) {
    place = place.split(",");
    checkCorner(corners, parseInt(place[0]) - 1, parseInt(place[1]) - 1, places);
    checkCorner(corners, parseInt(place[0]) - 1, parseInt(place[1]) + 1, places);
    checkCorner(corners, parseInt(place[0]) + 1, parseInt(place[1]) - 1, places);
    checkCorner(corners, parseInt(place[0]) + 1, parseInt(place[1]) + 1, places);
  });
  return corners;
}

// If (x, y) is a corner, adds (x, y) to set s
function checkCorner(s, x, y, places) {
  if (grid[x] && grid[x][y] == 0
          && notSame(x - 1, y)
          && notSame(x + 1, y)
          && notSame(x, y - 1)
          && notSame(x, y + 1)) {
    s.push({x: x, y: y});
  }
}

function notSame(x, y, places) {
  if (grid[x] && grid[x][y] == curPlayer) {
    return false;
  }
  return true;
}

function isLegal() {
  var locs = orient(curPiece);

  for (var i = 0; i < locs.length/2; i++) {
    var row =  grid.length - 1 - (curPieceY + locs[2 * i + 1]);
    var col = curPieceX + locs[2 * i];

    if (row < 0 || row >= 20 || col < 0 || col >= 20) {
      return false;
    }
    if (grid[row][col] != 0) {
      return false;
    }
  }

  if (score(curPlayer) == 0) {
    for (var i = 0; i < locs.length / 2; i++) {
      var row =  grid.length - 1 - (curPieceY + locs[2 * i + 1]);
      var col = curPieceX + locs[2 * i];
      if (curPlayer == 1 && row == 0 && col == 0) return true;
      if (curPlayer == 2 && row == 0 && col == 19) return true;
      if (curPlayer == 3 && row == 19 && col == 19) return true;
      if (curPlayer == 4 && row == 19 && col == 0) return true;
    }
    return false;
  }

  for (var i = 0; i < locs.length / 2; i++) {
    var row =  grid.length-1-(curPieceY+locs[2*i+1]);
    var col = curPieceX+locs[2*i];

    if (isColor(curPlayer,row + 1,col) || 
        isColor(curPlayer,row - 1,col) || 
        isColor(curPlayer,row,col + 1) || 
        isColor(curPlayer,row,col - 1))
            return false;
  }

  for (var i = 0; i < locs.length / 2; i++) {
    var row =  grid.length - 1 - (curPieceY + locs[2 * i + 1]);
    var col = curPieceX + locs[2 * i];

    if (isColor(curPlayer,row + 1,col + 1) || 
        isColor(curPlayer,row + 1,col - 1) || 
        isColor(curPlayer,row - 1,col + 1) || 
        isColor(curPlayer,row - 1,col - 1)) 
            return true;    
  }
  return false;
}

function submitMove() {
  var locs = orient(curPiece);
  for (var i = 0; i < locs.length / 2; i++) {
    grid[grid.length - 1 - (curPieceY + locs[2 * i + 1])][curPieceX + locs[2 * i]] = curPlayer;
  }
  remainingPieces[curPlayer][curPiece] = 0;
  var newScore = score(curPlayer);
  players[curPlayer - 1].score = newScore;
  $("#playerScore" + curPlayer).html(newScore);
  $(".icon-group").hide();

  startNewTurn(true);
}

$("#submit").on('click', function() {
  $('.icon').hide();
  $('#loading').show();
  lastSubmitted = curPiece;
  $.post(url + "/move", {
    piece: curPiece,
    orientation: getOrientation(rotate),
    x: curPieceX,
    y: curPieceY
  },
  function(data) {
    data = JSON.parse(data);
    if (!data.success) {
      $('#slow').show();
    }
  });
});
