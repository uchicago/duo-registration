jQuery(document).ready(function($) {

	var params = [
		{name: "_page", value: "5"},
		{name: "enrollsteps", value: "Next"}];

//	$('#activated').attr("disabled", true);

	// Check The Status Every 2 Seconds
	var timer = setInterval(function() {

		$.ajax({
			url: 'enrollment/activationstatus.json',
			success: function(data) {

				if (data === 'true') {
//					$('#activated').attr("disabled", false);
					clearInterval(timer);
					$("#activecomplete").submit();

				}
			}
		});

	}, 2000);

//	$("#activecomplete").submit(function() {
//		$('<input />').attr('type', 'hidden')
//				.attr('name', '_page')
//				.attr('value', '5')
//				.appendTo('#activecomplete');
//		$('<input />').attr('type', 'hidden')
//				.attr('name', 'enrollsteps')
//				.attr('value', 'Next')
//				.appendTo('#activecomplete');
//	});

	$("#activecomplete").submit(function() {
		$.each(params, function(i, param) {
			$('<input />').attr('type', 'hidden')
					.attr('name', param.name)
					.attr('value', param.value)
					.appendTo('#activecomplete');
		});

	});

});


