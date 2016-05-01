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
    <#if hide == "login">
      <a href="/?hide=register">Already have an account?</a>
    <#else>
      <button class="btn btn-lg btn-primary" type="submit">Log in</button>
    </#if>
  </form>
  <form class="form-signin form-signup" action="/signup" method="post">
    <input type="hidden" name="username" />
    <input type="hidden" name="password" />
    <input type="hidden" name="dest" value="${dest}" />
    <#if hide == "register">
      <a href="/?hide=login">Need to make an account?</a>
    <#else>
      <button class="btn btn-lg btn-primary" type="submit">Register</button>
    </#if>
  </form>
</div>
</div>

</div><!-- /.row -->

</div><!-- /.container -->

</#assign>
<#assign js>index</#assign>
<#include "template.ftl">
