//GAME SETTINGS

//The colors the viewer is controlling.  0 is first for convenient indexing.
youControl = [0,true,false,true,false]; 

timed = true; // Timed or Untimed
maxTime = 15; // Time per move in seconds
gameStarted = true; // whether the game has started
gameover = false;   // whether the game is over

startTime = null;

remainingPieces = [0,[],[],[],[]]; //0 is here for convenient player indexing
grid = []					    	//current game board

curPlayer = 1;  //players are 1,2,3,4

//BOARD PARAMETERS

boardRatio = 1.7;               //canvas width/height
SIZE = 25;     					//size of squares
supplyLeftEdge = 23*SIZE; 
size2 = Math.floor(SIZE/2);     //size for supply pieces

colors = ["#FFFFFF","#00A0FF","#EEEE00", "#FF4000","#00FF00"];


board = document.getElementById("board");
ctx = board.getContext("2d");


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

function init() {    //sets up grid and remainingPieces

	var foo = [];
	for (i = 0; i < 21; i++) {
		foo[i] = 0;
	}
	remainingPieces = [0,foo.slice(0),foo.slice(0),foo.slice(0),foo.slice(0)];


	var url = window.location.href; 
	
	$("#link").html(url.replace(/play/i, 'join'));
	
	$.get(url+"/info", function(data) {
		var response = JSON.parse(data);
		console.log(response);
		grid = response.board;
		var s = response.state;
		if (s == 0) gameStarted = false;
		if (s == 1) gameStarted = true;
		if (s == 2) gameOver = true;
		console.log(gameStarted);
		
		maxTime = response.params.timer;
		
		var serverTime = parseInt(response.curr_move.timestamp.$numberLong);
		var curTime = Date.now();
		var difference = Math.ceil((curTime - serverTime) / 1000);
		if (difference < maxTime) startTime = serverTime;
		
		
		var loadedBy = response.loaded_by;
		for (i = 0; i < response.players.length; i++) {
			var p = response.players[i];
			
			for (idx in p.pieces) {
				var p1 = p.pieces[idx];
				remainingPieces[i+1][p1] = 1;
			}
			if (loadedBy == p._id) 
				youControl[i+1] = true;
			else youControl[i+1] = false;
			
			$("#playerName"+(i+1)).html(p.name);
			console.log(p.name);
			
		}
		curPlayer = response.curr_move.turn+1;
		
	
		for (i = 1; i <= 4; i++) {
			$("#player" + i).css("border-color",colors[i]);
		}
		if (!gameStarted) {
			drawGrid();
			$("i").hide();
			$(".timed").hide();
			$("#alert").html("GAME NOT STARTED");
			mode = "notYourTurn";
			return;
		}
		
		if (timed) {
			var update = setInterval(processTime, 1000);
		}
		else {
			$(".timed").hide();
		}
		
		curPlayer = 0;
		startNewTurn(false);
	});
}

function startNewTurn(resetTime) {
	rotate = [1,0,0,1,1];
	var newPlayer = (curPlayer + 1) % 5;
	if (newPlayer == 0) newPlayer++;
		
	$("#player"+curPlayer).css("background-color",colors[0]);
	$("#player"+newPlayer).css("background-color",colors[newPlayer]);
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
	$("i").hide();
	
	if (resetTime) startTime = Date.now();
	$("#time").html(maxTime);
}

function processTime() {
	var d = Date.now();
	var remaining = Math.ceil(maxTime - (d - startTime) / 1000);
	if (remaining <= 0) {
		startNewTurn(true);
	}
	$("#time").html(remaining);
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
	//if (hovering != 0 && hovering != player) return;
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
