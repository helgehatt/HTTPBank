$(function(){
    $('#from').change(function(){
		var selected = $(this).find('option:selected');
		
		$('#id').val(selected.data('id'));
		
		var currency = selected.data('currency');
		$('#currency').val(currency);
		
		var balance = selected.data('balance');
		$('#balance').val(balance + ' ' + currency);
    });
});