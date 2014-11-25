<%-- 
    Document   : DuoRegistration Program
    Created on : Nov 25, 2014, 2:16:54 PM
    Author     : Daniel Yu (danielyu@uchicago.edu)
	Copyright 2014 University of Chicago
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<head>
	<meta charset="iso-8859-1">
	<title>DUO Registration</title>
	<script src="<spring:url value='/resources/js/libs/modernizr.js'/>"></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>
</head>
<body>
	<h1>DUO Device Registration</h1>

	<p><b>What operating system does this mobile phone run?</b></p>

	<form:form method="post" commandName="DuoPerson">
		<label class="radio-inline">
			<form:radiobutton path="deviceOS" value="apple ios"></form:radiobutton>iPhone
			<img src="<spring:url value='/resources/img/icon/phone_iphone.png'/>" class="img-responsive"/>	
		</label>
		<br><br>
		<label class="radio-inline">
			<form:radiobutton path="deviceOS" value="google android"></form:radiobutton>Android
			<img src="<spring:url value='/resources/img/icon/phone_android.png'/>" class="img-responsive"/>
		</label>
		<br><br>
		<label class="radio-inline">
			<form:radiobutton path="deviceOS" value="rim blackberry"></form:radiobutton>Blackberry
			<img src="<spring:url value='/resources/img/icon/phone_bb.png'/>" class="img-responsive"/>	
		</label>
		<br><br>
		<label class="radio-inline">
			<form:radiobutton path="deviceOS" value="windows phone"></form:radiobutton>Windows Phone
			<img src="<spring:url value='/resources/img/icon/phone_windows.png'/>" class="img-responsive"/>	
		</label>
		<br><br>
		<label class="radio-inline">
			<form:radiobutton path="deviceOS" value="unknown"></form:radiobutton>Other (cell phones)
			<img src="<spring:url value='/resources/img/icon/phone_mobile.png'/>" class="img-responsive"/>	
		</label>				
		<br><br><br>

		<input type="submit" class="btn btn-primary" value="Continue" name="enrollsteps"/>
		<input type="hidden" name="_page" value="5" />
		&nbsp&nbsp&nbsp&nbsp
		<input type="submit" class="btn btn-inverse" value="Back" name="back" id="bbutton" />
		<input type="hidden" name="_backpage" value="3" />

	</form:form>

</body>
</html>	