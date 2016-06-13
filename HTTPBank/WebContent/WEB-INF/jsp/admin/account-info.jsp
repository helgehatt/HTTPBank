<%@ include file="../content/head.jsp" %>

<style>

#account-info-item a {
	background-color:#e6e6e6;
}

.panel-body {
	padding: 15px;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="account-info" class="container container-sm col-sm-9">
	
	<%@ include file="../content/account-info.jsp" %>
	
	</div>
	
	
</div>


</body>
</html>