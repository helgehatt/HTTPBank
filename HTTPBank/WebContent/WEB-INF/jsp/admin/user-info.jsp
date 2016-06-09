<%@ include file="../head.jsp" %>

<style>

#user-info {
	max-width: 600px;
}

#user-info-item a {
	background-color:#e6e6e6;
}

.panel-body {
	padding: 15px;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="user"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="user-info" class="container col-sm-9">
	
	<%@ include file="../content/user-info.jsp" %>

	</div>
	
	
</div>


</body>
</html>