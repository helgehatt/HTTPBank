<%@ include file="../head.jsp" %>

<style>

#transaction-list {
	max-width: 800px;
}

#transaction-list .panel-body {
	padding: 0;
}

#transaction-list .table {
	margin: 0;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="transaction-list" class="container">
  <h3>Transactions</h3>
  <p>Current week's transactions for your account ${account.number}.</p>
  
	<%@ include file="../content/transaction-list.jsp" %>
	
</div>


</body>
</html>