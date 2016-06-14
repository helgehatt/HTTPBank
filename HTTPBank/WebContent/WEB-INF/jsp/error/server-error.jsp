<%@ include file="../head.jsp" %>

<style>

#server-error {
	text-align: center;
	max-width: 800px;
}

</style>

<%@ include file="navbar-logout.jsp" %>

<div id="server-error" class="container">
	  <h2>Internal Server Error</h2>
	  <br>
	  <h4>The connection to the database may have been lost. Try refreshing the page, or come back later.</h4>
</div>

</body>
</html>