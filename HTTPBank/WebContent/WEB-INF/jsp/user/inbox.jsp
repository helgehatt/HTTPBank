<%@ include file="../head.jsp" %>

<script type="text/javascript">
$(function(){
	$('.message').click(function() {
		var expanded = this.data('expanded');
		if (expanded === false) {
			this.val(this.data('text'))
			this.data('expanded').val('true');
		} else {
			this.val(this.data('preview'))
			this.data('expanded').val('false');
		}
		return false;
	});
});
</script>

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
		        		<td><a class="message" href="#" data-preview="${message.preview}"data-text="${message.text}" data-expanded="false">${message.preview}</a></td>
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