<%-- 
    Document   : DuoRegistration Program
    Created on : Nov 25, 2014, 2:16:54 PM
    Author     : Daniel Yu (danielyu@uchicago.edu)
	Copyright 2014 University of Chicago
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<head>
	<meta charset="iso-8859-1">
	<title>DUO Registration</title>
	<script src="<spring:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
	<script src="<spring:url value="/resources/js/DuoPhoneVerify.js" />" ></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
</head>
<body>

	<h1>DUO Device Registration - Verify Number</h1>
	<c:if test="${DuoPerson.choosenDevice == 'mobile'}">
		<h3>Mobile Phone Number: ${DuoPerson.phonenumber}</h3>
	</c:if>

	<c:if test="${DuoPerson.choosenDevice == 'landline'}">
		<h3>Landline Phone Number: ${DuoPerson.phonenumber}</h3>
	</c:if>

	<p>
		<c:if test="${DuoPerson.choosenDevice == 'mobile'}">
			To verify your mobile phone number,
		</c:if>
		<c:if test="${DuoPerson.choosenDevice == 'landline'}">
			To verify your landline phone number,
		</c:if>
		click the "Call Me" button below, answer the call, and then enter the 4 digit verification code provided. 
		After you verify the code, the system will automatically complete the registration process.
	</p>

	<button type="button" class="close" data-dismiss="alert" id="dismissalert">&times;</button>
	Please ensure that you can answer this specific phone before clicking the "Call Me" button

	<table>
		<tr>
			<td><button id="makecallbtn" >Call Me</button>&nbsp&nbsp&nbsp&nbsp</td>
			<td id="callstatus">Call Status</td>
		</tr>
	</table>

	<br><br>

	<h3>Verification Code:</h3>

	<div class="form-inline form-group">
		<input type="text" class="form-control" id="verifytxtbox" placeholder="Enter Code" value="" maxlength="4">
		<button id="verifybtn" class="btn btn-default">Verify</button>
		<span class="control-group error" id="verifymsgdiv">
			<span class="help-inline" id="verifymsg"></span>
		</span>
	</div>

	<br><br>

	<form:form method="post" commandName="DuoPerson">
		<input type="submit" class="btn btn-inverse"  value="Back" name="back" id="backbtn" />
		&nbsp&nbsp&nbsp&nbsp

		<c:if test="${DuoPerson.deviceOS == 'unknown' && DuoPerson.choosenDevice == 'mobile'}">
			<input type="hidden" name="_backpage" value="4" />
		</c:if>

		<c:if test="${DuoPerson.choosenDevice == 'landline'}">
			<input type="hidden" name="_backpage" value="3" />
		</c:if>

	</form:form>

	<form:form method="post" commandName="DuoPerson" id="verifycomplete"></form:form>

</body>
</html>	