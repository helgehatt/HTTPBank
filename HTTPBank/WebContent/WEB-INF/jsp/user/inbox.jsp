<%@ include file="../head.jsp" %>

<style>

</style>

<%@ include file="navbar.jsp" %>

<div class="container-fluid">
	
	<c:set var="menu" scope="page" value="account"/>
	
	<div id="inbox" class="container container-md">
		<div class="panel panel-default">
			<div class="panel-body">
		    <table class="table table-striped">
		      <thead>
		        <tr>
		          <th>Date</th>
		          <th>Message</th>
		          <th>From</th>		      		       
		        </tr>
		      </thead>
		      <tbody>
		      	<c:forEach var="message" items="${user.messages}">
		        	<tr>
		        		<td>${message.date}</td>
		        		<td>${message.message}</td>
		        		<td>${message.senderName}</td>
		        	</tr>
		        </c:forEach>
		      </tbody>
		  	</table>
			</div>
		</div>
	</div>
	
</div>

</body>
</html>