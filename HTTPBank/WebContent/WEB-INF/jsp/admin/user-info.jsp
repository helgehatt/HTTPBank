<%@ include file="../head.jsp" %>

<style>

#user-info {
	max-width: 600px;
}

#user-info .table {
	margin: 0;
}

#user-info-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="user"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="user-info" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
        <table class="table">
          <thead>
            <tr><th colspan="2">User information</th></tr>
          </thead>
          <tbody>
            <tr><td>CPR</td><td>${user.cpr}</td></tr>
            <tr><td>Name</td><td>${user.name}</td></tr>
            <tr><td>Institute</td><td>${user.institute}</td></tr>
           	<tr><td>Consultant</td><td>${user.consultant}</td></tr>
          </tbody>
        </table>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>