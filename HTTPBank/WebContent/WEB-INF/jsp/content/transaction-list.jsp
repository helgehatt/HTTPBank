<div class="panel panel-default">
	<div class="panel-body">
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Date</th>
          <th>Description</th>
          <th class="text-right">Amount (${account.currency})</th>
        </tr>
      </thead>
      <tbody>
      	<c:forEach var="transaction" items="${account.transactions}">
        	<tr>
        		<td>${transaction.date}</td>
        		<td>${transaction.description}</td>
        		<td class="text-right">${transaction.amount}</td>
        	</tr>
        </c:forEach>
      </tbody>
  	</table>
	</div>
</div>