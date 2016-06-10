<%@ include file="../head.jsp" %>

<style>

.panel-body {
	padding: 15px;
}

</style>

<%@ include file="navbar.jsp" %>
	
<div id="user-info" class="container container-sm">
  <h3>User info</h3>
  <p>Your information is shown below.</p>
	
	<%@ include file="../content/user-info.jsp" %>
	
	<div style="text-align: center; margin-top: 15px">
		<a class="btn btn-default" href="changeinfo">Change user name or password</a>
	</div>
	
</div>


</body>
</html>