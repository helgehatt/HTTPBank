$(function(){
    $('#from').change(function(){
		var selected = $(this).find('option:selected');
		
		var currency = selected.data('currency');
		$('#currency').val(currency);
		
		var balance = selected.data('balance');
		$('#balance').val(balance + ' ' + currency);
    });
});