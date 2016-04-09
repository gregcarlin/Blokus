board = document.getElementById("board");
ctx = board.getContext("2d");
SIZE = 25;     					//size of squares
supplyLeftEdge = 25*SIZE; 
size2 = Math.floor(SIZE/2);     //size for supply
grid = []						//current game board

remainingPieces = [0,[],[],[],[]]; //0 is here for convenient player indexing

curPlayer = 1;  //players are 1,2,3,4
curPiece = 0;   //pieces range from 0 to 20
rotate = [1,0,0,1,1]; //2x2 matrix followed by parity, see orient function

mode = "relaxing"; 
//modes include "relaxing" -> "dragging" -> "positioning" 

curPieceX = 0; //grid locations
curPieceY = 0;

curMouseX = 0; //mouse locations within grid from bottom left 
curMouseY = 0;

function init() {    //sets up grid and remainingPieces
	var foo = [];
	for (i = 0; i < 21; i++) {
		foo[i] = 1;
	}
	remainingPieces = [0,foo.slice(0),foo.slice(0),
			foo.slice(0),foo].slice(0);
	for (i = 0; i < 20; i++) {
	grid[i] = [];
	for (j = 0; j < 20; j++) {
		grid[i][j] = 0;
}}
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



function drawGrid() {
	ctx.clearRect(0, 0, board.width, board.height);
	ctx.fillStyle = colors[0];
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
		fillGridSquare(grid[i][j],i,j);
	}}
	
	drawSupply();
}

function drawSupply() {
	for (m = 0; m < 3; m++) {
	for (n = 0; n < 7; n++) {
		var piece = 3*n + m;
		if (remainingPieces[curPlayer][piece] == 1) {
			var xloc = supplyLeftEdge + (m*6+2)*size2;
			var yloc = (n*6+2)*size2;
			drawPiece(pieces[piece],curPlayer,xloc,yloc,size2);
		}
	}}
	
}

function fillSquare(color,x,y,width) {  // x and y are bottom left corner
	ctx.save();
	ctx.fillStyle = color;
	ctx.fillRect(x,board.height-y-width,width,width);
	ctx.restore();
}

function fillGridSquare(player, x, y) {   //fills row x col y gridsquare with color of player
	if (player == 0) return;
	fillSquare(colors[player],SIZE*x,SIZE*y,SIZE,SIZE);
}

function drawPiece(piece,player,x,y,squareSize) {   // x and y are bottom left corner of 0,0 in the piece
	for (i = 0; i < piece.length/2; i++) {
		fillSquare(colors[player],x+squareSize*piece[2*i],
			y+squareSize*piece[2*i+1],squareSize);
	}
}

function drawCurPiece() {
	drawPiece(orient(curPiece),curPlayer, SIZE*curPieceX,SIZE*curPieceY,SIZE);
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

function getMousePos(canvas,evt) {
	var rect = board.getBoundingClientRect();

    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}

$("#board").mousemove(function(e){
	
	curMouseX = getMousePos(board,e).x;
	curMouseY = board.height - getMousePos(board,e).y;
	
	if (mode == "dragging") {
		curPieceX = Math.floor(curMouseX/SIZE);
		curPieceY = Math.floor(curMouseY/SIZE);
		drawGrid();
		drawCurPiece();
	}
});

$("#board").mousedown(function(e){
	
	if (mode == "positioning" && mouseOnPiece()) {
		mode = "dragging";
		$("i").hide();
		return;
	}
	if (curMouseX > supplyLeftEdge && curMouseX < supplyLeftEdge + 3*6*size2) {
		var supplyX = Math.floor((curMouseX - supplyLeftEdge)/(6*size2));
		var supplyY = Math.floor(curMouseY/(6*size2));
		var newPiece = supplyX + 3*supplyY;
		if (remainingPieces[curPlayer][newPiece] == 0) return;
		curPiece = newPiece;
		curPieceX = Math.floor(curMouseX/SIZE);
		curPieceY = Math.floor(curMouseY/SIZE);
		drawGrid();
		drawCurPiece();
		mode = "dragging";
		$("i").hide();
		rotate = [1,0,0,1,1];
	}
});

function toGrid(x) {
	return Math.floor(x/SIZE)*SIZE;
}

$("#board").mouseup(function(e){
	if (mode == "dragging") {
		if (curMouseX < 20*SIZE) {
			mode = "positioning";
			$("i").show();
			$("i").css("position","absolute");
			$("i").css("left",(toGrid(e.clientX)-48)+"px");
			$("#rot-right").css("top",(toGrid(e.clientY)-SIZE-10)+"px");
			$("#rot-left").css("top",(toGrid(e.clientY)-10)+"px");
			$("#flip-vert").css("top",(toGrid(e.clientY)+SIZE-10)+"px");
			$("#flip-horiz").css("top",(toGrid(e.clientY)+2*SIZE-10)+"px");
			$("#submit").css("top",(toGrid(e.clientY) + 3*SIZE-10)+"px");
			
			curPieceX = Math.floor(curMouseX/SIZE);
			curPieceY = Math.floor(curMouseY/SIZE);
		}
		else {
			mode = "relaxing";
			drawGrid();
		}
	}
});

function rotLeft() {
	var temp1 = rotate[1];
	var temp2 = -1*rotate[0];
	var temp3 = rotate[3];
	var temp4 = -1*rotate[2];
	rotate = [temp1,temp2,temp3,temp4,rotate[4]];
}

function rotRight() {
	var temp1 = -1*rotate[1];
	var temp2 = rotate[0];
	var temp3 = -1*rotate[3];
	var temp4 = rotate[2];
	rotate = [temp1,temp2,temp3,temp4,rotate[4]];
}

$("#rot-left").on('click', function() {
	if (rotate[4]) rotLeft();
	else rotRight();
	drawGrid();
	drawCurPiece();
});

$("#rot-right").on('click', function() {
	if (!rotate[4]) rotLeft();
	else rotRight();
	drawGrid();
	drawCurPiece();
});

$("#flip-vert").on('click', function() {
	rotate[2] = -1*rotate[2];
	rotate[3] = -1*rotate[3];
	rotate[4] = (rotate[4] + 1) % 2;
	drawGrid();
	drawCurPiece();
});

$("#flip-horiz").on('click', function() {
	rotate[0] = -1*rotate[0];
	rotate[1] = -1*rotate[1];
	rotate[4] = (rotate[4] + 1) % 2;
	drawGrid();
	drawCurPiece();
});


$("#submit").on('click', function() {

	//TODO IMPLEMENT CHECKS

	mode = "relaxing";
	var locs = orient(curPiece);
	for (i = 0; i < locs.length/2; i++) {
		grid[curPieceX+locs[2*i]][curPieceY+locs[2*i+1]] = curPlayer;
	}
	remainingPieces[curPlayer][curPiece] = 0;
	rotate = [1,0,0,1,1];
	curPlayer = (curPlayer + 1) % 5;
	if (curPlayer == 0) curPlayer++;

	drawGrid();
	$("i").hide();	
});

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
	board.width = board.height*2;
	init();
	drawGrid();
	$("i").hide();

  var conn = new WebSocket('ws://' + window.location.host + '/live');
  conn.onopen = function() {
    conn.send(getCookie('session'));
  };
  conn.onerror = function(error) {
    console.log('Connection error: ' + error);
    // TODO report to user?
  };
  // received when someone else makes a move
  conn.onmessage = function(msg) {
    var json = JSON.parse(msg);
    // TODO update screen
  };
});
