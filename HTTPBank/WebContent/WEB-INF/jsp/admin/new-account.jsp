<%@ include file="../content/head.jsp" %>

<style>

#new-account .panel {
	border: none;
}

</style>

<%@ include file="navbar.jsp" %>
	
<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div class="container-fluid">

	<%@ include file="side-menu.jsp" %>
	
	<div id="new-account" class="container container-md col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
		    <form class="form-horizontal" role="form" action="newAccount" method="post">
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="name">Name:</label>
		        <div class="col-sm-9">          
		          <input name="name" type="text" class="form-control" id="name" placeholder="Optional">
    					<span class="error">${pageScope.errors.name}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="type">Type:</label>
		        <div class="col-sm-9">          
		          <input name="type" type="text" class="form-control" id="type">
    					<span class="error">${pageScope.errors.type}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="number">Number:</label>
		        <div class="col-sm-9">          
		          <input name="number" type="text" class="form-control" id="number">
    					<span class="error">${pageScope.errors.number}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="iban">IBAN:</label>
		        <div class="col-sm-9">          
		          <input name="iban" type="text" class="form-control" id="iban">
    					<span class="error">${pageScope.errors.iban}</span>
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="currency">Currency:</label>
		        <div class="col-sm-9">
				    	<select name="currency" class="form-control" id="currency">
				    		<c:forEach var="currency" items="${currencies}">
				    			<option value="${currency}">${currency}</option>
				    		</c:forEach>
				    	</select>	
		        </div>
		      </div>
		      <div class="form-group">
		        <label class="control-label col-sm-3" for="interest">Interest:</label>
		        <div class="col-sm-9">          
		          <input name="interest" type="text" class="form-control" id="interest">
    					<span class="error">${pageScope.errors.interest}</span>
		        </div>
		      </div>
		      <button name="type" value="new" type="submit" class="btn btn-default btn-submit">Submit</button>
		    </form>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>