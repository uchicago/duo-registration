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
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td><form:label path="username">Username</form:label></td>
					<td><form:input path="username" /></td>
				</tr>
				<tr>
					<td><form:label path="phonenumber">Phone Number</form:label></td>
					<td><form:input path="phonenumber" /></td>
				</tr>
				

				<tr><td></td><td></td></tr>
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
					
					<tr><td></td><td></td></tr>
				<tr>
					<td>Choose Platform:</td>
					<td>What operating system does this device run?</td>
				</tr>
				<tr>  
					<td>iPhone</td>  
					<td><form:radiobutton path="deviceOS" value="iPhone"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Android</td>  
						<td><form:radiobutton path="deviceOS" value="Android"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>BlackBerry</td>  
						<td><form:radiobutton path="deviceOS" value="BlackBerry"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Windows Phone</td>  
						<td><form:radiobutton path="deviceOS" value="Windows Phone"></form:radiobutton></td>  
					</tr>
					<tr>  
						<td>Other (and cell phones)</td>  
						<td><form:radiobutton path="deviceOS" value="Other"></form:radiobutton></td>  
					</tr>


					<tr>
						<td colspan="2">
							<input type="submit" value="Add User Information" name="AddUser"/>
						</td>
					</tr>
				</table>
		</form:form>
    </body>
</html>
