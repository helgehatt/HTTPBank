<%@ include file="../content/head.jsp" %>

<style>

#archive-item {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="error" scope="page" value="${sessionScope.error}"/>
<c:remove var="error" scope="session"/>

<div class="container-fluid">

	<%@ include file="side-menu.jsp" %>
	
	<div id="archive" class="container container-lg col-sm-9">
	
	<%@ include file="../content/archive.jsp" %>
	
	</div>
	
	
</div>


</body>
</html>