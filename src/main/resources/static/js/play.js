board = document.getElementById("board");
ctx = board.getContext("2d");
boardRatio = 1.7;               //canvas width/height
SIZE = 25;     					//size of squares
supplyLeftEdge = 23*SIZE; 
size2 = Math.floor(SIZE/2);     //size for supply
grid = []						//current game board

colors = ["#FFFFFF","#0000FF","#EEEE00", "#FF0000","#00FF00"];

remainingPieces = [0,[],[],[],[]]; //0 is here for convenient player indexing

curPlayer = 1;  //players are 1,2,3,4
curPiece = 0;   //pieces range from 0 to 20
rotate = [1,0,0,1,1]; //2x2 matrix followed by parity, see orient function

mode = "relaxing"; 
//modes include "relaxing" -> "dragging" -> "positioning" 

hovering = 0;

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
	
	$("#player1").css("background-color",colors[1]);
	
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
			drawPiece(pieces[piece],hoveredPlayer,xloc,yloc,size2);
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
	if (hovering != 0 && hovering != player) return;
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
	board.width = board.height*boardRatio;
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
