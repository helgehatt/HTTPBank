<%@ page import="ibm.resource.User" %>
<%@ include file="../head.jsp" %>

<style>

</style>

<%@ include file="navbar.jsp" %>

<div id="user-list" class="container container-md">
	<div class="panel panel-default button-panel">
	  <div class="panel-body">
	  	<ul class="list-group">
	    	<c:forEach var="user" items="${sessionScope.users}">
	   			<li class="list-group-item">
	     			<form action="getUser" method="post">
	     				<button class="btn btn-default" name="id" value="${user.id}">
		    				<h4 class="alignleft">${user.name}</h4>
		    				<h4 class="alignright">${user.cpr}</h4>
	     				</button>
	     			</form>
	     		</li>
	    	</c:forEach>
   			<li class="list-group-item">
     			<form action="newuser" method="get">
     				<button class="btn btn-default">
	    				<h4>New user</h4>
     				</button>
     			</form>
     		</li>
	   	</ul>
	  </div>
	</div>
</div>

</body>
</html>