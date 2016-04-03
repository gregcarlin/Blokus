<#assign content>

<form class="form-signin" action="/login" method="post">
  <h2 class="form-signin-heading">Please sign in</h2>
  <label for="inputEmail" class="sr-only">Email address</label>
  <input type="text" id="inputEmail" class="form-control" placeholder="Email address" required autofocus name="username" />
  <label for="inputPassword" class="sr-only">Password</label>
  <input type="password" id="inputPassword" class="form-control" placeholder="Password" required name="password" />
  <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
</form>
<form class="form-signin" action="/signup" method="post">
  <input type="hidden" name="username" />
  <input type="hidden" name="password" />
  <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
</form>

</#assign>
<#assign js>index</#assign>
<#include "template.ftl">
