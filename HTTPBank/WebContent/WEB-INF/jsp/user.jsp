<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>HTTP Bank</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
	</head>
  <body>

    <nav id="nav" class="navbar navbar-default">
      <div class="container-fluid">
        <div class="navbar-header">
          <img src="${pageContext.request.contextPath}/images/LogoPlain.jpg" alt="HTTP">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a role="button" class="btn btn-default" data-toggle="pill" href="#accounts">Accounts</a></li>
            <li><a role="button" class="btn btn-default" data-toggle="pill" href="#transfer">Transfer</a></li>
            <li><a role="button" class="btn btn-default" data-toggle="pill" href="#transactions">Transactions</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a role="button" class="btn btn-default"><span class="glyphicon glyphicon-envelope"></span> Inbox</a></li>
            <li><a role="button" class="btn btn-default" href="${pageContext.request.contextPath}/login"><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div id="main-content" class="tab-content">
      <div id="accounts" class="tab-pane fade in active">
        <h3>Accounts</h3>
        <p>Your accessible accounts are shown below.</p>
        <div id="accordion" class="panel-group">
	        <c:forEach var="account" items="${sessionScope.user.accounts}" varStatus="counter">
	          <div class="panel panel-default">
	            <div class="panel-heading">
	              <h4 class="panel-title">
	                <a class="accordion-toggle "data-toggle="collapse" data-parent="#accordion" href="#collapse${counter.count}">${account.type}</a>
	                <p>${account.balance} ${account.currency}</p>
	              </h4>
	            </div>
	            <div id="collapse${counter.count}" class="panel-collapse collapse <c:if test="${counter.count==1}">in</c:if>">
	              <div class="panel-body">
	                <table class="table">
	                  <thead>
	                    <tr><th>Account information</th><th></th></tr>
	                  </thead>
	                  <tbody>
	                    <tr><td>Bank</td><td>${account.bank}</td></tr>
	                    <tr><td>Account number</td><td>${account.number}</td></tr>
	                    <tr><td>Interest rate</td><td>${account.interest}</td></tr>
	                    <c:if test="${account.iban!=null}">
	                    	<tr><td>IBAN</td><td>${account.iban}</td></tr>
	                    	<tr><td>BIC</td><td>${account.bic}</td></tr>
	                    </c:if>
	                  </tbody>
	                </table>
	              </div>
	            </div>
	          </div>
	        </c:forEach>
        </div>
      </div>
      <div id="transfer" class="tab-pane fade">
        <h3>Transfer</h3>
        <p>Insert required information to make a new transfer.</p>
        <ul class="nav nav-pills">
          <li class="active"><a data-toggle="pill" href="#domestic">Domestic</a></li>
          <li><a data-toggle="pill" href="#international">International</a></li>
        </ul>
        <div class="tab-content">
          <div id="domestic" class="tab-pane fade in active">
            <form class="form-horizontal" role="form">
              <div class="form-group">
                <label class="control-label col-sm-3" for="d-from">From account:</label>
                <div class="col-sm-9">
                  <input type="text" class="form-control" id="d-from">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="d-to">To account:</label>
                <div class="col-sm-9">          
                  <input type="text" class="form-control" id="d-to">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="d-amount">Amount:</label>
                <div class="col-sm-7">          
                  <input type="text" class="form-control" id="d-amount">
                </div>
                <div class="col-sm-2">          
                  <input type="text" class="form-control" placeholder="DKK" disabled>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="d-message">Message:</label>
                <div class="col-sm-9">
                  <textarea class="form-control" rows="5" id="d-message" placeholder="Optional"></textarea>
                </div>
              </div>
              <button type="submit" class="btn btn-default btn-submit">Submit</button>
            </form>
          </div>
          <div id="international" class="tab-pane fade">
            <form class="form-horizontal" role="form">
              <div class="form-group">
                <label class="control-label col-sm-3" for="i-from">From account:</label>
                <div class="col-sm-9">
                  <input type="text" class="form-control" id="i-from">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="i-iban">IBAN:</label>
                <div class="col-sm-9">          
                  <input type="text" class="form-control" id="i-iban">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="i-bic">BIC:</label>
                <div class="col-sm-9">         
                  <input type="text" class="form-control" id="i-bic">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="i-amount">Amount:</label>
                <div class="col-sm-7">          
                  <input type="text" class="form-control" id="i-amount">
                </div>
                <div class="col-sm-2">          
                  <input type="text" class="form-control" id="i-currency" placeholder="DKK">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3" for="i-message">Message:</label>
                <div class="col-sm-9">
                  <textarea class="form-control" rows="5" id="i-message" placeholder="Optional"></textarea>
                </div>
              </div>
              <button type="submit" class="btn btn-default btn-submit">Submit</button>
            </form>
          </div>
        </div>
      </div>
      <div id="transactions" class="tab-pane fade">
        <h3>Transactions</h3>
        <p>Current week's transactions.</p>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Date</th>
              <th>Description</th>
              <th class="text-right">Amount</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach var="account" items="${sessionScope.user.accounts}">
          		<c:forEach var="transaction" items="${account.transactions}">
            		<tr><td>${transaction.date}</td><td>${transaction.description}</td><td class="text-right">${transaction.amount}</td></tr>
            	</c:forEach>
          	</c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </body>
</html>