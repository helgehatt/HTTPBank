<%@ include file="../head.jsp" %>

<style>

#account-info {
	max-width: 800px;
}

#account-info .table {
	margin: 0;
}

#account-info-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="account-info" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
        <table class="table">
          <thead>
            <tr><th>Account information</th><th></th></tr>
          </thead>
          <tbody>
            <tr><td>Balance</td><td>${account.balance}</td></tr>
            <tr><td>Currency</td><td>${account.currency}</td></tr>
            <tr><td>Account type</td><td>${account.type}</td></tr>
            <tr><td>Account number</td><td>${account.number}</td></tr>
           	<tr><td>IBAN</td><td>${account.iban}</td></tr>
            <tr><td>Interest rate</td><td>${account.interest}</td></tr>
          </tbody>
        </table>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>