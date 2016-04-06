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
          <ul class="nav navbar-nav navbar-right">
            <li><a role="button" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Search</a></li>
            <li><a role="button" class="btn btn-default" href="${pageContext.request.contextPath}/login"><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div id="user-list" class="list-group col-sm-2">
        <div class="list-group-item tab-header">
	        <p>Users</p>
	      	<a href="#new-user" class="account-glyphicon" data-toggle="pill"><span class="glyphicon glyphicon-user" title="Add User"></span></a>
	      	<a href="#new-account" class="account-glyphicon" data-toggle="pill"><span class="glyphicon glyphicon-piggy-bank" title="Add Account"></span></a>
        </div>
        <c:forEach var="user" items="${sessionScope.users}" varStatus="counter">
        	<a href="#account-list${counter.count}" class="list-group-item" data-toggle="pill">${user.name}</a>
        </c:forEach>
      </div>
      <div id="left-tab" class="tab-content col-sm-5">
        <div id="new-user" class="panel panel-default tab-pane fade">
          <div class="panel-heading">
            <h4 class="panel-title">Add User</h4>
          </div>
          <div class="panel-body">
	          <form class="form-horizontal" role="form">
	            <div class="form-group">
	              <label class="control-label col-sm-3" for="name">Name:</label>
	              <div class="col-sm-9">          
	                <input type="text" class="form-control" id="name">
	              </div>
	            </div>
             	<button type="submit" class="btn btn-default btn-submit">Submit</button>
	          </form>
          </div>
        </div>
        <div id="new-account" class="panel panel-default tab-pane fade">
          <div class="panel-heading">
            <h4 class="panel-title">Add Account</h4>
          </div>
          <div class="panel-body">
            <form class="form-horizontal" role="form">
              <div class="form-group">
                <label class="control-label col-sm-4" for="type">Type:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="type">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="bank">Bank:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="bank">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="number">Number:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="number">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="currency">Currency:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="currency">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="interest">Interest:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="interest">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="balance">Balance:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="balance" placeholder="Optional">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="iban">IBAN:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="iban" placeholder="Optional">
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-4" for="bic">BIC:</label>
                <div class="col-sm-8">          
                  <input type="text" class="form-control" id="bic" placeholder="Optional">
                </div>
              </div>
              <button type="submit" class="btn btn-default btn-submit">Submit</button>
            </form>
          </div>
        </div>
        <c:forEach var="user" items="${sessionScope.users}" varStatus="userCounter">
	        <div id="account-list${userCounter.count}" class="panel-group tab-pane fade">
	        	<div class="panel panel-default tab-header">
	        		<p>Accounts</p>
       				<a class="account-glyphicon" href="#transaction-list${userCounter.count}" data-toggle="pill"><span class="glyphicon glyphicon-stats" title="Transactions"></span></a>
       				<a class="account-glyphicon" href="#transfer" data-toggle="pill"><span class="glyphicon glyphicon-transfer" title="Transfer"></span></a>
       				<a class="account-glyphicon" href="#deposit" data-toggle="pill"><span class="glyphicon glyphicon-usd" title="Deposit/Withdrawal"></span></a>
	        	</div>
	        	<c:forEach var="account" items="${user.accounts}" varStatus="accountCounter">
		          <div class="panel panel-default">
		            <div class="panel-heading">
		              <h4 class="panel-title">
		                <a class="accordion-toggle "data-toggle="collapse" data-parent="#account-list${userCounter.count}" href="#collapse${userCounter.count}${accountCounter.count}">${account.type}</a>
		                <p>${account.balance} ${account.currency}</p>
		              </h4>
		            </div>
		            <div id="collapse${userCounter.count}${accountCounter.count}" class="panel-collapse collapse <c:if test="${accountCounter.count==1}">in</c:if>">
		              <div class="panel-body">
		                <table class="table">
		                  <thead>
		                    <tr>
		                      <th>Account information</th>
		                      <th class="text-right">
		                        <a class="account-glyphicon" href="#"><span class="glyphicon glyphicon-pencil" title="Edit"></span></a>
		                        <a class="account-glyphicon" href="#"><span class="glyphicon glyphicon-remove" title="Remove"></span></a>
		                      </th>
		                    </tr>
		                  </thead>
		                  <tbody>
		                    <tr><td>Bank</td><td>${account.bank}</td></tr>
		                    <tr><td>Account nr.</td><td>${account.number}</td></tr>
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
        </c:forEach>
      </div>
      <div id="right-tab" class="tab-content col-sm-5">
      	<div id="deposit" class="panel panel-default tab-pane fade">
      		<table class="table">
	          <thead>
	          	<tr>
	          		<th>Deposit</th>
                <th class="text-right">
          				<a class="account-glyphicon" href="#withdrawal" data-toggle="pill"><span class="glyphicon glyphicon-repeat" title="Withdrawal"></span></a>
                </th>
	          	</tr>
	          </thead>
          </table>
	        <form class="form-horizontal" role="form">
	          <div class="form-group">
	            <label class="control-label col-sm-3" for="dep-account">To:</label>
	            <div class="col-sm-9">
	              <input type="text" class="form-control" id="dep-account" placeholder="Account number">
	            </div>
	          </div>
	          <div class="form-group">
	            <label class="control-label col-sm-3" for="dep-amount">Amount:</label>
	            <div class="col-sm-9">          
	              <input type="text" class="form-control" id="dep-amount">
	            </div>
	          </div>
	          <button type="submit" class="btn btn-default btn-submit">Submit</button>
	        </form>
      	</div>
      	<div id="withdrawal" class="panel panel-default tab-pane fade">
      		<table class="table">
	          <thead>
	          	<tr>
	          		<th>Withdrawal</th>
                <th class="text-right">
          				<a class="account-glyphicon" href="#deposit" data-toggle="pill"><span class="glyphicon glyphicon-repeat" title="Deposit"></span></a>
                </th>
	          	</tr>
	          </thead>
          </table>
	        <form class="form-horizontal" role="form">
	          <div class="form-group">
	            <label class="control-label col-sm-3" for="wit-account">From:</label>
	            <div class="col-sm-9">
	              <input type="text" class="form-control" id="wit-account" placeholder="Account number">
	            </div>
	          </div>
	          <div class="form-group">
	            <label class="control-label col-sm-3" for="wit-amount">Amount:</label>
	            <div class="col-sm-9">          
	              <input type="text" class="form-control" id="wit-amount">
	            </div>
	          </div>
	          <button type="submit" class="btn btn-default btn-submit">Submit</button>
	        </form>
			  </div>
      	<div id="transfer" class="panel panel-default tab-pane fade">
      		<table class="table">
	          <thead>
	          	<tr>
	          		<th>Transfer</th>
                <th class="text-right">
          				<a class="account-glyphicon" href="#domestic" data-toggle="pill"><span class="glyphicon glyphicon-home" title="Domestic"></span></a>
          				<a class="account-glyphicon" href="#international" data-toggle="pill"><span class="glyphicon glyphicon-globe" title="International"></span></a>
                </th>
	          	</tr>
	          </thead>
          </table>
	        <div class="tab-content">
	          <div id="domestic" class="tab-pane fade in active">
	            <form class="form-horizontal" role="form">
	              <div class="form-group">
	                <label class="control-label col-sm-3" for="d-from">From:</label>
	                <div class="col-sm-9">
	                  <input type="text" class="form-control" id="d-from" placeholder="Account number">
	                </div>
	              </div>
	              <div class="form-group">
	                <label class="control-label col-sm-3" for="d-to">To:</label>
	                <div class="col-sm-9">          
	                  <input type="text" class="form-control" id="d-to" placeholder="Account number">
	                </div>
	              </div>
	              <div class="form-group">
	                <label class="control-label col-sm-3" for="d-amount">Amount:</label>
	                <div class="col-sm-9">          
	                  <input type="text" class="form-control" id="d-amount">
	                </div>
	              </div>
	              <div class="form-group">
	                <label class="control-label col-sm-3" for="d-currency">Currency:</label>
	                <div class="col-sm-9">          
	                  <input type="text" class="form-control" id="d-currency" placeholder="DKK" disabled>
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
	                <label class="control-label col-sm-3" for="i-from">From:</label>
	                <div class="col-sm-9">
	                  <input type="text" class="form-control" id="i-from" placeholder="Account number">
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
	                <div class="col-sm-9">          
	                  <input type="text" class="form-control" id="i-amount">
	                </div>
	              </div>
	              <div class="form-group">
	                <label class="control-label col-sm-3" for="i-currency">Currency:</label>
	                <div class="col-sm-9">          
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
      	<c:forEach var="user" items="${sessionScope.users}" varStatus="userCounter">
	        <div id="transaction-list${userCounter.count}" class="panel panel-default tab-pane fade">
	          <table class="table table-striped">
	            <thead>
	              <tr>
	              	<th colspan="3">Transactions</th>
	              </tr>
	            </thead>
	            <tbody>
	    					<c:forEach var="account" items="${user.accounts}" varStatus="accountCounter">
	            		<c:forEach var="transaction" items="${account.transactions}">
	              		<tr><td>${transaction.date}</td><td>${transaction.description}</td><td class="text-right">${transaction.amount}</td></tr>
	              	</c:forEach>
	       				</c:forEach>
	            </tbody>
	          </table>
	        </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>