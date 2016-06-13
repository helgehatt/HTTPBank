<%@ include file="../content/head.jsp" %>

<style>

#archive-nav {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="error" scope="page" value="${sessionScope.error}"/>
<c:remove var="error" scope="session"/>

<div id="transaction-list" class="container container-md">
  <h3>Archive</h3>
  <p>History of transactions.</p>
  
	<%@ include file="../content/archive.jsp" %>
	
</div>


</body>
</html>