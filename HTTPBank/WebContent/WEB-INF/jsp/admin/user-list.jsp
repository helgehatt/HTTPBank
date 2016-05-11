<%@ page import="ibm.resource.User" %>
<%@ include file="../head.jsp" %>

<style>

#user-list {
	max-width: 800px;
	padding: 20px;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="user-list" class="container">
	<div class="panel panel-default button-panel">
	  <div class="panel-body">
	  	<ul class="list-group">
	    	<c:forEach var="user" items="${sessionScope.users}">
	   			<li class="list-group-item">
	     			<form action="getUser" method="post">
	     				<button class="btn btn-default" name="cpr" value="${user.cpr}">
		    				<h4 class="alignleft">${user.name}</h4>
		    				<h4 class="alignright">${user.cpr}</h4>
	     				</button>
	     			</form>
	     		</li>
	    	</c:forEach>
	   	</ul>
	  </div>
	</div>
</div>

</body>
</html>