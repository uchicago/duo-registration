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
		<h2>Device Operating System</h2>
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td>Choose Platform:</td>
					<td>What operating system does this device run?</td>
				</tr>
				<tr>  
					<td>iPhone</td>  
					<td><form:radiobutton path="deviceOS" value="apple ios"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Android</td>  
						<td><form:radiobutton path="deviceOS" value="google android"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>BlackBerry</td>  
						<td><form:radiobutton path="deviceOS" value="rim blackberry"></form:radiobutton></td>  
					</tr>  
					<tr>  
						<td>Windows Phone</td>  
						<td><form:radiobutton path="deviceOS" value="windows phone"></form:radiobutton></td>  
					</tr>
					<tr>  
						<td>Other (and cell phones)</td>  
						<td><form:radiobutton path="deviceOS" value="unknown"></form:radiobutton></td>  
					</tr>
				</table>
				<input type="submit" value="Next" name="enrollUserNPhone"/>
		</form:form>
    </body>
</html>
