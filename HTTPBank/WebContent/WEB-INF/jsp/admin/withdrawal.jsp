<%@ include file="../head.jsp" %>

<script src="${pageContext.request.contextPath}/js/conversion-script.js"></script>

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

<c:set var="currencies" value="${['DKK','NOK','EUR']}" scope="page" />

<div class="container-fluid">

	<c:set var="menu" scope="page" value="account"/>
	<%@ include file="side-menu.jsp" %>
	
	<div id="withdrawal" class="container col-sm-9">
		<div class="panel panel-default">
			<div class="panel-body">
			  <form class="form-horizontal" role="form">
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="amount">Amount:</label>
				    <div class="col-sm-6">          
				      <input name="amount" type="text" class="form-control" id="amount">
							<span class="error">${pageScope.errors.amount}</span>
				    </div>
				    <div class="col-sm-3">
				    	<select name="to-currency" class="form-control" id="to-currency">
				    		<c:forEach var="currency" items="${currencies}">
				    			<option value="${currency}">${currency}</option>
				    		</c:forEach>
				    	</select>	          
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-3" for="change">Withdrawn:</label>
				    <div class="col-sm-6">          
				      <input name="change" type="text" class="form-control" id="change" readonly>
				    </div>
				    <div class="col-sm-3">
				      <input name="from-currency" type="text" class="form-control" id="from-currency" value="${account.currency}" readonly>	          	
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