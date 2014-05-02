<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('#dialCode').change(function() {
					var code = $('#dialCode').val();
					$('#dialcode').html("Country Dial Code:" + code);
				});

				$('#dialcode').html("Country Dial Code:" + $('#dialCode').val());

			});

		</script>


		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Enrollment</title>
    </head>
    <body>
        <h1>Duo Enrollment</h1>
		<h2>Your Phone Number</h2>
		<form:form method="post" commandName="DuoPerson">

			<form:select path="countryDialCode" id="dialCode">
				<form:options items="${countryDialList}" />
			</form:select>	

			<p id="dialcode"></p>

			<table>
				<tr>
					<td><form:label path="phonenumber">Phone Number</form:label></td>
					<td><form:input path="phonenumber" /></td>
					<td><form:errors path="phonenumber" cssclass="error" /></td>
				</tr>
				<c:if test="${DuoPerson.choosenDevice == 'landline'}">
					<tr>
						<td><form:label path="landLineExtension">Extension</form:label></td>
						<td><form:input path="landLineExtension" /></td>
						<td><form:errors path="landLineExtension" cssclass="error" /></td>
					</tr>
				</c:if>

			</table>
			<input type="submit" value="Next" name="enrollsteps"/>
			<input type="hidden" name="_page" value="4" />
		</form:form>
    </body>
</html>
