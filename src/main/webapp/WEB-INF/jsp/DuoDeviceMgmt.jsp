<%-- 
    Document   : DuoRegistration Program
    Created on : Nov 25, 2014, 2:16:54 PM
    Author     : Daniel Yu (danielyu@uchicago.edu)
	Copyright 2014 University of Chicago
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
	<meta charset="iso-8859-1">
	<title>2Factor Authentication | The University of Chicago</title>
	<script src="<spring:url value='/resources/js/libs/modernizr.js'/>"></script>
	<script src="<spring:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
	<script src="<spring:url value="/resources/js/DuoDevMgmt.js" />" ></script>
</head>
<body>

	<h1>DUO Portal: Manage Devices</h1>

	<h2><b>Fullname: ${DuoPerson.fullName}</b></h2>
	
	<a href="<spring:url value='/secure/enrollment'/>">Register another Device</a>
	
	<h3><b>Your Registered Devices:</b></h3>

	<c:if test="${displayPhones}">

		<c:if test="${smsSent}">
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert" id="dismissalert">&times;</button>
				<spring:message code = "SmsSentSuccess" />&nbsp${smsPhoneNumber}
			</div>

		</c:if>


		<table border="1" cellpadding="10">
			<caption>Phones:</caption>
			<thead>
				<tr>
					<th>Number</th>
					<th>Platform(OS)</th>
					<th>Type</th>
					<!--<th>Active?</th>-->
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${DuoPerson.phones}" var="phone" varStatus="i">
					<form:form method="post" commandName="DuoPerson">
						<tr>
							<td><c:out value="${phone.phoneNumber}"/></td>
							<td><c:out value="${phone.platform}"/></td>
							<td><c:out value="${phone.type}"/></td>
<!--												<td><c:out value="${phone.activationStatus}"/></td>-->
							<td>
								<input type="submit" value="Remove" name="removedevice" class="btn btn-mini confirmDelete" id="remove${i.index}"/>
								<c:if test="${phone.capableSMS}">
									<input type="submit" value="Text Passcodes" class="btn btn-mini" name="sendsmscode" id="smspasscode${i.index}"/>
								</c:if>
								<c:if test="${not phone.activationStatus && phone.type == 'Mobile' && phone.platform != 'Unknown'}">
									<input type="submit" value="Activate" class="btn btn-mini confirmActivation" 
										   data-toggle="tooltip" data-placement="top" name="deviceactivation" id="activate${i.index}" 
										   title="Suitable For:Identical replacement phone; Same phone that has been hard-reset; New/Upgraded mobile phone with same Number and OS" />
								</c:if>
								<c:if test="${phone.activationStatus && phone.type == 'Mobile' && phone.capablePush}">
									<input type="submit" value="Re-Activate" class="btn btn-mini confirmActivation" 
										   data-toggle="tooltip" data-placement="top" name="deviceactivation" id="activate${i.index}" 
										   title="Suitable For:Identical replacement phone; Same phone that has been hard-reset; New/Upgraded mobile phone with same Number and OS"/>
								</c:if>
							</td>
						</tr>
						<form:hidden path="choosenDevice" value="${phone.type}" />
						<form:hidden path="phone_id" value="${phone.id}" />
						<form:hidden path="phonenumber" value="${phone.phoneNumber}" />
						<form:hidden path="deviceOS" value="${phone.platform}" />
					</form:form>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${displayTablets}">
		<br><br>
		<table border="1" cellpadding="10">
			<caption>Tablets:</caption>
			<thead>
				<tr>
					<th>Name</th>
					<th>Platform(OS)</th>
					<th>Type</th>
					<!--<th>Active?</th>-->
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${DuoPerson.tablets}" var="tablet" varStatus="i">
					<form:form method="post" commandName="DuoPerson">
						<tr>
							<td><c:out value="${tablet.deviceName}"/></td>
							<td><c:out value="${tablet.platform}"/></td>
							<td><c:out value="${tablet.type}"/></td>
<!--												<td><c:out value="${tablet.activationStatus}"/></td>-->
							<td>
								<input type="submit" value="Remove" name="removedevice" class="btn btn-mini confirmDelete" id="remove${i.index}"/>
								<c:if test="${not tablet.activationStatus && tablet.platform != 'Unknown'}">
									<input type="submit" value="Activate" class="btn btn-mini confirmActivation" 
										   data-toggle="tooltip" data-placement="top" name="deviceactivation" id="activate${i.index}"
										   title="Suitable For:Identical replacement phone; Same phone that has been hard-reset; New/Upgraded mobile phone with same Number and OS"/>
								</c:if>
								<c:if test="${tablet.activationStatus && tablet.capablePush}">
									<input type="submit" value="Re-Activate" class="btn btn-mini confirmActivation" 
										   data-toggle="tooltip" data-placement="top" name="deviceactivation" id="activate${i.index}"
										   title="Suitable For:Identical replacement phone; Same phone that has been hard-reset; New/Upgraded mobile phone with same Number and OS"/>
								</c:if>
							</td>
						</tr>
						<form:hidden path="choosenDevice" value="Tablet" />
						<form:hidden path="phone_id" value="${tablet.id}" />
						<form:hidden path="tabletName" value="${tablet.deviceName}" />
						<form:hidden path="deviceOS" value="${tablet.platform}" />
					</form:form>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${displayTokens}">
		<br><br>
		<c:if test="${resyncsuccess}">
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert" id="dismissalert">&times;</button>
				<spring:message code = "TokenResyncSuccess" />&nbsp${resyncTokenSN}
			</div>

		</c:if>


		<table border="1" cellpadding="10">
			<caption>Tokens:</caption>
			<thead>
				<tr>
					<th>Token Serial Number</th>
					<th>Token Type</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${DuoPerson.tokens}" var="token" varStatus="i">
					<form:form method="post" commandName="DuoPerson">
						<tr>
							<td><c:out value="${token.serial}"/></td>
							<td><c:out value="${token.type}"/></td>
							<td>
								<input type="submit" value="Remove" name="removedevice" class="btn btn-mini confirmDelete" id="remove${i.index}"/>
								<input type="submit" value="Resync Token" name="resynctoken" class="btn btn-mini" id="resync${i.index}"/>
								<input type="hidden" name="resyncAction" value="input" />
							</td>
						</tr>
						<form:hidden path="tokenSerial" value="${token.serial}" />
						<form:hidden path="tokenId" value="${token.id}" />
						<form:hidden path="tokenType" value="${token.type}" />
						<form:hidden path="choosenDevice" value="Token" />
					</form:form>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

</body>
</html>	