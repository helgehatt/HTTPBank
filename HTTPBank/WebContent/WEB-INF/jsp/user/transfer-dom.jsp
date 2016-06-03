<%@ include file="../head.jsp" %>

<style>

#transfer-dom {
	max-width: 800px;
}

#transfer-dom form {
	padding: 20px;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<div id="transfer-dom" class="container">
  <h3>Transfer</h3>
  <p>Insert required information to make a new transfer.</p>
  <ul class="nav nav-pills">
    <li class="active"><a href="#">Domestic</a></li>
    <li><a href="international">International</a></li>
  </ul>
  <form class="form-horizontal" role="form" action="doTransfer" method="post">
    <div class="form-group">
      <label class="control-label col-sm-3" for="from">From account:</label>
      <div class="col-sm-9">
       	<select name="from" class="form-control" id="from">
       		<c:forEach var="account" items="${user.accounts}">
       			<option value="${account.id}">${account.number}</option>
       		</c:forEach>
       	</select>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-3" for="to">To account:</label>
      <div class="col-sm-9">          
        <input name="to" type="text" class="form-control" id="to">
				<span class="error">${pageScope.errors.to}</span>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-3" for="amount">Amount:</label>
      <div class="col-sm-7">          
        <input name="amount" type="text" class="form-control" id="amount">
				<span class="error">${pageScope.errors.amount}</span>
      </div>
      <div class="col-sm-2">          
        <input name="currency" type="text" class="form-control" value="DKK" readonly>
				<span class="error">${pageScope.errors.currency}</span>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-3" for="message">Message:</label>
      <div class="col-sm-9">
        <textarea name="message" class="form-control" rows="5" id="message" placeholder="Optional"></textarea>
      </div>
    </div>
    <button class="btn btn-default btn-submit">Submit</button>
  </form>
</div>


</body>
</html>