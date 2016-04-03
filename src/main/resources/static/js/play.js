board = document.getElementById("board");
ctx = board.getContext("2d");
SIZE = 25;
grid = []
for (i = 0; i < 20; i++) {
	grid[i] = [];
	for (j = 0; j < 20; j++) {
		grid[i][j] = 0;
}}
curPlayer = 1;
curPiece = 0;
rotate = [1,0,0,1];
mode = "relaxing";

curPieceX = 0;
curPieceY = 0;

curMouseX = 0;
curMouseY = 0;

function orient(piece) {
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

$("td").on('click', function() {
	var tag = $(this).text();
	curPiece = pieceVals[tag];
	if (mode == "positioning") {
		drawGrid();
		drawCurPiece();
		return;
	}
	mode = "placing";
});

function drawGrid() {
	ctx.clearRect(0, 0, board.width, board.height);
	ctx.beginPath();
	for (i = 0;i<20;i++)
	{
	ctx.moveTo(SIZE*i,0);
	ctx.lineTo(SIZE*i,20*SIZE);
	ctx.moveTo(0,SIZE*i);
	ctx.lineTo(20*SIZE,SIZE*i);
	}
	ctx.stroke();

	for (i=0;i<20;i++) {
	for (j = 0;j<20;j++) {
		fillSquare(grid[i][j],i,j);
	}}
}

function fillSquare(player, x, y) {
	ctx.fillStyle = colors[player];
	ctx.fillRect(SIZE*x+1,20*SIZE-SIZE*(y+1)+1,SIZE-2,SIZE-2);
}

function drawCurPiece() {
	var locs = orient(curPiece);
	for (i = 0; i < locs.length/2; i++) {
		fillSquare(curPlayer,curPieceX+locs[2*i],curPieceY+locs[2*i+1]);
	}
}



function getMousePos(canvas,evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}


$("#board").mousemove(function(e){
	
	var col = Math.floor(getMousePos(board,e).x/SIZE);
	var row = Math.floor((500-getMousePos(board,e).y)/SIZE);
	curMouseX = col;
	curMouseY = row;
	
	if (mode != "placing") return;
	curPieceX = col;
	curPieceY = row;
	drawGrid();
	drawCurPiece();
});

$("#board").mouseup(function(e){
	if (mode == "placing") {
		mode = "positioning";
		$("button").show();
		curPieceX = curMouseX;
		curPieceY = curMouseY;
	}
});

$("#rotate").on('click', function() {

	var temp1 = rotate[1];
	var temp2 = -1*rotate[0];
	var temp3 = rotate[3];
	var temp4 = -1*rotate[2];
	
	rotate = [temp1,temp2,temp3,temp4];
	drawGrid();
	drawCurPiece();
});

$("#flip").on('click', function() {
	
	rotate[2] = -1*rotate[2];
	rotate[3] = -1*rotate[3];
	drawGrid();
	drawCurPiece();
});


$("#cancel").on('click', function() {
	mode = "placing";
	drawGrid();
	$("button").hide();
});

$("#submit").on('click', function() {
	mode = "relaxing";
	var locs = orient(curPiece);
	for (i = 0; i < locs.length/2; i++) {
		grid[curPieceX+locs[2*i]][curPieceY+locs[2*i+1]] = curPlayer;
	}
	curPlayer = (curPlayer + 1) % 5;
	if (curPlayer == 0) curPlayer++;

	drawGrid();
	$("button").hide();	
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
	drawGrid();
	$("button").hide();

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
