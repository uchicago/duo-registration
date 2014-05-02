<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix = "spring" uri = "http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
		<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
		<script src="<c:url value="/resources/js/DuoAsyncStatusCheck.js" />" ></script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Activation QR Code</title>
    </head>
    <body>
        <h1>Duo Activation QR Code</h1>

		<c:if test="${deviceNotActive}">
			<h1><spring:message code = "DeviceActivationRequire" /></h1>
		</c:if>

		<img src="${DuoPerson.QRcode}"><br><br>	

		<form:form method="post" commandName="DuoPerson" id="activecomplete"></form:form>	

		<form:form method="post" commandName="DuoPerson">
			<input type="submit" value="Send Activation Code via SMS" name="sendsms"/><br>
			<c:if test="${empty DuoPerson.QRcode}">
				<input type="submit" value="Generate onscreen QR Code" name="genQRcode"/>
			</c:if>
		</form:form>

    </body>
</html>

