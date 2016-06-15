<form class="form-horizontal" role="form" action="doTransfer" method="post">
  <div class="form-group">
    <label class="control-label col-sm-3" for="from">From:</label>
    <div class="col-sm-5">
    	<select name="from-number" class="form-control" id="from">
    		<c:forEach var="account" items="${user.accounts}">
   				<option value="${account.iban}" data-id="${account.id}" data-balance="${account.balance}" data-currency="${account.currency}">${account.number}</option>
   			</c:forEach>
  		</select>
  		<input name="from-id" type="hidden" value="${user.accounts[0].id}" id="id">
		</div>
		<div class="col-sm-4">
			<input class="form-control" id="balance" value="${user.accounts[0].balance} ${user.accounts[0].currency}" readonly>
		</div>
	</div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="to">IBAN:</label>
    <div class="col-sm-9">          
      <input name="to-number" type="text" class="form-control" id="to">
			<span class="error">${pageScope.errors.toNumber}</span>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="bic">BIC:</label>
    <div class="col-sm-9">         
      <input name="to-bic" type="text" class="form-control" id="bic">
			<span class="error">${pageScope.errors.toBic}</span>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="amount">Amount:</label>
    <div class="col-sm-6">          
      <input name="input-amount" type="text" class="form-control" id="amount">
			<span class="error">${pageScope.errors.inputAmount}</span>
    </div>
    <div class="col-sm-3">
    	<select name="input-currency" class="form-control" id="to-currency">
    		<c:forEach var="currency" items="${currencies}">
    			<option value="${currency}">${currency}</option>
    		</c:forEach>
    	</select>	          
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="change">Withdrawn:</label>
    <div class="col-sm-6">          
      <input name="withdrawn-amount" type="text" class="form-control" id="change" readonly>
    </div>
    <div class="col-sm-3">
      <input name="withdrawn-currency" type="text" class="form-control" id="from-currency" value="${user.accounts[0].currency}" readonly>	          	
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="message">Message:</label>
    <div class="col-sm-9">
      <textarea name="message" class="form-control" rows="5" id="message" placeholder="Optional" maxlength="200"></textarea>
    </div>
  </div>
  <button name="international" class="btn btn-default btn-submit" id="submit">Submit</button>
</form>