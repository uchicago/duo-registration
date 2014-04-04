<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix = "spring" uri = "http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Enrollment</title>
    </head>
    <body>
        <h1>Duo Enrollment</h1>
		
		<c:if test="${existingUser}">
			<h1><spring:message code = "UserExist" /></h1>
		</c:if>
		
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td>Choose Your Authenticator:</td>
					<td></td>
				</tr>
				<tr>  
					<td>Mobile phone RECOMMENDED</td>  
					<td><form:radiobutton path="choosenDevice" value="mobile"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Tablet (iPad, Nexus 7, etc.)</td>  
						<td><form:radiobutton path="choosenDevice" value="tablet"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Landline</td>  
						<td><form:radiobutton path="choosenDevice" value="landline"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Token</td>  
						<td><form:radiobutton path="choosenDevice" value="token"></form:radiobutton></td>  
					</tr>
				</table>
				<input type="submit" value="Next" name="enrollsteps"/>
				<input type="hidden" name="_page" value="3" />
		</form:form>
    </body>
</html>
