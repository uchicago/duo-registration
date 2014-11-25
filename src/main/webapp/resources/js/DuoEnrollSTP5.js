jQuery(document).ready(function() {
	$("#registerbtn").attr("disabled", true);
	$("#bb10pic").hide();
	$("#bbpic").hide();

	$("#appinstallconfirm").click(function() {
		if ($("#appinstallconfirm").is(':checked'))
			$("#registerbtn").attr("disabled", false); // checked
		else
			$("#registerbtn").attr("disabled", true); // unchecked

	});

	$("#bb10selected").click(function() {
		$("#bb10pic").show();
		$("#bbpic").hide();
	});

	$("#bbselected").click(function() {
		$("#bbpic").show();
		$("#bb10pic").hide();
	});

});