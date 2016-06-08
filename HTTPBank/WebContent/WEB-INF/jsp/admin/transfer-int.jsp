<%@ include file="../head.jsp" %>

<script src="${pageContext.request.contextPath}/js/transfer-int-script.js"></script>

<style>

#transfer-int {
	max-width: 800px;
}

#transfer-int .panel {
	border: none;
}

#transfer-int-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="transfer"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="transfer-int" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
				<%@ include file="../content/transfer-int.jsp" %>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>