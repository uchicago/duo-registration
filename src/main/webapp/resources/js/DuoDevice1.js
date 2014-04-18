jQuery(document).ready(function() {
	$('.confirmDelete').click(function() {
		var answer = confirm("Are you sure you want to delete this Device?");
		if (answer) {
			return true;
		} else {
			return false;
		}
	});
});


