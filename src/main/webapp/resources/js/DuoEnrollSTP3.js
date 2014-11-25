jQuery(document).ready(function() {
	$('#dialCode').change(function() {
		var code = $('#dialCode').val();
		$('#dialcode').html("<b>Country Dial Code:</b>&nbsp" + code);
	});

	$('#dialcode').html("<b>Country Dial Code:</b>&nbsp" + $('#dialCode').val());

	$('.phoneboxes').keyup(function(e) {
		if (!(e.keyCode == '37' || e.keyCode == '39')) {
			this.value = this.value.replace(/[^0-9(()#*)\-]/g, '');
		}
	});

});

