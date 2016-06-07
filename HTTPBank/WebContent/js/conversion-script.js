function convert(from, to, amount) {
	var yql_base_url = "https://query.yahooapis.com/v1/public/yql";
	var yql_query = 'select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20("' + from + to + '")';
	var yql_query_url = yql_base_url + "?q=" + yql_query + "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	$.get(yql_query_url, function(data) {
		var total = amount * data.query.results.rate.Rate;
		$('#change').val(total.toFixed(2));
	});
}

function updateChange() {
	var selectedCurrency = $('#to-currency').find('option:selected').val();
	var accountCurrency = $('#from-currency').val();
	var amount = $('#amount').val();
	
	if (accountCurrency === selectedCurrency) {
		$('#change').val(amount);
	} else {
		convert(selectedCurrency, accountCurrency, amount);
	}
}

function onSubmit() {
	var from = $('#to-currency').find('option:selected').val();
	var to = $('#from-currency').val();
	var amount = $('#amount').val();
	
	var yql_base_url = "https://query.yahooapis.com/v1/public/yql";
	var yql_query = 'select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20("' + from + to + '")';
	var yql_query_url = yql_base_url + "?q=" + yql_query + "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	jQuery.ajax({
        url: yql_query_url,
        success: function (data) {
	    		var total = amount * data.query.results.rate.Rate;
	    		$('#change').val(total.toFixed(2));
        },
        async: false
    });
}

$(function(){
	
	$('#to-currency').change(function(){
		updateChange();
	});
	
	$('#amount').change(function(){
		updateChange();
	});
	
	$('form').on('submit', function(){
		onSubmit();
	})
})