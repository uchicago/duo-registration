<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
		<script type="text/javascript">
			function hideShowToggle(c) {
				var d1 = false;
				var d2 = false;
				var d3 = false;
				var d4 = false;
				switch (c) {
					case 'VERIFIED':
						d1 = true;
						d2 = true;
						d3 = true;
						break;
					case 'INCORRECT':
						d4 = true;
						break;
					case 'RESET':
						d4 = true;
						break;
					case 'CALLING':
						d1 = true;
						break;
				}
				$('#makecallbtn').attr("disabled", d1);
				$('#verifytxtbox').attr("disabled", d2);
				$('#verifybtn').attr("disabled", d3);
				$('#registerbtn').attr("disabled", d4);

			}

			jQuery(document).ready(function() {

				$('#registerbtn').attr("disabled", true);

				$('#makecallbtn').click(function(event) {
					$.ajax({
						type: "GET",
						url: 'enrollment/phoneverify.json/call',
						success: function(data) {
							if (data === 'CALLING') {
								hideShowToggle(data);
								$("#verifytxtbox").focus();
							}
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

					var timer = setInterval(function() {
						$.ajax({
							type: "GET",
							url: 'enrollment/phoneverify.json/status',
							success: function(data) {
								$('#callstatus').html(data);

								if (data === 'Call completed.') {
									$('#makecallbtn').attr("disabled", false);
									clearInterval(timer);
								}
							},
							error: function() {
								clearInterval(timer);
								$('#callstatus').html("Unable to Check Call Status!");
								$('#makecallbtn').attr("disabled", false);
							},
							statusCode: {
								500: function() {
									clearInterval(timer);
									$('#makecallbtn').attr("disabled", false);
								}
							}
						});
					}, 1000);
				});

				$('#verifybtn').click(function(event) {
					var pinToBeVerify = $("#verifytxtbox").val();
					if (!pinToBeVerify) {
						alert("Verification Pin cannot be Empty!");
					} else if (pinToBeVerify.length != 4) {
						alert("Verification Pin needs to be 4 Digits Long.");
					} else if (!jQuery.isNumeric(pinToBeVerify)) {
						alert("Verification Pin is Numeric ONLY!");
					} else {
						$('#verifybtn').attr("disabled", true);
						$('#verifytxtbox').attr("disabled", true);

						$.ajax({
							type: "GET",
							url: 'enrollment/phoneverify.json/verify/' + pinToBeVerify,
							success: function(data) {
								if (data === 'VERIFIED') {
									$('#callstatus').html("Congratulation! You have successfully confirmed your phone");
									hideShowToggle(data);
								} else if (data === 'INCORRECT') {
									$('#callstatus').html("Pin Incorrect, Please try again");
									hideShowToggle(data);
								}
							},
							error: function(data) {
								$('#callstatus').html("Unable to Verify Pin, Please try again");
								hideShowToggle("RESET");
							},
							statusCode: {
								500: function() {
									$('#callstatus').html("Unable to Verify Pin, Please try again (500)");
									hideShowToggle("RESET");
								},
								404: function() {
									$('#callstatus').html("Unable to Verify Pin, Please try again (404)");
									hideShowToggle("RESET");
								}
							}
						});
					}
				});

				$("#verifytxtbox").keyup(function(e) {
					this.value = this.value.replace(/[^0-9\.]/g, '');
					if (e.which == 13) {
						$('#verifybtn').click();
					}
				});
			});

		</script>


		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UChicago 2Factor Portal</title>
    </head>
    <body>
		<h1>Verify Ownership</h1>
		<h3>Phone Number: ${DuoPerson.phonenumber}</h3>

		<table>
			<tr>
				<td colspan="2">1. We can call you with a verification Code to verify that you own this number</td>
			</tr>
			<tr>
				<td id="callstatus">Call Status</td>
				<td></td>
			</tr>
			<tr>
				<td><button id="makecallbtn">Call me</button></td>
				<td></td>
			</tr>
			<tr>
				<td>2. Verification code(4 digits):</td>
				<td><input type="text" id="verifytxtbox" value="" maxlength="4"></td>
			</tr>
			<tr>
				<td><button id="verifybtn">Verify</button></td>
				<td></td>
			</tr>
		</table>



		<form:form method="post" commandName="DuoPerson">


			<table>


				<tr>
					<td><input type="submit" value="REGISTER" name="enrollUserNPhone" id="registerbtn" /></td>
					<td></td><td></td><td></td>
				</tr>		

			</table>


			<input type="hidden" name="_page" value="4" />
		</form:form>
    </body>
</html>
