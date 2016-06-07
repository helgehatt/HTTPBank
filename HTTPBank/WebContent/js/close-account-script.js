function convert(from, to, amount) {
	var yql_base_url = "https://query.yahooapis.com/v1/public/yql";
	var yql_query = 'select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20("' + from + to + '")';
	var yql_query_url = yql_base_url + "?q=" + yql_query + "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	$.get(yql_query_url, function(data) {
		var total = amount * data.query.results.rate.Rate;
		$('#change').val(total.toFixed(2));
	});
}

function updateChange(currency) {
	var accountCurrency = $('#from').data('currency');
	var selectedCurrency = currency;
	var balance = parseFloat($('#from').data('balance')).toFixed(2);
	
	if (accountCurrency === selectedCurrency) {
		$('#change').val(balance);
	} else {
		convert(accountCurrency, selectedCurrency, balance);
	}
}

function transfer(){
	$('#to-form-group').show();
	$('#deposit-currency').show();
	$('#withdrawal-currency').hide();
	$('#change-label').text('Deposited:');
	$('button').val('transfer');
	
	var currency = $('#to').find('option:selected').data('currency');
	updateChange(currency);
	$('#deposit-currency').val($('#to').find('option:selected').data('currency'));
}

function withdrawal(){
	$('#to-form-group').hide();
	$('#deposit-currency').hide();
	$('#withdrawal-currency').show();
	$('#change-label').text('Withdrawn:');
	$('button').val('withdrawal');
	
	var currency = $('#withdrawal-currency').find('option:selected').val();
	updateChange(currency);
	$('#to-currency').val($('#from').data('currency'));
}

$(function(){
	var selected = $('#to').find('option:selected');
	var balance = selected.data('balance');
	var currency = selected.data('currency');
	$('#to-balance').val(balance + ' ' + currency);
	$('#deposit-currency').val(currency);
	$('#withdrawal-currency').hide();
	updateChange(currency);
	
	$('#transfer-pill').click(function(){
		transfer();
	});
	
	$('#withdrawal-pill').click(function(){
		withdrawal();
	});
	
	$('#to').change(function(){
		var selected = $(this).find('option:selected');
		
		var currency = selected.data('currency');
		$('#deposit-currency').val(currency);
		
		var balance = selected.data('balance');
		$('#to-balance').val(balance + ' ' + currency);
		
		updateChange(currency);
  });
	
	$('#withdrawal-currency').change(function(){
		var currency = $('#withdrawal-currency').find('option:selected').val();
		updateChange(currency);
	});
});