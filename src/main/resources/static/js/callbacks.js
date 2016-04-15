$(".playerInfo").hover(function(e){
	var p = $(this).attr('id').slice(-1);
	$(this).css("background-color",colors[p]);
	hovering = p;
	drawGrid();
	if (mode == "positioning") drawCurPiece();
},function(e){
	var p = $(this).attr('id').slice(-1);
	if (p != curPlayer) {
		$(this).css("background-color","white");
	}
	hovering = 0;
	drawGrid();
	if (mode == "positioning") drawCurPiece();
});


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
	var newPlayer = (curPlayer + 1) % 5;
	if (newPlayer == 0) newPlayer++;
	
	$("#player"+curPlayer).css("background-color",colors[0]);
	$("#player"+newPlayer).css("background-color",colors[newPlayer]);
	curPlayer = newPlayer;

	drawGrid();
	$("i").hide();	
});