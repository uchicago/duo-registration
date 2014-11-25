function hideShowToggle(c) {
	var d1 = false;
	var d2 = true;
	var d3 = true;
	var d4 = false;
	switch (c) {
		case 'INCORRECT':
			d2 = false;
			d3 = false;
			break;
		case 'RESET':
			$('#verifymsg').html("");
			$('#callstatus').html("Call Status");
			$('#makecallbtn').focus();
			break;
		case 'CALLING':
			d1 = true;
			d4 = true;
			break;
		case 'ANSWERED':
			d1 = true;
			d2 = false;
			d3 = false;
			d4 = true;
			break;
		case 'CALLCOMPLETE':
			d1 = false;
			d2 = false;
			d3 = false;
			break;
		case 'VERIFYING':
			d1 = true;
			d2 = true;
			d3 = true;
			d4 = true;
			break;
	}
	$('#makecallbtn').attr("disabled", d1);
	$('#verifytxtbox').attr("disabled", d2);
	$('#verifybtn').attr("disabled", d3);
	$('#backbtn').attr("disabled", d4);
}

//Declar Global Variables
var timer;
var ajaxReq;
var verifying = 'N';
var params = [{name: "enrollUserNPhone", value: "Continue"}];

//AJAX request Function(Global) for Call Status checking, include Loop function
function callRequestStatus() {
	$.ajax({
		type: "GET",
		url: 'enrollment/phoneverify.json/status',
		success: function(data) {
			$('#callstatus').html(data);
			$('#makecallbtn').attr("disabled", true);

			if (data === 'Call completed.' && verifying === 'N') {
				clearTimeout(timer);
				hideShowToggle("CALLCOMPLETE");
			}

			if (data === 'Call has been answered.') {
				hideShowToggle("ANSWERED");
				$("#verifytxtbox").focus();
			}
		},
		error: function() {
			clearTimeout(timer);
			$('#callstatus').html("Unable to Check Call Status!");
			hideShowToggle("RESET");
		},
		statusCode: {
			500: function() {
				clearTimeout(timer);
				hideShowToggle("RESET");
			}
		}
	});
	//Create Looping using SetTimeOut
	if (verifying === 'N') {
		timer = setTimeout('callRequestStatus()', 2000);
	} else {
		clearTimeout(timer);
	}
}

//JQuery Main Body
jQuery(document).ready(function($) {
	hideShowToggle("RESET");
	$('#makecallbtn').focus();

	$('#makecallbtn').click(function(event) {
		$('#verifymsg').html("");
		$('#callstatus').html("Call Status");
		$('#dismissalert').click();
		$.ajax({
			type: "GET",
			url: 'enrollment/phoneverify.json/call',
			success: function(data) {
				if (data === 'CALLING') {
					hideShowToggle(data);
					$('#verifytxtbox').val("");
				}
				//If success, begin querying the Call status non-stop until complete
				setTimeout('callRequestStatus()', 1000);
			},
			error: function(data) {
				$('#callstatus').html("Unable to Make Call, Please try again");
				hideShowToggle("RESET");
			},
			statusCode: {
				500: function() {
					hideShowToggle("RESET");
				}
			}
		});
	});


	$('#resetbtn').click(function(event) {
		clearTimeout(timer);
		clearTimeout(timer);
		clearTimeout(timer);
		hideShowToggle("RESET");
		$('#makecallbtn').focus();
	});

	/*
	 * Action for Verification Button Pressed
	 */
	$('#verifybtn').click(function(event) {
		verifying = 'Y';
		clearTimeout(timer);

		var pinToBeVerify = $("#verifytxtbox").val();
		if (!pinToBeVerify) {
			$('#verifymsg').html("Verification Code cannot be Empty!");
		} else if (pinToBeVerify.length != 4) {
			$('#verifymsg').html("Verification Code is not 4 Digits!");
		} else if (!jQuery.isNumeric(pinToBeVerify)) {
			$('#verifymsg').html("Verification Code is Numeric ONLY!");
		} else {
			hideShowToggle("VERIFYING");
			$('#verifymsg').html("Verifying...");

			$.ajax({
				type: "GET",
				url: 'enrollment/phoneverify.json/verify/' + pinToBeVerify,
				success: function(data) {
					if (data === 'VERIFIED') {
						clearInterval(timer);
						$('#backbtn').attr("disabled", true);
						$('#verifymsgdiv').removeClass("error").addClass("success");
						$('#verifymsg').html("Verification Success");
						$("#verifycomplete").submit();
					} else if (data === 'INCORRECT') {
						$('#verifymsg').html("Verification Code is incorrect!");
						hideShowToggle(data);
						$("#verifytxtbox").focus();
					}
				},
				error: function(data) {
					$('#callstatus').html("Call Status");
					$('#verifymsg').html("Verification Error, Please try again");
					hideShowToggle("RESET");
				},
				statusCode: {
					500: function() {
						$('#callstatus').html("Call Status");
						$('#verifymsg').html("Verification Error, Please try again(500)");
						hideShowToggle("RESET");
					},
					404: function() {
						$('#callstatus').html("Call Status");
						$('#verifymsg').html("Verification Error, Please try again(404)");
						hideShowToggle("RESET");
					}
				}
			});
		}
	});

	$("#verifytxtbox").keyup(function(e) {
		if (!(e.keyCode == '37' || e.keyCode == '39')) {
			this.value = this.value.replace(/[^0-9]/g, '');
		}
		if (e.which == 13) {
			$('#verifybtn').click();
		}
	});

	$("#verifycomplete").submit(function() {
		clearTimeout(timer);
		$('#callstatus').html("");
		$.each(params, function(i, param) {
			$('<input />').attr('type', 'hidden')
					.attr('name', param.name)
					.attr('value', param.value)
					.appendTo('#verifycomplete');
		});

	});

});