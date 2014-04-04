<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Token Enrollment</title>
    </head>
    <body>
        <h1>Duo Token Enrollment</h1>
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td>Token Type: </td>
					<td>
						<form:select path="tokenType">
							<form:option value="NONE" label="--- Select ---"/>
							<form:options items="${tokenTypeList}" />
						</form:select>
					</td>
					<td><form:errors path="tokenType" cssClass="error" /></td>
				</tr>

				<tr>
					<td><form:label path="tokenSerial">Token Serial Number:</form:label></td>
					<td><form:input path="tokenSerial" /></td>
					<td><form:errors path="tokenSerial" cssclass="error" /></td>
				</tr>
				<tr><td><br></td></tr>
			</table>
			<input type="submit" value="Register" name="enrollUserNPhone"/>
		</form:form>
    </body>
</html>
