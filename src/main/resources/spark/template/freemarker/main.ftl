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
        <li><a href="/signout">Sign out</a></li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</nav>

<div class="container">

  <!-- Main component for a primary marketing message or call to action -->
  <div class="row">
    <div class="col-md-6">
      <h2>Create a new game</h2>
      <form action="/auth/new" method="post">
        <div class="btn-group btn-group-justified" data-toggle="buttons">
          <label class="btn btn-primary active">
            <input type="radio" name="type" id="public" autocomplete="off" checked> Public 
          </label>
          <label class="btn btn-primary">
            <input type="radio" name="type" id="private" autocomplete="off"> Private
          </label>
          <label class="btn btn-primary">
            <input type="radio" name="type" id="local" autocomplete="off"> Local
          </label>
        </div>

        <div class="btn-group btn-group-justified" data-toggle="buttons">
          <label class="btn btn-primary active">
            <input type="radio" name="count" id="public" autocomplete="off"> 2 Player
          </label>
          <label class="btn btn-primary">
            <input type="radio" name="count" id="private" autocomplete="off" checked> 4 Player
          </label>
        </div>

        <div class="input-group">
          <span class="input-group-addon" id="timer-addon">Timer (sec)</span>
          <input type="number" class="form-control" min="0" name="timer" id="timer" aria-describedby="timer-addon" />
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Create Game</button>
      </form>
    </div>

    <div class="col-md-6">
      <h2>Current Games</h2>
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>Player Count</th>
            <th>Timer</th>
          </tr>
        </thead>
      </table>

      <h2>Public Games</h2>
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>Player Count</th>
            <th>Timer</th>
          </tr>
        </thead>
      </table>
    </div>
  </div>

</div> <!-- /container -->

</#assign>
<#assign js>index</#assign>
<#include "template.ftl">
