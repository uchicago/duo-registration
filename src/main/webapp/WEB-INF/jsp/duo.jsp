<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO JSP Controller</title>
    </head>

    <body>
        <p>DUO JSP Controller</p><br>
		<h1>${duotest.adminkeys}</h1>
		<c:out value="${duotest.adminkeys.ikey}"/><br><br>
		<c:out value="${duotest.adminkeys.skey}"/><br><br>
		<c:out value="${duotest.adminkeys.hostkey}"/><br><br>
		
		<c:out value="Server Time = ${duotest.mintime}"/><br><br>
		
		<c:out value="${duotest.userinfo}"/><br><br>	
		
	</body>
</html>




<%--<c:forEach items="${model.products}" var="prod">
			<c:out value="${prod.description}"/> <i>$<c:out value="${prod.price}"/></i><br><br>
		</c:forEach>--%>