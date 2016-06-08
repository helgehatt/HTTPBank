<%@ include file="../head.jsp" %>

<style>

#user-info {
	max-width: 600px;
}

#user-info .table {
	margin: 0;
}


</style>

<%@ include file="navbar.jsp" %>
	
<div id="user-info" class="container">
  <h3>User info</h3>
  <p>Your information is shown below.</p>
	
	<%@ include file="../content/user-info.jsp" %>
	
</div>


</body>
</html>