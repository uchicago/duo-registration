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
	<script src="<spring:url value="/resources/js/DuoAsyncStatusCheck.js" />" ></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
</head>
<body>
	<h1 class="page-title">DUO Device Registration - Activate Duo Mobile App</h1>

	<c:if test="${deviceNotActive}">
		<h1><spring:message code = "DeviceActivationRequire" /></h1>
	</c:if>

	<p><b>Activate Duo Mobile for ${DuoPerson.deviceOS}</b><p>

	<ol>
		<li>Open the Duo Mobile app.</li>
		<li>Tap the "+" button. Then tap "Scan Barcode."</li>
		<li>Scan the barcode on this screen.</li>
	</ol>							

	<p>(Can't scan the barcode? Click below button to text an SMS activation code to your device to verify activation.)</p>	

	<form:form method="post" commandName="DuoPerson">
		<c:if test="${DuoPerson.choosenDevice == 'mobile'}">
			<input type="submit" class="btn btn-primary actionbtns" value="Send Activation Code via SMS" name="sendsms" />
		</c:if>
	</form:form>

	<form:form method="post" commandName="DuoPerson" id="activecomplete"></form:form>

	<c:if test="${not empty DuoPerson.QRcode}">
		<p>
			<b>Scan Barcode Below:</b> <br>
			<img src="${DuoPerson.QRcode}">
		</p>
	</c:if>

	<form:form method="post" commandName="DuoPerson">
		<c:if test="${empty DuoPerson.QRcode}">
			<input type="submit" class="btn btn-primary actionbtns" value="Generate onscreen QR Code" name="genQRcode" />
		</c:if>
	</form:form>

</body>
</html>	