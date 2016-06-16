<%@ include file="../content/head.jsp" %>

<script type="text/javascript">
$(function(){
	$('.message').click(function() {
		var expanded = $(this).data('expanded');
		if (expanded === false) {
			$(this).text($(this).data('text'));
			$(this).data('expanded', true);
		} else {
			$(this).text($(this).data('preview'));
			$(this).data('expanded', false);
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
		          <th class="column-hide">Date</th>
		          <th>Message</th>
		          <th class="column-hide">From</th>		      		       
		        </tr>
		      </thead>
		      <tbody>
		      	<c:forEach var="message" items="${user.messages}">
		        	<tr>
		        		<td class="column-hide" style="white-space:nowrap">${message.date}</td>
		        		<td>
		        			<p class="column-show">${message.date}</p>
		        			<c:choose>
		        				<c:when test="${message.preview != null}">
		        					<a class="message" href="#" data-preview="${message.preview}"data-text="${message.text}" data-expanded="false">${message.preview}</a>
		        				</c:when>
		        				<c:otherwise>
		        					<p>${message.text}</p>
		        				</c:otherwise>
		        			</c:choose>
		        			<c:if test="${message.preview != null}">
		        			
		        			</c:if>
		        			<p class="column-show">${message.senderName}</p>
		        		</td>
		        		<td class="column-hide" style="white-space:nowrap">${message.senderName}</td>
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