<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Token Resync</title>
    </head>
    <body>
        <h1>Duo Token Resync</h1>
		<form:form method="post" commandName="DuoPerson">
			<h2>Token Type: ${DuoPerson.tokenType}</h2>
			<h2>Token Serial Number ${DuoPerson.tokenSerial}</h2>
			
			<table>
				<tr>
					<th colspan="2">Generate three passcodes in a row and enter them here to resync this hardware token.</th>
				</tr>
				<tr>
					<td><form:label path="tokenSyncCode1">1st code</form:label></td>
					<td><form:input path="tokenSyncCode1" /></td>
					<td><form:errors path="tokenSyncCode1" cssclass="error" /></td>
				</tr>
				<tr>
					<td><form:label path="tokenSyncCode2">2nd code</form:label></td>
					<td><form:input path="tokenSyncCode2" /></td>
					<td><form:errors path="tokenSyncCode2" cssclass="error" /></td>
				</tr>
				<tr>
					<td><form:label path="tokenSyncCode3">3rd code</form:label></td>
					<td><form:input path="tokenSyncCode3" /></td>
					<td><form:errors path="tokenSyncCode3" cssclass="error" /></td>
				</tr>		

			</table>
			
			
			<input type="submit" value="Resync Token" name="resynctoken"/>
			<input type="hidden" value="resync" name="resyncAction" />
			<input type="submit" value="Cancel" name="cancel"/>
		</form:form>
    </body>
</html>
