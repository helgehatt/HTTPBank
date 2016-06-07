<%@ include file="../head.jsp" %>

<style>


#archive {
	max-width: 1200px;
}

#archive .panel-body {
	padding: 0;
}

#archive .table {
	margin: 0;
}

#archive-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">
	
	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="archive" class="container col-sm-9">
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
		      	<c:forEach var="transaction" items="${account.archive}">
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