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
	<script src="<spring:url value='/resources/js/libs/modernizr.js'/>"></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
</head>
<body>

	<h1>DUO Device Registration - Success</h1>

	<c:if test="${DuoPerson.choosenDevice == 'mobile'}">
		<h4><b>You have successfully registered your mobile phone in DUO!</b></h4><br>
		<b>Phone:</b>
	</c:if>
	<c:if test="${DuoPerson.choosenDevice == 'tablet'}">
		<h4><b>You have successfully registered your tablet in DUO!</b></h4><br>
		<b>Tablet:</b>
	</c:if>
	<c:if test="${DuoPerson.choosenDevice == 'landline'}">
		<h4><b>You have successfully registered your landline phone in DUO!</b></h4><br>
		<b>Landline Phone:</b>
	</c:if>
	<c:if test="${DuoPerson.choosenDevice == 'token'}">
		<h4><b>You have successfully registered your token in DUO!</b></h4><br>
		<b>Token:</b>
	</c:if>
	<br><br>	
	<table border="1">
		<tr>
			<c:if test="${DuoPerson.choosenDevice == 'mobile' || DuoPerson.choosenDevice == 'landline' }">
				<th align="center">Number</th>
				</c:if>

			<c:if test="${DuoPerson.choosenDevice == 'tablet'}">
				<th align="center">Name</th>
				</c:if>

			<c:if test="${DuoPerson.choosenDevice == 'token'}">
				<td align="center">Serial #</td>
			</c:if>		

			<c:if test="${DuoPerson.choosenDevice == 'mobile'}">
				<th align="center">Platform</th>
				</c:if>

			<c:if test="${DuoPerson.choosenDevice == 'landline' && not empty DuoPerson.landLineExtension}">
				<th align="center">Extension</th>	
				</c:if>	

			<th align="center">Type</th>
			<th align="center">Active?</th>
		</tr>
		<tr>
			<c:if test="${DuoPerson.choosenDevice == 'mobile' || DuoPerson.choosenDevice == 'landline' }">
				<td align="center">${DuoPerson.phonenumber}</td>
			</c:if>

			<c:if test="${DuoPerson.choosenDevice == 'tablet'}">
				<td align="center">${DuoPerson.tabletName}</td>
			</c:if>

			<c:if test="${DuoPerson.choosenDevice == 'token'}">
				<td align="center">${DuoPerson.tokenSerial}</td>
			</c:if>	

			<c:if test="${DuoPerson.choosenDevice == 'mobile'}">
				<td align="center">${DuoPerson.deviceOS}</td>
			</c:if>

			<c:if test="${DuoPerson.choosenDevice == 'landline' && not empty DuoPerson.landLineExtension}">
				<td align="center">${DuoPerson.landLineExtension}</td>
			</c:if>		

			<c:if test="${DuoPerson.choosenDevice != 'token'}">
				<td align="center">${DuoPerson.choosenDevice}</td>
			</c:if>	

			<c:if test="${DuoPerson.choosenDevice == 'token'}">
				<td align="center">${DuoPerson.tokenType}</td>
			</c:if>	

			<td align="center">${deviceActive}</td>
		</tr>
	</table>
	<br>
	<form:form method="post" commandName="DuoPerson">	
		<input type="submit" class="btn btn-info btn-block" value="Enroll Another Device" name="enrollsteps"/>
		<br><br>
		<div class="text-center">
			<input type="submit" class="btn btn-success btn-small" value="DONE" name="reset"/>
		</div>
		<input type="hidden" name="_page" value="0" />
	</form:form>

</body>
</html>	