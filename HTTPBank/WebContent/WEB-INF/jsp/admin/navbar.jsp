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
        <li><a role="button" class="btn btn-default" href="users">Users</a></li>
        <c:if test="${sessionScope.user != null}">
        	<li><a class="btn btn-default" style="pointer-events: none; cursor: default;">Currently viewing ${user.name} - ${user.cpr}</a></li>
        </c:if>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a role="button" class="btn btn-default" href="../login"><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>
      </ul>
    </div>
  </div>
</nav>