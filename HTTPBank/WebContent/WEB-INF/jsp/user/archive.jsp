<%@ include file="../head.jsp" %>

<style>

#archive {
	max-width: 800px;
}

#archive-nav {
	background-color:#e6e6e6;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="archive" class="container">
  <h3>Archive</h3>
  <p>History of all transactions.</p>
  <div id="transaction-accordion" class="transaction-list panel-group">
   <c:forEach var="account" items="${sessionScope.user.accounts}" varStatus="counter">
     <div class="panel panel-default">
       <div class="panel-heading">
         <div class="panel-title">
           <a class="accordion-toggle "data-toggle="collapse" data-parent="#transaction-accordion" href="#transaction-collapse${counter.count}">${account.type}</a>
           <p class="alignright">${account.number}</p>
         </div>
       </div>
       <div id="transaction-collapse${counter.count}" class="panel-collapse collapse <c:if test="${counter.count==1}">in</c:if>">
         <div class="panel-body">
       <table class="table table-striped">
         <thead>
           <tr>
             <th>Date</th>
             <th>Description</th>
             <th class="text-right">Amount</th>
           </tr>
         </thead>
         <tbody>
        		<c:forEach var="transaction" items="${account.archive}">
          		<tr><td>${transaction.date}</td><td>${transaction.description}</td><td class="text-right">${transaction.amount}</td></tr>
          	</c:forEach>
         </tbody>
       </table>
         </div>
       </div>
     </div>
   </c:forEach>
  </div>
</div>


</body>
</html>