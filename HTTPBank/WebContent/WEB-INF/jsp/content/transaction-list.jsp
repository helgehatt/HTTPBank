<div class="panel panel-default">
	<div class="panel-body">
    <table class="table table-striped">
      <thead>
        <tr>
          <th class="column-hide">Date</th>
          <th>Description</th>
          <th class="text-right column-hide">${account.currency}</th>
        </tr>
      </thead>
      <tbody>
      	<c:forEach var="transaction" items="${account.transactions}">
        	<tr>
        		<td class="column-hide" style="white-space:nowrap">${transaction.date}</td>
        		<td>
        			<p class="column-show">${transaction.date}</p>
        			${transaction.description}
        			<p class="column-show">${transaction.amount} ${account.currency}</p>
        		</td>
        		<td class="text-right column-hide" style="white-space:nowrap">${transaction.amount}</td>
        	</tr>
        </c:forEach>
      </tbody>
  	</table>
	</div>
</div>