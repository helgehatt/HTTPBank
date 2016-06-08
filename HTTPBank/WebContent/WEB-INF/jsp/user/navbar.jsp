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
        <span class="icon-bar"></span>
      </button>
    </div>
    <div id="navbar" class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li><a class="btn btn-default" href="accounts">Accounts</a></li>
        <c:if test="${account != null}">
        	<li><a class="btn btn-default" href="transactions">Transactions</a></li>
        	<li><a class="btn btn-default" href="accountinfo">Account info</a></li>        
        </c:if>
        <li><a class="btn btn-default" href="transfer">Transfer</a></li>
        <li><a class="btn btn-default" href="archive">Archive</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
	      <li class="dropdown">
	        <a class="dropdown-toggle btn btn-default" data-toggle="dropdown" href="#">Logged in as ${user.name}
	        <span class="caret"></span></a>
	        <ul class="dropdown-menu">
		        <li><a href="userinfo"><span class="glyphicon glyphicon-user"></span> Info</a></li>
		        <li><a href="inbox"><span class="glyphicon glyphicon-envelope"></span> Inbox</a></li>
		        <li><a href="../login"><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>
	        </ul>
	      </li>
      </ul>
    </div>
  </div>
</nav>