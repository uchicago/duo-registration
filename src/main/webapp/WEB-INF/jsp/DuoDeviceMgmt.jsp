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
		<script src="<c:url value="/resources/js/DuoDevice1.js" />" ></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO Device Management Console</title>
    </head>
    <body>
        <h1>Welcome to Device Management Console</h1>

		<c:if test="${displayPhones}">

			<table border="1">
				<h2>Your Phones</h2>
				<tr>
					<th align="center">DUO Phone ID</th>
					<th align="center">Phone Number</th>
					<th align="center">Phone Platform(OS)</th>
					<th align="center">Phone Type</th>
					<th align="center">Activation Status</th>
					<th align="center">SMS Capable</th>
					<th align="center">PUSH Capable(DUO Mobile App)</th>
					<th align="center">Phone Capable(Voice Dial)</th>
					<th align="center">SMS Passcode Sent</th>
					<th align="center">Options</th>
				</tr>
				<c:forEach items="${DuoPerson.phones}" var="phone" varStatus="i">
					<form:form method="post" commandName="DuoPerson">
						<tr>
							<td align="center"><c:out value="${phone.id}"/></td>
							<td align="center"><c:out value="${phone.phoneNumber}"/></td>
							<td align="center"><c:out value="${phone.platform}"/></td>
							<td align="center"><c:out value="${phone.type}"/></td>
							<td align="center"><c:out value="${phone.activationStatus}"/></td>
							<td align="center"><c:out value="${phone.capableSMS}"/></td>
							<td align="center"><c:out value="${phone.capablePush}"/></td>
							<td align="center"><c:out value="${phone.capablePhone}"/></td>
							<td align="center"><c:out value="${phone.smsPassCodeSent}"/></td>
							<td>
								<input type="submit" value="Remove Phone" name="removedevice" class="confirmDelete" id="remove${i.index}"/>
								<c:if test="${phone.capableSMS}">
									<input type="submit" value="Send SMS Passcodes"  name="sendsmscode" id="smspasscode${i.index}"/>
								</c:if>
								<c:if test="${not phone.activationStatus && phone.type == 'Mobile' }">
									<input type="submit" value="Activate Phone"  name="deviceactivation" id="activate${i.index}"/>
								</c:if>
								<c:if test="${phone.capablePush && phone.activationStatus}">
									<input type="submit" value="Re-Activate Phone"  name="deviceactivation" id="activate${i.index}"/>
								</c:if>
							</td>
						</tr>
						<form:hidden path="choosenDevice" value="${phone.type}" />
						<form:hidden path="phone_id" value="${phone.id}" />
					</form:form>
				</c:forEach>
			</table>
		</c:if>
		
		<c:if test="${displayTablets}">

			<table border="1">
				<h2>Your Tablets</h2>
				<tr>
					<th align="center">DUO Tablet(Phone) ID</th>
					<th align="center">Tablet's Name</th>
					<th align="center">Tablet Platform(OS)</th>
					<th align="center">Tablet Type</th>
					<th align="center">Activation Status</th>
					<th align="center">SMS Capable</th>
					<th align="center">PUSH Capable(DUO Mobile App)</th>
					<th align="center">Phone Capable(Voice Dial)</th>
					<th align="center">Options</th>
				</tr>
				<c:forEach items="${DuoPerson.tablets}" var="tablet" varStatus="i">
					<form:form method="post" commandName="DuoPerson">
						<tr>
							<td align="center"><c:out value="${tablet.id}"/></td>
							<td align="center"><c:out value="${tablet.deviceName}"/></td>
							<td align="center"><c:out value="${tablet.platform}"/></td>
							<td align="center"><c:out value="${tablet.type}"/></td>
							<td align="center"><c:out value="${tablet.activationStatus}"/></td>
							<td align="center"><c:out value="${tablet.capableSMS}"/></td>
							<td align="center"><c:out value="${tablet.capablePush}"/></td>
							<td align="center"><c:out value="${tablet.capablePhone}"/></td>
							<td>
								<input type="submit" value="Remove Tablet" name="removedevice" class="confirmDelete" id="remove${i.index}"/>
								<c:if test="${not tablet.activationStatus}">
									<input type="submit" value="Activate Device"  name="deviceactivation" id="activate${i.index}"/>
								</c:if>
								<c:if test="${tablet.capablePush}">
									<input type="submit" value="Re-Activate Tablet"  name="deviceactivation" id="activate${i.index}"/>
								</c:if>
							</td>
						</tr>
						<form:hidden path="choosenDevice" value="Tablet" />
						<form:hidden path="phone_id" value="${tablet.id}" />
					</form:form>
				</c:forEach>
			</table>
		</c:if>
		
		<c:if test="${displayTokens}">

			<table border="1">
				<h2>Your Tokens</h2>
				<tr>
					<th align="center">DUO Token ID</th>
					<th align="center">Token Type</th>
					<th align="center">Token Serial Number</th>
					<th align="center">Options</th>
				</tr>
				<c:forEach items="${DuoPerson.tokens}" var="token" varStatus="i">
					<form:form method="post" commandName="DuoPerson">
						<tr>
							<td align="center"><c:out value="${token.id}"/></td>
							<td align="center"><c:out value="${token.type}"/></td>
							<td align="center"><c:out value="${token.serial}"/></td>
							<td>
								<input type="submit" value="Remove Token" name="removedevice" class="confirmDelete" id="remove${i.index}"/>
							</td>
						</tr>
						<form:hidden path="tokenSerial" value="${token.serial}" />
						<form:hidden path="tokenId" value="${token.id}" />
						<form:hidden path="choosenDevice" value="Token" />
					</form:form>
				</c:forEach>
			</table>
		</c:if>
		
		<BR><BR>
		<form:form method="post">
			<input type="submit" value="HOME" name="reset"/>
		</form:form>
  

	</body>
</html>