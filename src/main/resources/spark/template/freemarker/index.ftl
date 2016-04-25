<#assign content>

<video autoplay loop poster="/img/blokus.jpg" id="bgvid">
  <source src="/img/blokus.webm" type="video/webm">
  <source src="/img/blokus.mp4" type="video/mp4">
</video>

<div class="container container-index">

<div class="row">
<div class="col-md-8">
<h1>Blokus</h1>
</div>

<div class="col-md-4">
<div class="form-container">
  <#if error??>
    <div class="alert alert-danger" role="alert">${error?html}</div>
  </#if>
  <form class="form-signin" action="/login" method="post">
    <label for="inputEmail" class="sr-only">Username</label>
    <input type="text" id="inputEmail" class="form-control" placeholder="Username" required autofocus name="username" />
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required name="password" />
    <input type="hidden" name="dest" value="${dest}" />
    <button class="btn btn-lg btn-primary" type="submit">Sign in</button>
  </form>
  <form class="form-signin form-signup" action="/signup" method="post">
    <input type="hidden" name="username" />
    <input type="hidden" name="password" />
    <input type="hidden" name="dest" value="${dest}" />
    <button class="btn btn-lg btn-primary" type="submit">Sign up</button>
  </form>
</div>
</div>

</div><!-- /.row -->

</div><!-- /.container -->

</#assign>
<#assign js>index</#assign>
<#include "template.ftl">
