<%@ include file="../head.jsp" %>

<style>

#account-info {
	max-width: 600px;
}

#account-info .table {
	margin: 0;
}

#account-info-nav {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>
	
<div id="account-info" class="container">
  <h3>Account info</h3>
  <p>Information about your account is shown below.</p>
	
	<%@ include file="../content/account-info.jsp" %>
	
</div>


</body>
</html>