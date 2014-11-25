jQuery(document).ready(function() {
	$('.tokeninputbox').keyup(function(e) {
		if (!(e.keyCode == '37' || e.keyCode == '39')) {
			var tokenType = $('#tokenType').val();
			if (tokenType == 'd1') {
				this.value = this.value.replace(/[^\w]/g, '').toUpperCase();
			} else {
				this.value = this.value.replace(/[^\w]/g, '');
			}
		}
	});
});