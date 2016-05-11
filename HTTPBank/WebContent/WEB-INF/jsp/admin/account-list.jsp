<%@ include file="../head.jsp" %>

<style>

#account-list {
	max-width: 1200px;
}

#account-list-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<%@ include file="side-menu.jsp" %>
	
	<div id="account-list" class="container col-sm-9">
		<div class="panel panel-default button-panel">
			<div class="panel-body">
				<ul class="list-group">
					<c:forEach var="account" items="${user.accounts}">
		   			<li class="list-group-item">
		     			<form action="getAccount" method="post">
		     				<button class="btn btn-default" name="iban" value="${account.iban}">
		     					<h4 class="alignleft">${account.type}</h4>
		     					<h4 class="aligncenter">${account.number}</h4>
		     					<h4 class="alignright">${account.balance} ${account.currency}</h4>
		     				</button>
		     			</form>
		     		</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>