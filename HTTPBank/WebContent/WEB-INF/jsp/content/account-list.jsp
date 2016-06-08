<div class="panel panel-default button-panel">
	<div class="panel-body">
		<ul class="list-group">
			<c:forEach var="account" items="${user.accounts}">
   			<li class="list-group-item">
     			<form action="getAccount" method="post">
     				<button class="btn btn-default" name="id" value="${account.id}">
     					<h4 id="text-left">
     						<c:choose>
	     						<c:when test="${account.name != '' }">
	     							${account.name}
	     						</c:when>
	     						<c:otherwise>
	     							${account.type}
	     						</c:otherwise>
     						</c:choose>
     					</h4>
     					<h4 id="text-center">${account.number}</h4>
     					<h4 id="text-right">${account.balance} ${account.currency}</h4>
     				</button>
     			</form>
     		</li>
			</c:forEach>
		</ul>
	</div>
</div>