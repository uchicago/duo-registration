<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix = "spring" uri = "http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Activation QR Code</title>
    </head>
    <body>
        <h1>Duo Activation Code(Thru Device Management)</h1>

		<c:if test="${deviceNotActive}">
			<h1><spring:message code = "DeviceActivationRequire" /></h1>
		</c:if>

		<img src="${DuoPerson.QRcode}"><br><br>


		<br><br>
		<form:form method="post" commandName="DuoPerson">
			<input type="submit" value="Send Activation Code via SMS" name="deviceactivation"/>
			<input type="hidden" name="action" value="sendsms" />
		</form:form>
		<c:if test="${empty DuoPerson.QRcode}">
			<form:form method="post" commandName="DuoPerson">
				<input type="submit" value="Generate onscreen QR Code" name="deviceactivation"/>
				<input type="hidden" name="action" value="qrcode" />	
			</form:form>
		</c:if>
		<br><br>
		<form:form method="post" commandName="DuoPerson">
			<input type="submit" value="Finish" name="deviceactivation"/>
			<input type="hidden" name="action" value="finish" />
		</form:form>



    </body>
</html>

