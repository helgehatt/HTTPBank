<%@ include file="../head.jsp" %>

<style>

#account-list {
	max-width: 800px;
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

<div id="account-list" class="container">
  <h3>Accounts</h3>
  <p>Your accessible accounts are shown below.</p>
  
	<%@ include file="../content/account-list.jsp" %>
	
			</ul>
		</div>
	</div>
</div>


</body>
</html>