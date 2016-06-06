<%@ include file="../head.jsp" %>

<style>

#account-list {
	max-width: 1200px;
}

#account-list-item a {
	background-color:#e6e6e6;
}

@media (min-width: 768px) {
	#text-left {
		float: left;
		text-align: left;
		width: 40%;
	}
	
	#text-center {
		display: inline-block;
		text-align: center;
		width: 20%;
	}
	
	#text-right {
		float: right;
		text-align: right;
		width: 40%;
	}
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
		     				<button class="btn btn-default" name="id" value="${account.id}">
		     					<h4 id="text-left">${account.type}</h4>
		     					<h4 id="text-center">${account.number}</h4>
		     					<h4 id="text-right">${account.balance} ${account.currency}</h4>
		     				</button>
		     			</form>
		     		</li>
					</c:forEach>
					<li class="list-group-item">
	     			<form action="newaccount" method="get">
	     				<button class="btn btn-default">
	     					<h4>New account</h4>
	     				</button>
	     			</form>
					</li>
				</ul>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>