<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Enrollment Success</title>
    </head>
    <body>
        <h1>Duo Enrollment Success!</h1>
		<form:form method="post" commandName="DuoPerson">
			<h2>Phone Number: ${DuoPerson.phonenumber}</h2>
			<h2>Username: ${DuoPerson.username}</h2>
			<h2>User Info: ${userinfo}</h2>
			<h2>Choosen Device: ${DuoPerson.choosenDevice}</h2>
			<h2>Device OS: ${DuoPerson.deviceOS}</h2>
			<input type="submit" value="DONE" name="reset"/>
			<input type="submit" value="Enroll Another Device" name="enrollsteps"/>
			<input type="hidden" name="_page" value="0" />
		</form:form>
    </body>
</html>
