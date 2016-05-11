<%@ include file="../head.jsp" %>

<style>

#transfer-int {
	max-width: 800px;
}

#transfer-int .panel {
	border: none;
}

#transfer-int-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="transfer"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="transfer-int" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
	      <form class="form-horizontal" role="form" action="transfer" method="post">
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="i-from">From:</label>
	          <div class="col-sm-8">
	            <input name="from" type="text" class="form-control" id="i-from">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="i-to">To:</label>
	          <div class="col-sm-8">          
	            <input name="to" type="text" class="form-control" id="i-to" placeholder="IBAN">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="i-bic">BIC:</label>
	          <div class="col-sm-8">         
	            <input name="bic" type="text" class="form-control" id="i-bic">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="i-amount">Amount:</label>
	          <div class="col-sm-8">          
	            <input name="amount" type="text" class="form-control" id="i-amount">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="i-currency">Currency:</label>
	          <div class="col-sm-8">          
	            <input name="currency" type="text" class="form-control" id="i-currency">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="i-message">Message:</label>
	          <div class="col-sm-8">
	            <textarea name="message" class="form-control" rows="5" id="i-message" placeholder="Optional"></textarea>
	          </div>
	        </div>
	         <button type="submit" class="btn btn-default btn-submit">Submit</button>
	      </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>