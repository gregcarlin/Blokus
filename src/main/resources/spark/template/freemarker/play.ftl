<#assign content>

<!-- Static navbar -->
<nav class="navbar navbar-default navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/auth/main">Blokus</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <ul class="nav navbar-nav navbar-right">
        <li><a class="static">${username}</a></li>
        <li><a href="/signout">Sign out</a></li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</nav>

<div class="container-fluid container-play">

<h1 id = "header"> 
	<span id = "alert"> GAME NOT STARTED</span> 
	<span class = "timed"> Time Remaining:&nbsp&nbsp </span>
	<span id = "time" class = "timed">0 </span>
</h1>

<div id = "players">
	<div id = "player1" class = "playerInfo"> 
    <div>
      <span id="playerName1" class="playerName">EMPTY</span>
      <span id="playerScore1" class="badge">0</span>
    </div>
	</div>
	<div id = "player2" class = "playerInfo"> 
    <div>
      <span id="playerName2" class="playerName">EMPTY</span>
      <span id="playerScore2" class="badge">0</span>
    </div>
	</div>
	<div id = "player3" class = "playerInfo">
    <div>
      <span id="playerName3" class="playerName">EMPTY</span>
      <span id="playerScore3" class="badge">0</span>
    </div>
	</div>
	<div id = "player4" class = "playerInfo"> 
    <div>
      <span id="playerName4" class="playerName">EMPTY</span>
      <span id="playerScore4" class="badge">0<span>
    </div>
	</div>
	<div id = "linkDiv">
		<span class = "linkPart"> LINK TO JOIN GAME: </span><br>
		<div id  = "link" class = "linkPart">  </div>
	</div>
</div>
<div id = "canvas">
	<canvas id = "board" height = "500">
	</canvas>
</div>

<div class="icon-group">
  <span id="rot-right" class="material-icons icon rotation">redo</span>
  <span id="rot-left" class="material-icons icon rotation">undo</span>
  <span id="flip-vert" class="material-icons icon flip">swap_vert</span>
  <span id="flip-horiz" class="material-icons icon flip">swap_horiz</span>
  <span id="submit" class="material-icons icon">done</span>
</div>

<table class = "gameResults">
	
	<tr>
		<td></td><td> <u>Game Results:</u> </td><td><button id = "closeResults">x</button></td>
	</tr>
	<tr>
		<td>1st</td>
		<td id = "username1"></td>
		<td> <span id = "score1">0</span> pts </td>
	</tr>
	<tr>
		<td>2nd</td>
		<td id = "username2"></td>
		<td> <span id = "score2">0</span> pts </td>
	</tr>
	<tr>
		<td>3rd</td>
		<td id = "username3"></td>
		<td> <span id = "score3">0</span> pts </td>
	</tr>
	<tr>
		<td>4th</td>
		<td id = "username4"></td>
		<td> <span id = "score4">0</span> pts </td>
	</tr>
</table>

</div><!-- /.container -->

<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment.min.js"></script>
<script src="/js/pieces.js"></script>
</#assign>
<#assign js>play</#assign>
<#assign js1>callbacks</#assign>
<#include "template.ftl">
