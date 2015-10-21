<%-- 
    Document   : DuoRegistration Program
    Created on : Oct 19, 2015, 2:21:00 PM
    Author     : Brandon Gresham (brandon.gresham@utah.edu)
	Copyright 2015 University of Utah
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
	<meta charset="iso-8859-1">
	<title>2Factor Authentication | The University of Chicago</title>
	<script src="<spring:url value='/resources/js/libs/modernizr.js'/>"></script>
	<script src="<spring:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
</head>
<body>

	<h1>DUO Portal: Helpdesk</h1>

	<h2><b>Helpdesk User: ${DuoPeople.duoHelpdeskPerson.fullName}</b></h2>
	
	<h2>Generate Bypass-Code for Another User</h2>
	
	<form:form method="post" commandName="DuoPeople">
	
		<form:input placeholder="Chicago ID" id="accountSearch" path="duoEnduserPerson.chicagoID"/>
		<input type="submit" value="Search" name="unidsearch"/>
				
		<c:if test="${DuoPeople.duoEnduserPerson.chicagoID != null}">
			<c:set var="endUser" value="${DuoPeople.duoEnduserPerson}"/>
			
			<h3>End-User Verification</h3>			
			<ul>
				<li>Chicago ID: ${endUser.chicagoID}</li>
				<li>Fullname: ${endUser.fullName}</li>
				<li>Email: ${endUser.email}</li>
				<li>Primary Phone Number: ${endUser.phonenumber}</li>
			</ul>
			
			<c:choose>
				<c:when test="${DuoEndUserPerson_bypasscode eq null}">
					<input type="submit" value="Generate Bypass-Code" name="genbypasscode" />
				</c:when>
				<c:otherwise>
					<h3>Bypass Code: ${DuoEndUserPerson_bypasscode}</h3>
				</c:otherwise>
			</c:choose>			
		</c:if>
	
	</form:form>

</body>
</html>	