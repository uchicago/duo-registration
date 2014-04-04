<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Enrollment</title>
    </head>
    <body>
        <h1>Duo Enrollment</h1>
		<h2>Your Phone Number</h2>
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td><form:label path="phonenumber">Phone Number</form:label></td>
					<td><form:input path="phonenumber" /></td>
					<td><form:errors path="phonenumber" cssclass="error" /></td>
				</tr>
			</table>
			<input type="submit" value="Next" name="enrollsteps"/>
			<input type="hidden" name="_page" value="4" />
		</form:form>
    </body>
</html>
