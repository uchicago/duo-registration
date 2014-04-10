jQuery(document).ready(function($) {
	$('#sms').hide();
	$('#push').hide();
	$('#delDevice').hide();

	$("input:radio").click(function() {
		var array = $(this).val().split(";");
		phoneid = array[0];
		var sms = array[1];
		var push = array[2];
		devicetype = array[3];
		alert("sms Capable" + sms);
		alert($(this).attr("value"));
		if (sms == "true") {
			$('#sms').show();
		} else {
			$('#sms').hide();
		}
		if (push == "true") {
			$('#push').show();
		} else {
			$('#push').hide();
		}
		if (phoneid != '') {
			$('#delDevice').show();
		} else {
			$('#delDevice').hide();
		}

	});

	$("form").submit(function() {
		alert("Submitted: " + $(this).attr("value"));
		alert($("input:radio:checked").val());
		$("input:radio:checked").val(phoneid);
		alert("Value Sending to next Form: "+$("input:radio:checked").val());
		$('#devicetype').val(devicetype);
		alert("Hidden Value Sending to next Form: "+$('#devicetype').val());
	});

});


