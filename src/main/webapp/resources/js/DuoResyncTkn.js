jQuery(document).ready(function() {

	$('.tokenboxes').keyup(function(e) {
		if (!(e.keyCode == '37' || e.keyCode == '39')) {
			this.value = this.value.replace(/[^0-9]/g, '');
		}
	});

});


