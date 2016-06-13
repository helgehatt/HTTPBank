<%@ include file="../head.jsp" %>

<meta http-equiv="refresh" content="5; url=${pageContext.request.contextPath}"/>

<style>

#forbidden-access {
	text-align: center;
	max-width: 800px;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="forbidden-access" class="container">
	  <h2>Unauthorized access</h2>
	  <br>
	  <h4>You are not logged in, or your session has expired.</h4>
	  <h4>Redirecting to login page in 5 seconds.</h4>
</div>

</body>
</html>