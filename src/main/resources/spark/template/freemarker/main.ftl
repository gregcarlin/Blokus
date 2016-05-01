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

<div class="container container-main">

  <!-- Main component for a primary marketing message or call to action -->
  <div class="row">
    <div class="col-md-6">
      <h2>Create a new game</h2>
      <form class="form-create" action="/auth/new" method="post">
        <div class="btn-group btn-group-justified" data-toggle="buttons">
          <label class="btn btn-primary active" title="Anyone online will be able to join this game.">
            <input type="radio" name="type" id="public" autocomplete="off" checked value="public"> Public 
          </label>
          <label class="btn btn-primary" title="Only friends you invite can join this game.">
            <input type="radio" name="type" id="private" autocomplete="off" value="private"> Private
          </label>
          <label class="btn btn-primary" title="Play with your friends on your computer.">
            <input type="radio" name="type" id="local" autocomplete="off" value="local"> Local
          </label>
        </div>
        <div id="status"></div>

        <div class="btn-group btn-group-justified" data-toggle="buttons">
          <label class="btn btn-primary">
            <input type="radio" name="count" id="2player" autocomplete="off" value="2"> 2 Player
          </label>
          <label class="btn btn-primary active">
            <input type="radio" name="count" id="4player" autocomplete="off" checked value="4"> 4 Player
          </label>
        </div>

        <input type="hidden" name="timer" id="timer-actual" />
        <div class="input-group input-group-timer">
          <input type="number" class="form-control" min="0" name="timer-display" id="timer" aria-describedby="timer-addon" value="0" required placeholder="Timer" />
          <div class="input-group-btn">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span id="unit">Seconds</span> <span class="caret"></span></button>
            <ul class="dropdown-menu dropdown-menu-right dropdown-timer">
              <li><a href="#" data-mult="1">Seconds</a></li>
              <li><a href="#" data-mult="60">Minutes</a></li>
              <li><a href="#" data-mult="3600">Hours</a></li>
              <li><a href="#" data-mult="86400">Days</a></li>
            </ul>
          </div>
        </div>
        <a href="#" id="timer-btn">Add Timer</a>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Create Game</button>
      </form>

      <div class="panel panel-info" id="tips">
        <div class="panel-heading">Did you know?</div>
        <div class="panel-body">
          ${tip}
        </div>
      </div>
    </div>

    <div class="col-md-6 current-games">
      <h2>
        Current Games
        <small>Where you are currently playing</small>
      </h2>
      <div class="limited">
        <table class="table table-striped table-hover table-current">
          <thead>
            <tr>
              <th></th>
              <th>Players</th>
              <th>Max</th>
              <th>Timer</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td colspan="4">Loading...</td>
            </tr>
          </tbody>
        </table>
        <div class="shadow"></div>
      </div>

      <h2>
        Public Games
        <small>Where you can join others</small>
      </h2>
      <div class="limited">
        <table class="table table-striped table-hover table-public">
          <thead>
            <tr>
              <th>Players</th>
              <th>Max</th>
              <th>Timer</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td colspan="3">Loading...</td>
            </tr>
          </tbody>
        </table>
        <div class="shadow"></div>
      </div>
    </div>
  </div>

</div> <!-- /container -->

</#assign>
<#assign js>main</#assign>
<#include "template.ftl">
