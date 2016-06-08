<form class="form-horizontal" role="form" action="doTransfer" method="post">
  <div class="form-group">
    <label class="control-label col-sm-3" for="from">From:</label>
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
		<label class="control-label col-sm-3" for="to">To:</label>
  	<div class="col-sm-9">          
    	<input name="to" type="text" class="form-control" id="to">
			<span class="error">${pageScope.errors.to}</span>
  	</div>
	</div>
	<div class="form-group">
  	<label class="control-label col-sm-3" for="amount">Amount:</label>
  	<div class="col-sm-6">          
    	<input name="amount" type="text" class="form-control" id="amount">
			<span class="error">${pageScope.errors.amount}</span>
  	</div>
  	<div class="col-sm-3">
    	<input name="currency" type="text" class="form-control" id="currency" value="${user.accounts[0].currency}" readonly>	          
  	</div>
	</div>
	<div class="form-group">
    <label class="control-label col-sm-3" for="message">Message:</label>
    <div class="col-sm-9">
      <textarea name="message" class="form-control" rows="5" id="message" placeholder="Optional"></textarea>
    </div>
  </div>
  <button class="btn btn-default btn-submit">Submit</button>
</form>