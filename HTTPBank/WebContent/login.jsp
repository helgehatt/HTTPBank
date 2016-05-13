<%@ include file="WEB-INF/jsp/head.jsp" %>

<style>

#login {
  margin-top: 40px;
  max-width: 360px;
  padding: 0;
}

#login img {
  margin-bottom: 40px;
}

#login .panel-heading {
  font-size: 16pt;
  background-color: #408F8F;
  color: #FFF;
}

#login button {
  display: block;
  margin-left: auto;
  margin-right: auto;
  padding: 6px 12px 6px 12px;
  background-color: #489DAE;
  color: #FFF;
}

</style>

</head>
<body>

<div class="container" id="login">
  <img class="img-responsive" src="${pageContext.request.contextPath}/images/Logo.jpg" alt="HTTP">
  <div class="panel panel-default">
    <div class="panel-heading">Login</div>
    <div class="panel-body">
      <form role="form" action="checkLogin" method="post">
        <div class="form-group">
          <label for="username">User name:</label>
          <input name="username" type="text" class="form-control" autofocus>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input name="password" type="password" class="form-control">
        </div>
        <button type="submit" class="btn btn-default btn-lg">Submit</button>
      </form>
    </div>
  </div>
</div>
		
</body>
</html>