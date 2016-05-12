<%@ include file="../head.jsp" %>

<style>

#transfer {
	max-width: 800px;
}

#transfer .tab-content {
  padding: 20px;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="transfer" class="container">
  <h3>Transfer</h3>
  <p>Insert required information to make a new transfer.</p>
  <ul class="nav nav-pills">
    <li class="active"><a data-toggle="pill" href="#domestic">Domestic</a></li>
    <li><a data-toggle="pill" href="#international">International</a></li>
  </ul>
  <div class="tab-content">
    <div id="domestic" class="tab-pane fade in active">
      <form class="form-horizontal" role="form" action="transfer" method="post">
        <div class="form-group">
          <label class="control-label col-sm-3" for="d-from">From account:</label>
          <div class="col-sm-9">
            <input name="from" type="text" class="form-control" id="d-from">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="d-to">To account:</label>
          <div class="col-sm-9">          
            <input name="to" type="text" class="form-control" id="d-to">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="d-amount">Amount:</label>
          <div class="col-sm-7">          
            <input name="amount" type="text" class="form-control" id="d-amount">
          </div>
          <div class="col-sm-2">          
            <input name="currency" type="text" class="form-control" placeholder="DKK" disabled>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="d-message">Message:</label>
          <div class="col-sm-9">
            <textarea name="message" class="form-control" rows="5" id="d-message" placeholder="Optional"></textarea>
          </div>
        </div>
        <button name="transfer" class="btn btn-default btn-submit" value="user-dom">Submit</button>
      </form>
    </div>
    <div id="international" class="tab-pane fade">
      <form class="form-horizontal" role="form" action="transfer" method="post">
        <div class="form-group">
          <label class="control-label col-sm-3" for="i-from">From account:</label>
          <div class="col-sm-9">
            <input name="from" type="text" class="form-control" id="i-from">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="i-iban">IBAN:</label>
          <div class="col-sm-9">          
            <input name="to" type="text" class="form-control" id="i-iban">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="i-bic">BIC:</label>
          <div class="col-sm-9">         
            <input name="bic" type="text" class="form-control" id="i-bic">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="i-amount">Amount:</label>
          <div class="col-sm-7">          
            <input name="amount" type="text" class="form-control" id="i-amount">
          </div>
          <div class="col-sm-2">          
            <input name="currency" type="text" class="form-control" id="i-currency" placeholder="DKK">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="i-message">Message:</label>
          <div class="col-sm-9">
            <textarea name="message" class="form-control" rows="5" id="i-message" placeholder="Optional"></textarea>
          </div>
        </div>
        <button name="transfer" class="btn btn-default btn-submit" value="user-int">Submit</button>
      </form>
    </div>
  </div>
</div>


</body>
</html>