<%@ include file="../head.jsp" %>

<style>

#account-info-nav {
	background-color:#e6e6e6;
}

.panel-body {
	padding: 15px;
}

</style>

<%@ include file="navbar.jsp" %>
	
<div id="account-info" class="container container-sm">
  <h3>Account info</h3>
  <p>Information about your account is shown below.</p>
	
	<%@ include file="../content/account-info.jsp" %>
	
</div>


</body>
</html>