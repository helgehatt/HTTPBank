<%@ include file="../head.jsp" %>

<style>

#deposit {
	max-width: 600px;
}

#deposit .panel {
	border: none;
}

#deposit-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="deposit" class="container col-sm-9">
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
			      <label class="control-label col-sm-3" for="deposited">Deposited:</label>
			      <div class="col-sm-9">          
			        <input name="deposited" type="text" class="form-control" id="deposited" disabled>
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