<%@ include file="WEB-INF/jsp/head.jsp" %>

<style>

#login {
  max-width: 360px;
  padding: 10px;
}

#login img {
  margin-bottom: 20px;
}

#login .panel-heading {
  font-size: 16pt;
  background-color: #408F8F;
  border-bottom: none;
  color: #FFF;
}

#login .panel-body {
	padding: 15px;
}

#login button {
  display: block;
  margin-left: auto;
  margin-right: auto;
  padding: 6px 12px 6px 12px;
  background-color: #489DAE;
  color: #FFF;
}

#error {
	background-color: #DE2525;
	color: white;
	text-align: center;
	height: 40px;
	width: 100%;
	padding-top: 10px;
}

</style>

</head>
<body>

<div class="container" id="login">
  <img class="img-responsive" src="${pageContext.request.contextPath}/images/Logo.jpg" alt="HTTP">
  <div class="panel panel-default">
    <div class="panel-heading">Login</div>
   	<c:if test="${param.s==0}">
	    <div id="error">
	   		Wrong user name or password
	    </div>
   	</c:if>
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