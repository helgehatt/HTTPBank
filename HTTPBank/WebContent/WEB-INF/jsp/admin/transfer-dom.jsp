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
	      <form class="form-horizontal" role="form" action="doTransfer" method="post">
	        <div class="form-group">
	          <label class="control-label col-sm-3" for="from">From:</label>
	          <div class="col-sm-9">
	            <input name="from" type="text" class="form-control" id="from">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-3" for="to">To:</label>
	          <div class="col-sm-9">          
	            <input name="to" type="text" class="form-control" id="to">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-3" for="amount">Amount:</label>
	          <div class="col-sm-9">          
	            <input name="amount" type="text" class="form-control" id="amount">
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-3" for="currency">Currency:</label>
	          <div class="col-sm-9">          
	            <input name="currency" type="text" class="form-control" id="currency" disabled>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-3" for="message">Message:</label>
	          <div class="col-sm-9">
	            <textarea name="message" class="form-control" rows="5" id="message" placeholder="Optional"></textarea>
	          </div>
	        </div>
	         <button name="transfer" class="btn btn-default btn-submit" value="admin-dom">Submit</button>
	      </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>