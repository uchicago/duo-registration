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
        <h1>Duo Activation QR Code</h1>

		<c:if test="${deviceNotActive}">
			<h1><spring:message code = "DeviceActivationRequire" /></h1>
		</c:if>

		<form:form method="post" commandName="DuoPerson">
			<img src="${DuoPerson.QRcode}"><br><br>
			<input type="submit" value="Next" name="enrollsteps"/>
			<input type="hidden" name="_page" value="5" />
			<br><br>
			<input type="submit" value="Send Activation Code via SMS" name="sendsms"/>
				
		</form:form>
		
		
		
    </body>
</html>

