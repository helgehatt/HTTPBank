<%@ include file="../head.jsp" %>

<style>


#transaction-list {
	max-width: 1200px;
}

#transaction-list .panel-body {
	padding: 0;
}

#transaction-list .table {
	margin: 0;
}

#transaction-list-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">
	
	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="archive-list" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
		    <table class="table table-striped">
		      <thead>
		        <tr>
		          <th>Date</th>
		          <th>Description</th>
		          <th class="text-right">Amount (${account.currency})</th>
		        </tr>
		      </thead>
		      <tbody>
		      	<c:forEach var="transaction" items="${account.transactions}">
		        	<tr>
		        		<td>${transaction.date}</td>
		        		<td>${transaction.description}</td>
		        		<td class="text-right">${transaction.amount}</td>
		        	</tr>
		        </c:forEach>
		      </tbody>
		  	</table>
			</div>
		</div>
	</div>
	
</div>


</body>
</html>