<%@ include file="../head.jsp" %>

<script src="${pageContext.request.contextPath}/js/transfer-int-script.js"></script>

<script type="text/javascript">
function deposit(){
	$('#account-label').text('To:');
	$('#change-label').text('Deposited:');
	$('button').val('deposit');
}

function withdrawal(){
	$('#account-label').text('From:');
	$('#change-label').text('Withdrawn:');
	$('button').val('withdrawal');
}

$(function(){
	$('#deposit-pill').click(function(){
		deposit();
	});
	
	$('#withdrawal-pill').click(function(){
		withdrawal();
	});
});
</script>

<style>

#deposit-withdrawal {
	max-width: 600px;
}

#deposit-withdrawal .panel {
	border: none;
}

#deposit-withdrawal .panel-body {
	padding-top: 0;
}

#deposit-withdrawal .nav-pills {
	padding: 20px;
}

#deposit-withdrawal li a {
	border: solid 1px;
}

#deposit-withdrawal-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<%@ include file="side-menu.jsp" %>
	
	<div id="deposit-withdrawal" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
			  <ul class="nav nav-pills">
			    <li class="active" id="deposit-pill"><a data-toggle="pill">Deposit</a></li>
			    <li><a data-toggle="pill" id="withdrawal-pill">Withdrawal</a></li>
			  </ul>
			  <form class="form-horizontal" role="form" action="doDepositWithdrawal" method="post">
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="from" id="account-label">To:</label>
				    <div class="col-sm-5">
				    	<select name="from" class="form-control" id="from">
				    		<c:forEach var="account" items="${user.accounts}">
				   				<option value="${account.id}" data-currency="${account.currency}" data-balance="${account.balance}">${account.number}</option>
				   			</c:forEach>
				  		</select>
						</div>
						<div class="col-sm-4">
							<input class="form-control" id="balance" value="${user.accounts[0].balance} ${user.accounts[0].currency}" readonly>
						</div>
					</div>
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="amount">Amount:</label>
				    <div class="col-sm-6">          
				      <input name="amount" type="text" class="form-control" id="amount">
							<span class="error">${pageScope.errors.amount}</span>
				    </div>
				    <div class="col-sm-3">
				    	<select name="to-currency" class="form-control" id="to-currency">
				    		<c:forEach var="currency" items="${currencies}">
				    			<option value="${currency}">${currency}</option>
				    		</c:forEach>
				    	</select>	          
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="change" id="change-label">Deposited:</label>
				    <div class="col-sm-6">          
				      <input name="change" type="text" class="form-control" id="change" readonly>
				    </div>
				    <div class="col-sm-3">
				      <input name="from-currency" type="text" class="form-control" id="from-currency" value="${user.accounts[0].currency}" readonly>	          	
				    </div>
				  </div>
			    <button name="type" value="deposit" type="submit" class="btn btn-default btn-submit">Submit</button>
			  </form>
			</div>
		</div>
	</div>
	
</div>


</body>
</html>