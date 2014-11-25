<%-- 
    Document   : DuoRegistration Program
    Created on : Nov 25, 2014, 2:16:54 PM
    Author     : Daniel Yu (danielyu@uchicago.edu)
	Copyright 2014 University of Chicago
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
		<script src="<spring:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
		<script src="<spring:url value="/resources/js/DuoEnrollSTP3.js" />" ></script>
		<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO Registration</title>
    </head>
    <body>
        <h1>DUO Device Registration</h1>
		<h2>Your Phone Number</h2>
		<form:form method="post" commandName="DuoPerson">

			<form:select path="countryDialCode" id="dialCode">
				<form:options items="${countryDialList}" />
			</form:select>	

			<p id="dialcode"></p>

			<table>
				<tr>
					<td><form:label path="phonenumber">Phone Number</form:label></td>
					<td><form:input path="phonenumber" class="phoneboxes" maxlength="20" /></td>
					<td><form:errors path="phonenumber" cssclass="error" /></td>
				</tr>
				<c:if test="${DuoPerson.choosenDevice == 'landline'}">
					<tr>
						<td><form:label path="landLineExtension">Extension</form:label></td>
						<td><form:input path="landLineExtension" class="phoneboxes" maxlength="10" /></td>
						<td><form:errors path="landLineExtension" cssclass="error" /></td>
					</tr>
				</c:if>

			</table>
			<input type="submit" value="Next" name="enrollsteps"/>
			<input type="submit" value="Back" name="back" />
			<input type="hidden" name="_page" value="4" />
			<input type="hidden" name="_backpage" value="2" />
		</form:form>
    </body>
</html>
