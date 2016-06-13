<%@ include file="../content/head.jsp" %>

<script src="${pageContext.request.contextPath}/js/close-account-script.js"></script>

<style>

#close-account .panel {
	border: none;
}

#close-account-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="close-account" class="container container-sm col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
			  <ul class="nav nav-pills">
			    <li class="active" id="transfer-pill"><a data-toggle="pill">Transfer</a></li>
			    <li><a data-toggle="pill" id="withdrawal-pill">Withdrawal</a></li>
			  </ul>
			  <form class="form-horizontal" role="form" action="closeAccount" method="post">
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="from">From:</label>
				    <div class="col-sm-5">
				    	<input class="form-control" id="from" value="${account.number}" data-currency="${account.currency}" data-balance="${account.balance}" readonly>
						</div>
						<div class="col-sm-4">
							<input class="form-control" value="${account.balance} ${account.currency}" readonly>
						</div>
					</div>
				  <div class="form-group" id="to-form-group">
				    <label class="control-label col-sm-3" for="to" id="account-label">To:</label>
				    <div class="col-sm-5">
				    	<select name="to" class="form-control" id="to">
				    		<c:forEach var="account" items="${user.accounts}">
				    			<c:if test="${account.number != sessionScope.account.number}">
				   					<option value="${account.id}" data-currency="${account.currency}" data-balance="${account.balance}">${account.number}</option>
				   				</c:if>
				   			</c:forEach>
				  		</select>
						</div>
						<div class="col-sm-4">
							<input class="form-control" id="to-balance" readonly>
						</div>
					</div>
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="change" id="change-label">Deposited:</label>
				    <div class="col-sm-6">          
				      <input name="amount" type="text" class="form-control" id="change" readonly>
				    </div>
				    <div class="col-sm-3">
				      <input type="text" class="form-control" id="deposit-currency" readonly>	          	
				    	<select class="form-control" id="withdrawal-currency">
				    		<c:forEach var="currency" items="${currencies}">
				    			<option value="${currency}">${currency}</option>
				    		</c:forEach>
				    	</select>
				    </div>
				  </div>
			    <button name="type" value="transfer" type="submit" class="btn btn-default btn-submit">Submit</button>
			  </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>