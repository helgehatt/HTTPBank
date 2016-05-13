<%@ include file="../head.jsp" %>

<style>

#withdrawal {
	max-width: 600px;
}

#withdrawal .panel {
	border: none;
}

#withdrawal-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="withdrawal" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
			  <form class="form-horizontal" role="form">
			    <div class="form-group">
			      <label class="control-label col-sm-3" for="amount">Amount:</label>
			      <div class="col-sm-9">          
			        <input name="amount" type="text" class="form-control" id="amount">
			      </div>
			    </div>
			    <div class="form-group">
			      <label class="control-label col-sm-3" for="currency">Currency:</label>
			      <div class="col-sm-9">          
			        <input name="currency" type="text" class="form-control" id="currency">
			      </div>
			    </div>
			    <div class="form-group">
			      <label class="control-label col-sm-3" for="withdrawn">Withdrawn:</label>
			      <div class="col-sm-9">          
			        <input name="withdrawn" type="text" class="form-control" id="withdrawn" disabled>
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