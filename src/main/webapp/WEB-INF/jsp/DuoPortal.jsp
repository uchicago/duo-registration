<%-- 
    Document   : DuoRegistration Program
    Created on : Nov 25, 2014, 2:16:54 PM
    Author     : Daniel Yu (danielyu@uchicago.edu)
	Copyright 2014 University of Chicago
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO Registration Portal</title>
    </head>

    <body>
        <h1>DUO Registration</h1>
		
		<h3>Welcome!</h3>
		User's Fullname: ${DuoPerson.fullName} <br><br>
		User's Email Address: ${DuoPerson.email} <br>

		<br>
		<form:form method="post" commandName="DuoPerson">
			<input type="submit" value="Register a Device for DUO" name="wheretogo"/>
			<input type="hidden" name="_destination" value="register" />
		</form:form>

		<br>

		<form:form method="post" commandName="DuoPerson">
			<input type="submit" value="Manage Devices Registered in DUO" name="wheretogo"/>
			<input type="hidden" name="_destination" value="devicemgmt" />
		</form:form>

	</body>
</html>
