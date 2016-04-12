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

<h1> PLAY BLOKUS </h1>
<canvas id = "board" width = "1000" height = "500">
</canvas>

<br>
<i id = "rot-right" class="material-icons">redo</i>
<i id = "rot-left" class="material-icons">undo</i>
<i id = "submit" class="material-icons">done</i>
<i id = "flip-vert" class="material-icons">swap_vert</i>
<i id = "flip-horiz" class="material-icons">swap_horiz</i>

</div><!-- /.container -->

<script src="/js/pieces.js"></script>
</#assign>
<#assign js>play</#assign>
<#include "template.ftl">
