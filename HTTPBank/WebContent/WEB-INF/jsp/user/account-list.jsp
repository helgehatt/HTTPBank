<%@ include file="../head.jsp" %>

<style>

#account-list {
	max-width: 800px;
}

#account-list .table {
	margin: 0;
}

</style>

<%@ include file="navbar.jsp" %>

<div id="account-list" class="container">
  <h3>Accounts</h3>
  <p>Your accessible accounts are shown below.</p>
  <div id="account-accordion" class="panel-group">
   <c:forEach var="account" items="${sessionScope.user.accounts}" varStatus="counter">
     <div class="panel panel-default">
       <div class="panel-heading">
         <div class="panel-title">
           <a class="accordion-toggle "data-toggle="collapse" data-parent="#account-accordion" href="#account-collapse${counter.count}">${account.type}</a>
           <p class="alignright">${account.balance} ${account.currency}</p>
         </div>
       </div>
       <div id="account-collapse${counter.count}" class="panel-collapse collapse <c:if test="${counter.count==1}">in</c:if>">
         <div class="panel-body">
           <table class="table">
             <thead>
               <tr><th>Account information</th><th></th></tr>
             </thead>
             <tbody>
               <tr><td>Account number</td><td>${account.number}</td></tr>
              	<tr><td>IBAN</td><td>${account.iban}</td></tr>
               <tr><td>Interest rate</td><td>${account.interest}</td></tr>
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