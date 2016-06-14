<%@ include file="../content/head.jsp" %>

<style>

#delete-user {
	text-align: center;
}

#delete-user form {
	padding-top: 20px;
}

#delete-user button {
	margin-right: 10px;
}

#delete-user .panel-body {
	padding: 15px;
}

#delete-user-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="user"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="delete-user" class="container container-sm col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
				Are you sure you want to delete ${user.username}?
		    <form class="form-horizontal" role="form" action="deleteUser" method="post">
		    	<button class="btn btn-default" type="submit">Yes</button>
		    	<a class="btn btn-default" type="button" href="userinfo">No</a>
		    </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>