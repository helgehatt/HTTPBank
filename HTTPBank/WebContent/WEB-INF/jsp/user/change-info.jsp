<%@ include file="../head.jsp" %>

<style>

#change-info {
	max-width: 800px;
}

#change-info form {
	padding: 20px;
}

</style>

<%@ include file="navbar.jsp" %>
	
<div id="change-info" class="container">
  <h3>Change user name or password</h3>
  <p>Enter your password to make any changes.</p>
	<form class="form-horizontal" role="form" action="changeInfo" method="post">
		<div class="form-group">
			<label class="control-label col-sm-3" for="to">Current password:</label>
  		<div class="col-sm-9">          
    		<input name="to" type="text" class="form-control" id="to" placeholder="Required">
				<span class="error">${pageScope.errors.to}</span>
  		</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="to">New user name:</label>
  		<div class="col-sm-9">          
    		<input name="to" type="text" class="form-control" id="to">
				<span class="error">${pageScope.errors.to}</span>
  		</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="to">New password:</label>
  		<div class="col-sm-9">          
    		<input name="to" type="text" class="form-control" id="to">
				<span class="error">${pageScope.errors.to}</span>
  		</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="to">Repeat password:</label>
  		<div class="col-sm-9">          
    		<input name="to" type="text" class="form-control" id="to">
				<span class="error">${pageScope.errors.to}</span>
  		</div>
		</div>
	</form>
  
  
	
</div>


</body>
</html>