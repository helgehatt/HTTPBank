<form class="form-horizontal" role="form" action="doTransfer" method="post">
  <div class="form-group">
    <label class="control-label col-sm-3" for="from">From:</label>
    <div class="col-sm-9">
    	<select name="from" class="form-control" id="from">
    		<c:forEach var="account" items="${user.accounts}">
    			<option value="${account.id}" data-currency="${account.currency}">${account.number}</option>
    		</c:forEach>
    	</select>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="to">IBAN:</label>
    <div class="col-sm-9">          
      <input name="to" type="text" class="form-control" id="to">
			<span class="error">${pageScope.errors.to}</span>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="bic">BIC:</label>
    <div class="col-sm-9">         
      <input name="bic" type="text" class="form-control" id="bic">
			<span class="error">${pageScope.errors.bic}</span>
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
    <label class="control-label col-sm-3" for="change">Withdrawn:</label>
    <div class="col-sm-6">          
      <input name="change" type="text" class="form-control" id="change" readonly>
    </div>
    <div class="col-sm-3">
      <input name="from-currency" type="text" class="form-control" id="from-currency" readonly>	          	
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-3" for="message">Message:</label>
    <div class="col-sm-9">
      <textarea name="message" class="form-control" rows="5" id="message" placeholder="Optional"></textarea>
    </div>
  </div>
  <button name="international" class="btn btn-default btn-submit" id="submit">Submit</button>
</form>