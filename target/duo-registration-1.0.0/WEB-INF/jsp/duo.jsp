<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DUO Enrollment Portal</title>
    </head>

    <body>
        <h1>DUO Enrollment Portal</h1><br>
<!--		<h1>${duotest.adminkeys}</h1>-->
		
		<c:out value="${duotest.adminkeys.hostkey}"/><br><br>

		<c:out value="Server Time = ${duotest.mintime}"/><br><br>

		<c:out value="${duotest.userinfo}"/><br><br>	

		<c:out value="${duotest.username}"/><br><br>

		<c:out value="${DuoPerson.phonenumber}"/><br><br>

<!--		<a href="<c:url value="enrollment.htm"/>">Duo Enrollment</a>
		<br>-->
		<spring:url value="/enrollment" var="enroll" />
		<a href="${enroll}">Duo Enrollment Portal(Spring Tag)</a>
		


</body>
</html>




<%--<c:forEach items="${model.products}" var="prod">
			<c:out value="${prod.description}"/> <i>$<c:out value="${prod.price}"/></i><br><br>
		</c:forEach>--%>