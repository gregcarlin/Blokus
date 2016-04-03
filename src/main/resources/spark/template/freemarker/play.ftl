<#assign content>

<h1> PLAY BLOKUS </h1>
<div id = "left">
<canvas id = "board" width = "500" height = "500">
</canvas>
</div>
<div> Piece-List: </div><br>

<table id = "pieceList" border = "1">
  <tr>
    <td>I1</td>
    <td>I2</td>		
    <td>I3</td>
    <td>V3</td>
    <td>I4</td>		
    <td>L4</td>
    <td>O4</td>
  </tr>
  <tr>
    <td>T4</td>
    <td>Z4</td>		
    <td>F5</td>
    <td>I5</td>
    <td>L5</td>		
    <td>N5</td>
    <td>P5</td>
  </tr>
  <tr>
    <td>T5</td>
    <td>U5</td>		
    <td>V5</td>
    <td>W5</td>
    <td>X5</td>		
    <td>Y5</td>
    <td>Z5</td>
  </tr>
</table><br>
<button id = "rotate">Rotate</button><br>
<button id = "flip">Flip</button><br>
<button id = "cancel">Cancel</button><br>
<button id = "submit">Submit</button><br>

<script src="/js/pieces.js"></script>
</#assign>
<#assign js>play</#assign>
<#include "template.ftl">
