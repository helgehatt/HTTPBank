<%@ include file="../content/head.jsp" %>

<style>

#reset-password {
	text-align: center;
}

#reset-password form {
	padding-top: 20px;
}

#reset-password button {
	margin-right: 10px;
}

#reset-password .panel-body {
	padding: 15px;
}

#reset-password-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="user"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="reset-password" class="container container-sm col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
				Are you sure you want to reset ${user.username}'s password?
		    <form class="form-horizontal" role="form" action="resetPassword" method="post">
		    	<button class="btn btn-default" type="submit">Yes</button>
		    	<a class="btn btn-default" type="button" href="userinfo">No</a>
		    </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>