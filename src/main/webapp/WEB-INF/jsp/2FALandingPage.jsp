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
	<title>2Factor Authentication</title>
</head>
<body>
	<h1 class="page-title">Welcome to DUO Registration Portal (Landing Page)</h1>

	<h2><b>Protect Your University Account</b></h2>

	<p>2Factor Authentication (2FA) enhances the security of your UserID by using your phone to verify your identity. 
		<br>This prevents anyone but you from using your account to log in to University websites, even if they know your UserID password.
	</p>

	<a href="<spring:url value='/'/>">Home</a><br>

	<h4>Both actions below go to the same place:</h4>

	<a href="<spring:url value='/secure'/>">Go to 2Factor (Hyperlink)</a>
	<br><br>

	<form:form method="post" commandName="DuoPerson">
		<input type="submit" value="Go to 2Factor (Button)" name="wheretogo"/>
		<input type="hidden" name="_destination" value="portal" />
	</form:form>

</body>	
