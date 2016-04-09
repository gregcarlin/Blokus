<#assign content>

<h1> PLAY BLOKUS </h1>
<canvas id = "board" width = "1000" height = "500">
</canvas>

<br>
<i id = "rot-right" class="material-icons">redo</i>
<i id = "rot-left" class="material-icons">undo</i>
<i id = "submit" class="material-icons">done</i>
<i id = "flip-vert" class="material-icons">swap_vert</i>
<i id = "flip-horiz" class="material-icons">swap_horiz</i>
<script src="/js/pieces.js"></script>
</#assign>
<#assign js>play</#assign>
<#include "template.ftl">
