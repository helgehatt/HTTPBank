<%@ include file="../head.jsp" %>

<script src="${pageContext.request.contextPath}/js/transfer-dom-script.js"></script>

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
	<%@ include file="../content/transfer-dom.jsp" %>
</div>


</body>
</html>