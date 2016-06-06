<%@ include file="../head.jsp" %>

<style>

#edit-account {
	max-width: 600px;
}

#edit-account .panel {
	border: none;
}

#edit-account-item a {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>
	
<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="edit-account" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
		    <form class="form-horizontal" role="form" action="editAccount" method="post">
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="name">Name:</label>
		        <div class="col-sm-9">          
		          <input name="name" type="text" class="form-control" id="name" value="${account.name}">
    					<span class="error">${pageScope.errors.name}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="type">Type:</label>
		        <div class="col-sm-9">          
		          <input name="type" type="text" class="form-control" id="type" value="${account.type}">
    					<span class="error">${pageScope.errors.type}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="number">Number:</label>
		        <div class="col-sm-9">          
		          <input name="number" type="text" class="form-control" id="number" value="${account.number}">
    					<span class="error">${pageScope.errors.number}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="iban">IBAN:</label>
		        <div class="col-sm-9">          
		          <input name="iban" type="text" class="form-control" id="iban" value="${account.iban}">
    					<span class="error">${pageScope.errors.iban}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="balance">Balance:</label>
		        <div class="col-sm-9">          
		          <input name="balance" type="text" class="form-control" id="balance" value="${account.balance}">
    					<span class="error">${pageScope.errors.balance}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="currency">Currency:</label>
		        <div class="col-sm-9">          
		          <input name="currency" type="text" class="form-control" id="currency" value="${account.currency}">
    					<span class="error">${pageScope.errors.currency}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="interest">Interest:</label>
		        <div class="col-sm-9">          
		          <input name="interest" type="text" class="form-control" id="interest" value="${account.interest}">
    					<span class="error">${pageScope.errors.interest}</span>
		        </div>
		      </div>
		      <button name="type" value="edit" type="submit" class="btn btn-default btn-submit">Submit</button>
		    </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>