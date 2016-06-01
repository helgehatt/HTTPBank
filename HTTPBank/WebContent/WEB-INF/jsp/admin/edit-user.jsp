<%@ include file="../head.jsp" %>

<style>

#edit-user {
	max-width: 600px;
}

#edit-user .panel {
	border: none;
}

#edit-user-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>
	
<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="user"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="edit-user" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
		    <form class="form-horizontal" role="form" action="editUser" method="post">
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="cpr">CPR:</label>
		        <div class="col-sm-9">          
		          <input name="cpr" type="text" class="form-control" id="cpr" value="${user.cpr}">
    					<span class="error">${pageScope.errors.cpr}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="name">Name:</label>
		        <div class="col-sm-9">          
		          <input name="name" type="text" class="form-control" id="name" value="${user.name}">
    					<span class="error">${pageScope.errors.name}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="institute">Institute:</label>
		        <div class="col-sm-9">          
		          <input name="institute" type="text" class="form-control" id="institute" value="${user.institute}">
    					<span class="error">${pageScope.errors.institute}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="consultant">Consultant:</label>
		        <div class="col-sm-9">          
		          <input name="consultant" type="text" class="form-control" id="consultant" value="${user.consultant}">
    					<span class="error">${pageScope.errors.consultant}</span>
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