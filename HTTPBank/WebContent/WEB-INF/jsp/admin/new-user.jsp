<%@ page import="ibm.resource.User" %>
<%@ include file="../head.jsp" %>

<style>

#new-user {
	padding: 20px;
}

#new-user .panel {
	border: none;
}

</style>

<%@ include file="navbar.jsp" %>
	
<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div id="new-user" class="container container-md">
		<div class="panel panel-default">
	  <div class="panel-body">
			<form class="form-horizontal" role="form" action="newUser" method="post">
			  <div class="form-group">
			    <label class="control-label col-sm-3" for="username">User name:</label>
			    <div class="col-sm-9">          
			      <input name="username" type="text" class="form-control" id="username">
   					<span class="error">${pageScope.errors.username}</span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="control-label col-sm-3" for="cpr">CPR:</label>
			    <div class="col-sm-9">          
			      <input name="cpr" type="text" class="form-control" id="cpr">
   					<span class="error">${pageScope.errors.cpr}</span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="control-label col-sm-3" for="name">Name:</label>
			    <div class="col-sm-9">          
			      <input name="name" type="text" class="form-control" id="name">
   					<span class="error">${pageScope.errors.name}</span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="control-label col-sm-3" for="institute">Institute:</label>
			    <div class="col-sm-9">          
			      <input name="institute" type="text" class="form-control" id="institute">
   					<span class="error">${pageScope.errors.institute}</span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="control-label col-sm-3" for="consultant">Consultant:</label>
			    <div class="col-sm-9">          
			      <input name="consultant" type="text" class="form-control" id="consultant">
   					<span class="error">${pageScope.errors.consultant}</span>
			    </div>
			  </div>
			  <button name="type" value="new" type="submit" class="btn btn-default btn-submit">Submit</button>
			</form>
	  </div>
	</div>
</div>

</body>
</html>