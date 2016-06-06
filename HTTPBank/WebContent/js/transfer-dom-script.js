$(function(){
    $('#from').change(function(){
       var currency = $(this).find('option:selected').data('currency');
       $('#currency').val(currency);
    });
});