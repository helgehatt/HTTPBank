<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="../content/head.jsp" %>

<style>

#search-label {
	color: #555;
	font-size: 18px;
	font-weight: 500;
	margin-top: 6px
}

@media (max-width: 767px) {
	#search-label {
		display: block;
		text-align: center;
	}
	
	#div-newuser {
		border-bottom: 1px solid #ddd;
	}
}

@media (min-width: 768px) {
	#div-newuser {
		border-right: 1px solid #ddd;
	}
}

</style>

<%@ include file="navbar.jsp" %>

<div id="user-list" class="container container-md">
	<div class="panel panel-default button-panel">
	  <div class="panel-body">
	  	<ul class="list-group">
   			<li class="list-group-item">
   				<div class="container-fluid">
   				<div id="div-newuser" class="col-sm-6" style="padding: 0">
	     			<form action="newuser" method="get">
	     				<button class="btn btn-default">
		    				<h4>New user</h4>
	     				</button>
	     			</form>   				
   				</div>
   				<div id="div-search" class="col-sm-6" style="padding: 0">
	     			<form action="findUsers" method="post">
	     				<div class="form-group">
    						<label class="control-label col-sm-3" id="search-label" for="search-input">Search:</label>
    						<div class="col-sm-9" style="padding-left: 15px">
     							<input name="search" class="form-control" id="search-input" class="form-control">
    						</div>	     				
	     				</div>	     				
	     			</form>   				
   				</div>
   				</div>
     		</li>
	    	<c:forEach var="user" items="${users}">
	   			<li class="list-group-item">
	     			<form action="getUser" method="post">
	     				<button class="btn btn-default" name="id" value="${user.id}">
		    				<h4 class="alignright">${user.cpr}</h4>
		    				<h4 class="alignleft">${user.name}</h4>
	     				</button>
	     			</form>
	     		</li>
	    	</c:forEach>
	    	<c:if test="${moreUsers == true}">
	   			<li class="list-group-item">
	     			<form action="getMoreUsers" method="post">
	     				<button class="btn btn-default" name="id" value="${fn:length(users)}">
		    				<h4>...</h4>
	     				</button>
	     			</form>
	     		</li>
     		</c:if>
	   	</ul>
	  </div>
	</div>
</div>

</body>
</html>