<%@ include file="../head.jsp" %>

<style>

#account-list {
	max-width: 1200px;
}

#account-list-item a {
	background-color:#e6e6e6;
}

@media (min-width: 768px) {
	#text-left {
		float: left;
		text-align: left;
		width: 40%;
	}
	
	#text-center {
		display: inline-block;
		text-align: center;
		width: 20%;
	}
	
	#text-right {
		float: right;
		text-align: right;
		width: 40%;
	}
}


</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<%@ include file="side-menu.jsp" %>
	
	<div id="account-list" class="container col-sm-9">
	
	<%@ include file="../content/account-list.jsp" %>
	
	</div>
	
	
</div>


</body>
</html>