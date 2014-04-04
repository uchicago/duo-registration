<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO</title>
    </head>

    <body>
        <p>DUO Test !!!</p>
		<c:out value="${duointegrations.adminikeys.host}"/><br>
		<c:out value="${duointegrations.adminikeys.ikey}"/><br>
		<c:out value="${duointegrations.adminikeys.skey}"/><br>
	</body>
</html>
