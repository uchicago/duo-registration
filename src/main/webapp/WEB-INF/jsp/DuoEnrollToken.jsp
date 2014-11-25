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
	<title>Duo Token Enrollment</title>
	<script src="<spring:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
	<script src="<spring:url value="/resources/js/DuoEnrollToken.js" />" ></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
</head>
<body>

	<h1>2Factor: Register a Device - Token Type & Number</h1>

	<form:form method="post" commandName="DuoPerson">
		<table>
			<tr>
				<td colspan="2">
					<form:label class="control-label" path="tokenType"><b>What kind of token do you have?</b></form:label>
					</td>
				</tr>
				<tr>
					<td>

					<form:select path="tokenType" id="tokenType">
						<form:option value="d1" label="Duo-D100 hardware token"/>
						<!--													
						<form:option value="" label="--- Select ---"/>
						<form:options items="${tokenTypeList}" />
						-->
					</form:select>	

				</td>
				<td>
					<div class="control-group error">
						<form:errors path="tokenType" class="help-inline" />
					</div>
				</td>
			</tr>

			<tr><td colspan="2">&nbsp;</td></tr>

			<tr>
				<td colspan="2">
					<form:label class="control-label" path="tokenSerial"><b>Please enter the token's serial number:</b></form:label>
					</td>
				</tr>
				<tr>
					<td><form:input path="tokenSerial" class="tokeninputbox" /></td>
				<td>
					<div class="control-group error">
						<form:errors path="tokenSerial" class="help-inline" />
					</div>
				</td>
			</tr>


		</table>

		<input type="submit" class="btn btn-primary" value="Continue" name="enrollsteps"/>
		&nbsp&nbsp&nbsp&nbsp
		<input type="submit" class="btn btn-inverse"  value="Back" name="back" />
		<input type="hidden" name="_backpage" value="2" />
		<input type="hidden" name="_page" value="32" />


	</form:form>

</body>
</html>	