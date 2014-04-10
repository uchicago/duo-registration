<%-- 
    Document   : DuoDeviceMgmt
    Created on : Apr 7, 2014, 2:16:54 PM
    Author     : danielyu
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
		<script src="<c:url value="/resources/js/DuoDevice.js" />" ></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO Device Management Console</title>
    </head>
    <body>
        <h1>Welcome to Device Management Console</h1>
		<form:form method="post" commandName="DuoPerson">
			<table border="1">
				<tr>
					<th align="center">Select Device</th>
					<th align="center">DUO Phone ID</th>
					<th align="center">Phone Number</th>
					<th align="center">Phone Platform(OS)</th>
					<th align="center">Phone Type</th>
					<th align="center">Activation Status</th>
					<th align="center">SMS Capable</th>
					<th align="center">PUSH Capable(DUO Mobile App)</th>
					<th align="center">Phone Capable(Voice Dial)</th>
				</tr>
				<c:forEach items="${DuoPerson.phones}" var="phone">
					<tr>
						<td align="center"><form:radiobutton path="phone_id" value="${phone.id};${phone.capableSMS};${phone.capablePush};${phone.type}" /></td>
						<td align="center"><c:out value="${phone.id}"/></td>
						<td align="center"><c:out value="${phone.phoneNumber}"/></td>
						<td align="center"><c:out value="${phone.platform}"/></td>
						<td align="center"><c:out value="${phone.type}"/></td>
						<td align="center"><c:out value="${phone.activationStatus}"/></td>
						<td align="center"><c:out value="${phone.capableSMS}"/></td>
						<td align="center"><c:out value="${phone.capablePush}"/></td>
						<td align="center"><c:out value="${phone.capablePhone}"/></td>
					</tr>
				</c:forEach>

			</table>

			<div id="delDevice">
				<input type="submit" value="Remove Selected Device" name="duo"/>
			</div>
			<div id="sms">
				<input type="submit" value="Send SMS By Passcode" name="duo"/>
			</div>
			<div id="push">
				<input type="submit" value="Reactivate Your Phone" name="duo"/>
			</div>
			
			<form:hidden path="choosenDevice" id="devicetype"/>
		</form:form>
	</body>
</html>
