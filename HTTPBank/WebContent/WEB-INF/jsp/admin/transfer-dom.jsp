<%@ include file="../head.jsp" %>

<style>

#transfer-dom {
	max-width: 800px;
}

#transfer-dom .panel {
	border: none;
}

#transfer-dom-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="transfer"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="transfer-dom" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
	      <form class="form-horizontal" role="form" action="transfer" method="post">
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="d-from">From:</label>
	          <div class="col-sm-8">
	            <input name="from" type="text" class="form-control" id="d-from">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="d-to">To:</label>
	          <div class="col-sm-8">          
	            <input name="to" type="text" class="form-control" id="d-to">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="d-amount">Amount:</label>
	          <div class="col-sm-8">          
	            <input name="amount" type="text" class="form-control" id="d-amount">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="d-currency">Currency:</label>
	          <div class="col-sm-8">          
	            <input name="currency" type="text" class="form-control" id="d-currency" disabled>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4" for="d-message">Message:</label>
	          <div class="col-sm-8">
	            <textarea name="message" class="form-control" rows="5" id="d-message" placeholder="Optional"></textarea>
	          </div>
	        </div>
	         <button class="btn btn-default btn-submit">Submit</button>
	      </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>