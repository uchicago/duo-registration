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
	<title>DUO Token Resync</title>
	<script src="<spring:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
	<script src="<spring:url value="/resources/js/DuoResyncTkn.js" />" ></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
</head>
<body>

	<h1>DUO Portal: Token Resync</h1>


	<form:form method="post" commandName="DuoPerson">
		<h3>Your Token Information:</h3>
		<p>
			Token Type:&nbsp<b>${DuoPerson.tokenType}</b><br>
			Token Serial Number:&nbsp<b>${DuoPerson.tokenSerial}</b>
		</p>

		<table>
			<tr>
				<td colspan="3">
					<form:label class="control-label" path="tokenSerial">
						<b>Generate three passcodes in a row and enter them here to resync this hardware token.</b>
					</form:label>
				</td>
			</tr>
			<tr><td colspan="3">&nbsp;</td></tr>
			<tr>
				<td><form:label path="tokenSyncCode1">1st code</form:label></td>
				<td><form:input path="tokenSyncCode1" class="tokenboxes" maxlength="6"/></td>
				<td>
					<div class="control-group error">
						<form:errors path="tokenSyncCode1" class="help-inline" />
					</div>
				</td>
			</tr>

			<tr>
				<td><form:label path="tokenSyncCode2">2nd code</form:label></td>
				<td><form:input path="tokenSyncCode2" class="tokenboxes" maxlength="6" /></td>
				<td>
					<div class="control-group error">
						<form:errors path="tokenSyncCode2" class="help-inline" />
					</div>
				</td>
			</tr>

			<tr>
				<td><form:label path="tokenSyncCode3">3rd code</form:label></td>
				<td><form:input path="tokenSyncCode3" class="tokenboxes" maxlength="6"/></td>
				<td>
					<div class="control-group error">
						<form:errors path="tokenSyncCode3" class="help-inline" />
					</div>
				</td>
			</tr>


		</table>

		<input type="submit" class="btn btn-primary" value="Resync Token" name="resynctoken"/>
		&nbsp&nbsp&nbsp&nbsp
		<input type="submit" class="btn btn-inverse"  value="Cancel" name="cancel" />
		<input type="hidden" value="resync" name="resyncAction" />

	</form:form>
</body>
</html>	