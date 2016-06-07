$(function(){	
	var currency = $('#from').find('option:selected').data('currency');
	$('#to-currency').val(currency);
	
	$('#from').change(function(){
		var selected = $(this).find('option:selected');
		
		var currency = selected.data('currency');
		$('#to-currency').val(currency);
		$('#from-currency').val(currency);
		
		var balance = selected.data('balance');
		$('#balance').val(balance + ' ' + currency);
		
		var amount = $('#amount').val();
		$('#change').val(amount);
  });
});