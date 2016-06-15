</head>

<body>

<c:set var="exception" scope="page" value="${sessionScope.exception}"/>
<c:remove var="exception" scope="session"/>
<c:set var="confirmation" scope="page" value="${sessionScope.confirmation}"/>
<c:remove var="confirmation" scope="session"/>

<nav id="nav" class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
    	<a href="users">
     		<img src="${pageContext.request.contextPath}/images/LogoIcon.jpg" alt="HTTP">
    	</a>
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
        <li><a role="button" class="btn btn-default" href="../logout"><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>
      </ul>
    </div>
  </div>
</nav>
<c:if test="${pageScope.confirmation != null}">
	<div class="response-banner" style="background-color:rgb(17, 187, 44)">
		${pageScope.confirmation}
	</div>
</c:if>
<c:if test="${pageScope.exception != null}">
	<div class="response-banner" style="background-color:#DE2525">
		${pageScope.exception}
	</div>
</c:if>