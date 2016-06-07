<%@ include file="../head.jsp" %>

<script src="${pageContext.request.contextPath}/js/transfer-int-script.js"></script>

<style>

#transfer-int {
	max-width: 800px;
}

#transfer-int form {
	padding: 20px;
}

</style>

<%@ include file="navbar.jsp" %>

<c:set var="errors" scope="page" value="${sessionScope.errors}"/>
<c:remove var="errors" scope="session"/>

<c:set var="currencies" value="${['DKK','NOK','EUR']}" scope="page" />

<div id="transfer-int" class="container">
  <h3>Transfer</h3>
  <p>Insert required information to make a new transfer.</p>
  <ul class="nav nav-pills">
    <li><a href="transfer">Domestic</a></li>
    <li class="active"><a href="#">International</a></li>
  </ul>
	<%@ include file="../transfer-int-form.jsp" %>
</div>


</body>
</html>