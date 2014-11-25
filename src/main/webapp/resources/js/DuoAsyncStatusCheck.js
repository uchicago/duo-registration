jQuery(document).ready(function($) {

	var params = [
		{name: "_page", value: "6"},
		{name: "enrollsteps", value: "Next"}];

	// Check The Status Every 2 Seconds
	var timer = setInterval(function() {
		$.ajax({
			url: 'enrollment/activationstatus.json',
			success: function(data) {
				if (data === 'true') {
					$('.actionbtns').prop("disabled", true);
					clearInterval(timer);
					$("#activecomplete").submit();
				}
			}
		});
	}, 2000);

	$("#activecomplete").submit(function() {
		$.each(params, function(i, param) {
			$('<input />').attr('type', 'hidden')
					.attr('name', param.name)
					.attr('value', param.value)
					.appendTo('#activecomplete');
		});

	});

});


