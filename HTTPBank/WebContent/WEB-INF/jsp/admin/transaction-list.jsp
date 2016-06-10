<%@ include file="../head.jsp" %>

<style>

#transaction-list-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">
	
	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="transaction-list" class="container container-lg col-sm-9">

	<%@ include file="../content/transaction-list.jsp" %>
	
	</div>
	
</div>


</body>
</html>