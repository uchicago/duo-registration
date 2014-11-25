jQuery(document).ready(function() {
	$('.confirmDelete').click(function() {
		var answer = confirm("Are you sure you want to delete this Device?");
		if (answer) {
			return true;
		} else {
			return false;
		}
	});
	
	$('.confirmActivation').click(function() {
		var answer = confirm("The Activation Process includes automated process of Removing and Re-Adding your device, stopping in the midst of activation may result in lost of device registration!");
		if (answer) {
			return true;
		} else {
			return false;
		}
	});
	
	
	$('[data-toggle="collapse"]').click(function() {
		$(this).text($(this).text() == '<<Less' ? 'MORE>>' : '<<Less');
	});
	
	$('.confirmActivation').tooltip();

});


