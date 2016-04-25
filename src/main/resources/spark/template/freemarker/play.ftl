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
  <span id = "rot-right" class="material-icons icon">redo</span>
  <span id = "rot-left" class="material-icons icon">undo</span>
  <span id = "flip-vert" class="material-icons icon">swap_vert</span>
  <span id = "flip-horiz" class="material-icons icon">swap_horiz</span>
  <span id = "submit" class="material-icons icon">done</span>
</div>

</div><!-- /.container -->

<script src="/js/pieces.js"></script>
</#assign>
<#assign js>play</#assign>
<#assign js1>callbacks</#assign>
<#include "template.ftl">
