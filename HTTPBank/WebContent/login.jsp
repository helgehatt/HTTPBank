<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>HTTP Bank</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
	</head>
	<body>
		<div class="container" id="login-container">
		  <img class="img-responsive" src="${pageContext.request.contextPath}/images/Logo.jpg" alt="HTTP">
		  <div class="panel panel-default">
		    <div class="panel-heading">Login</div>
		    <div class="panel-body">
		      <form role="form" action="checkLogin" method="post">
		        <div class="form-group">
		          <label for="username">User name:</label>
		          <input name="username" type="text" class="form-control">
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