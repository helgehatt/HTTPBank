<%@ include file="../content/head.jsp" %>

<script src="${pageContext.request.contextPath}/js/transfer-dom-script.js"></script>

<style>

#transfer-dom .panel {
	border: none;
}

#transfer-dom-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="transfer"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="transfer-dom" class="container container-md col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
				<%@ include file="../content/transfer-dom.jsp" %>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>