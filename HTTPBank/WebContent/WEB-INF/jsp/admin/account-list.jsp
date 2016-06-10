<%@ include file="../head.jsp" %>

<style>

#account-list-item a {
	background-color:#e6e6e6;
}


</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">

	<%@ include file="side-menu.jsp" %>
	
	<div id="account-list" class="container container-lg col-sm-9">
	
		<%@ include file="../content/account-list.jsp" %>
	
	
					<li class="list-group-item">
	     			<form action="newaccount" method="get">
	     				<button class="btn btn-default">
	     					<h4>New account</h4>
	     				</button>
	     			</form>
					</li>
				</ul>
			</div>
		</div>
	</div>
	
	
</div>


</body>
</html>