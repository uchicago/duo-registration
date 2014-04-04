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
		<h2>Your User Name</h2>
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td><form:label path="username">UserName</form:label></td>
					<td><form:input path="username" /></td>
				</tr>
				<tr>
					<td><form:label path="fullName">Full Name</form:label></td>
					<td><form:input path="fullName" /></td>
				</tr>
				<tr>
					<td><form:label path="email">Email Address</form:label></td>
					<td><form:input path="email" /></td>
				</tr>
			</table>
			<input type="submit" value="Next" name="enrollsteps"/>
			<input type="hidden" name="_page" value="2" />
		</form:form>
    </body>
</html>
