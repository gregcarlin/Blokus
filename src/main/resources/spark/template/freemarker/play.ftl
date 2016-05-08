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
        <li><a href="#" data-toggle="modal" data-target="#rulesModal">Rules</a></li>
        <li><a href="/signout">Sign out</a></li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</nav>

<!-- Rules modal -->
<div class="modal fade" id="rulesModal" tabindex="-1" role="dialog" aria-labelledby="rulesModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="rulesModalLabel">Blokus Rules</h4>
      </div>
      <div class="modal-body">
        Rules go here
      </div>
    </div>
  </div>
</div>

<div class="container-fluid container-play">

<h1 id="header"> 
  <span id="alert">GAME NOT STARTED</span> 
</h1>

<div class="alert alert-info alert-dismissible" role="alert" id="slow">
  <button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  <strong>Too slow!</strong> A random move was made for you.
</div>

<div class="alert alert-info alert-dismissible" role="alert" id="inactive">
  <strong>Good game!</strong> You have no more available moves.
</div>

<div id="players">
  <div id="player1" class="playerInfo"> 
    <div>
      <span id="playerName1" class="playerName">EMPTY</span>
      <span id="playerScore1" class="badge">0</span>
    </div>
    <div class="progress">
      <span class="progress-text"></span>
      <div class="progress-bar progress-bar-info progress-bar-striped active" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
      </div>
    </div>
  </div>

  <div id="player2" class="playerInfo"> 
    <div>
      <span id="playerName2" class="playerName">EMPTY</span>
      <span id="playerScore2" class="badge">0</span>
    </div>
    <div class="progress">
      <span class="progress-text"></span>
      <div class="progress-bar progress-bar-warning progress-bar-striped active" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
      </div>
    </div>
  </div>

  <div id="player3" class="playerInfo">
    <div>
      <span id="playerName3" class="playerName">EMPTY</span>
      <span id="playerScore3" class="badge">0</span>
    </div>
    <div class="progress">
      <span class="progress-text"></span>
      <div class="progress-bar progress-bar-danger progress-bar-striped active" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
      </div>
    </div>
  </div>

  <div id="player4" class="playerInfo"> 
    <div>
      <span id="playerName4" class="playerName">EMPTY</span>
      <span id="playerScore4" class="badge">0<span>
    </div>
    <div class="progress">
      <span class="progress-text"></span>
      <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
      </div>
    </div>
  </div>

  <div id="linkDiv" class="panel panel-info">
    <div class="panel-heading">Invite your friends!</div>
    Give your friends the following link so they can join this game:
    <input id="link" class="form-control" type="text" value="test" readonly />
  </div>
</div>

<div id="canvas">
  <canvas id="board" height="500">
  </canvas>
</div>

<div class="icon-group">
  <span id="rot-right" class="material-icons icon rotation">redo</span>
  <span id="rot-left" class="material-icons icon rotation">undo</span>
  <span id="flip-vert" class="material-icons icon flip">swap_vert</span>
  <span id="flip-horiz" class="material-icons icon flip">swap_horiz</span>
  <span id="submit" class="material-icons icon">done</span>
  <span id="loading" class="fa fa-spinner fa-spin"></span>
</div>

<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" id="gameResults">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Game Results</h4>
      </div>
      <div class="modal-body">
        <table id="score-list">
        <tr id="results0"></tr>
        <tr id="results1"></tr>
        <tr id="results2"></tr>
        <tr id="results3"></tr>
        </table>
      </div>
    </div>
  </div>
</div>

</div><!-- /.container -->

<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment.min.js"></script>
<script src="/js/pieces.js"></script>
</#assign>
<#assign js>play</#assign>
<#assign js1>callbacks</#assign>
<#include "template.ftl">
