<%@ include file="../head.jsp" %>

<style>

#transaction-list-nav {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="transaction-list" class="container container-md">
  <h3>Transactions</h3>
  <p>Current week's transactions for your account ${account.number}.</p>
  
	<%@ include file="../content/transaction-list.jsp" %>
	
</div>


</body>
</html>