<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>HTTP Bank</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery-1.12.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>


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