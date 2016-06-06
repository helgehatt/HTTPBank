function setCurrency() {
	var currency = $('#from').find('option:selected').data('currency');
	$('#to-currency').val(currency);
	$('#from-currency').val(currency);
}

$(function(){
	setCurrency();
	
	$('#from').change(function(){
		setCurrency();
		var amount = $('#amount').val();
		$('#change').val(amount);
  });
});