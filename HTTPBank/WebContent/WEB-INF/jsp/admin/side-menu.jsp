<div id="side-menu" class="container col-sm-3">
	<div class="panel panel-default button-panel">
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
					<li class="list-group-item" id="deposit-item">
						<a class="btn btn-default side-menu-sub-item" href="deposit">Deposit</a>
					</li>
					<li class="list-group-item" id="withdrawal-item">
						<a class="btn btn-default side-menu-sub-item" href="withdrawal">Withdrawal</a>
					</li>				
					<li class="list-group-item" id="edit-account-item">
						<a class="btn btn-default side-menu-sub-item" href="editaccount">Edit</a>
					</li>
					<li class="list-group-item" id="archive-item">
						<a class="btn btn-default side-menu-sub-item" href="viewarchive">Archive</a>
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