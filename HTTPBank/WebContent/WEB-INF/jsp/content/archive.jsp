<div class="panel panel-default">
	<div class="panel-body">
		<form class="form-horizontal" role="form" action="getArchive" method="post" id="archive-form">
			<div class="form-group">
				<label class="control-label col-sm-4" for="from">From date:</label>
				<div class="col-sm-8">
					<input name="from" class="form-control" type="date" id="from" class="form-control">
				</div>	     				
			</div>	     	
			<div class="form-group">
				<label class="control-label col-sm-4" for="to">To date:</label>
				<div class="col-sm-8">
					<input name="to" class="form-control" type="date" id="to" class="form-control">
					<span class="error">${pageScope.error}</span>
				</div>	     				
			</div>
  		<button class="btn btn-default btn-center">Fetch</button>
		</form>   
    <table class="table table-striped">
      <thead>
        <tr>
          <th class="column-hide">Date</th>
          <th>Description</th>
          <th class="text-right column-hide">Amount</th>
        </tr>
      </thead>
      <tbody>
      	<c:forEach var="transaction" items="${archive}">
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