<%@ include file="../content/head.jsp" %>

<style>

#change-info form {
	margin-top: 20px;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>
	
<div id="change-info" class="container container-md">
  <h3>Change user name or password</h3>
  <p>Enter your password to make any changes.</p>
	<form class="form-horizontal" role="form" action="doChangeInfo" method="post">
		<div class="form-group">
			<label class="control-label col-sm-3" for="c-password">Current password:</label>
  		<div class="col-sm-9">          
    		<input name="c-password" type="password" class="form-control" id="c-password" placeholder="Required">
				<span class="error">${pageScope.errors.cpassword}</span>
  		</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="username">New user name:</label>
  		<div class="col-sm-9">          
    		<input name="username" type="text" class="form-control" id="username">
				<span class="error">${pageScope.errors.username}</span>
  		</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="n-password">New password:</label>
  		<div class="col-sm-9">          
    		<input name="n-password" type="password" class="form-control" id="n-password">
  		</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="r-password">Repeat password:</label>
  		<div class="col-sm-9">          
    		<input name="r-password" type="password" class="form-control" id="r-password">
				<span class="error">${pageScope.errors.rpassword}</span>
  		</div>
		</div>
	  <button class="btn btn-default btn-submit">Submit</button>
	</form>
  
  
	
</div>


</body>
</html>