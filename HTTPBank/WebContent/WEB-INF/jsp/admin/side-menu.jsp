<div id="side-menu" class="container col-sm-3">
	<div class="panel panel-default button-panel">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a data-toggle="collapse" href="#side-menu-collapse">Collapsible panel</a>
      </h4>
    </div>
		<div id="side-menu-collapse" class="panel-collapse collapse">
			<div class="panel-body">
				<ul class="list-group">
					<li class="list-group-item" id="account-list-item">
						<a class="btn btn-default side-menu-item" href="accounts">Accounts</a>
					</li>
					<c:if test="${pageScope.menu == 'account'}">
						<li class="list-group-item" id="account-info-item">
							<a class="btn btn-default side-menu-sub-item" href="accountinfo">Account info</a>
						</li>
						<li class="list-group-item" id="transaction-list-item">
							<a class="btn btn-default side-menu-sub-item" href="transactions">Transactions</a>
						</li>
						<li class="list-group-item" id="archive-item">
							<a class="btn btn-default side-menu-sub-item" href="archive">Archive</a>
						</li>
						<li class="list-group-item" id="edit-account-item">
							<a class="btn btn-default side-menu-sub-item" href="editaccount">Edit</a>
						</li>			
						<li class="list-group-item" id="close-account-item">
							<a class="btn btn-default side-menu-sub-item" href="closeaccount">Close</a>
						</li>
					</c:if>
					<li class="list-group-item" id="transfer-dom-item">
						<a class="btn btn-default side-menu-item" href="transfer">Transfer</a>
					</li>
					<c:if test="${pageScope.menu == 'transfer'}">
						<li class="list-group-item" id="transfer-int-item">
							<a class="btn btn-default side-menu-sub-item" href="international">International</a>
						</li>
					</c:if>
					<li class="list-group-item" id="deposit-withdrawal-item">
						<a class="btn btn-default side-menu-item" href="depositwithdrawal">Deposit / <br> Withdrawal</a>
					</li>
					<li class="list-group-item" id="user-info-item">
						<a class="btn btn-default side-menu-item" href="userinfo">User info</a>
					</li>
					<c:if test="${pageScope.menu == 'user'}">
						<li class="list-group-item" id="edit-user-item">
							<a class="btn btn-default side-menu-sub-item" href="edituser">Edit</a>
						</li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>	
</div>