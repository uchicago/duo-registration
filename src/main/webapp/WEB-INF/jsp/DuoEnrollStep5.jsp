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
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>DUO Registration</title>
	<script src="<spring:url value='/resources/js/libs/modernizr.js'/>"></script>
	<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />" ></script>
	<script src="<spring:url value="/resources/js/DuoEnrollSTP5.js" />" ></script>
	<script src="<spring:url value="/resources/js/backfix.min.js" />" ></script>

</head>
<body>
	<h1>DUO Device Registration</h1>

	<h1>Install Duo Mobile App</h1>

	<a id="main-nav"></a>
	<c:if test="${DuoPerson.choosenDevice == 'mobile' && DuoPerson.deviceOS == 'apple ios'}">
		<img src="<spring:url value='/resources/img/iphone-install.png'/>" class="img-responsive" alt="iPhone Install Demo Picture">
	</c:if>	

	<c:if test="${DuoPerson.deviceOS == 'google android'}">
		<img src="<spring:url value='/resources/img/android-install.png'/>" class="img-responsive" alt="Android Install Demo Picture">
	</c:if>

	<c:if test="${DuoPerson.deviceOS == 'windows phone'}">
		<img src="<spring:url value='/resources/img/windowsphone-install.png'/>" class="img-responsive" alt="Windows Phone Install Demo Picture">
	</c:if>

	<c:if test="${DuoPerson.choosenDevice == 'tablet' && DuoPerson.deviceOS == 'apple ios'}">
		<img src="<spring:url value='/resources/img/iPad-install.gif'/>" class="img-responsive" alt="iPad Install Demo Picture">
	</c:if>		

	<img src="<spring:url value='/resources/img/blackberry10-install.png'/>" class="img-responsive" alt="BlackBerry Install Demo Picture" id="bb10pic">	
	<img src="<spring:url value='/resources/img/blackberry-install.png'/>" class="img-responsive" alt="BlackBerry Install Demo Picture" id="bbpic">	


	<c:if test="${DuoPerson.choosenDevice == 'mobile' && DuoPerson.deviceOS == 'apple ios'}">
		<P><b>Install Duo Mobile for iOS</b></p>

		<ol>
			<li>Launch the App Store application on your device and search for "Duo Mobile."</li>
			<li>Tap "Free" and then "Install" to download the app.</li>
		</ol>

		<P><b>Note: Tap "OK" when asked if Duo Mobile should be able to send push notifications.</b></P>

	</c:if>

	<c:if test="${DuoPerson.choosenDevice == 'tablet' && DuoPerson.deviceOS == 'apple ios'}">
		<P><b>Install Duo Mobile for Apple iPad</b></p>

		<ol>
			<li>Launch the App Store application on your device and search for "Duo Mobile."</li>
			<li>If "Duo Mobile" app is not listed in the result, please change the search filter from iPad Only to iPhone Only </li>
			<li>Tap "Free" and then "Install" to download the app.</li>
		</ol>

		<P><b>Note: Tap "OK" when asked if Duo Mobile should be able to send push notifications.</b></P>

	</c:if>	



	<c:if test="${DuoPerson.deviceOS == 'google android'}">
		<P><b>Install Duo Mobile for Android</b></p>

		<ol>
			<li>Launch the Google Play Store app and search for "Duo Mobile."</li>
			<li>Tap "Install" to install the app.</li>
		</ol>

	</c:if>

	<c:if test="${DuoPerson.deviceOS == 'windows phone'}">
		<P><b>Install Duo Mobile for Windows Phone</b></p>

		<ol>
			<li>Search for "Duo Mobile" in the store.</li>
			<li>Tap "Install" to install the app.</li>
		</ol>

	</c:if>

	<c:if test="${DuoPerson.deviceOS == 'rim blackberry'}">
		<P><b>Install Duo Mobile for BlackBerry</b></p>
		<p>Choose Your BlackBerry Platform</p>

		<div class="accordion" id="accordion1">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion1" href="#collapseOne" id="bb10selected">
						BlackBerry 10 (Z10, Q10, etc.)
					</a>
				</div>
				<div id="collapseOne" class="accordion-body collapse">
					<div class="accordion-inner">
						<ol>
							<li>Search for "Duo Mobile for BlackBerry 10" in BlackBerry World.</li>
							<li>Tap "Install" to install the app.</li>
						</ol>
					</div>
				</div>
			</div>
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion1" href="#collapseTwo" id="bbselected">
						BlackBerry OS (devices running version 7.1 and earlier)
					</a>
				</div>
				<div id="collapseTwo" class="accordion-body collapse">
					<div class="accordion-inner">
						<ol>
							<li>Search for "Duo Mobile" in BlackBerry World.</li>
							<li>Tap "Install" to install the app.</li>
						</ol>
					</div>
				</div>
			</div>
		</div>

	</c:if>

	<br>

	<label class="checkbox">
		<input type="checkbox" id="appinstallconfirm" />I have the Duo Mobile App installed on my device
	</label>

	<br>

	<!--BELOW!!! Need to Set Action Here to make sure it hits the right URL since a Cross Controller will be calling a specific URL which will make the 
	submit not working, for example, /secure/enrollment/deviceReactivation is use to land to this page, when submit without the action, it will stay with
	this URL which obviously won't work.-->

	<spring:url var = "gotourl" value='/secure/enrollment' />
	<form:form method="post" commandName="DuoPerson" action="${gotourl}">
		<input type="submit" class="btn btn-success" value="Continue" name="enrollUserNPhone" id="registerbtn"/>
		&nbsp&nbsp&nbsp&nbsp

		<c:if test="${not devReActivate}">
			<c:if test="${DuoPerson.choosenDevice == 'tablet'}">
				<input type="submit" class="btn btn-inverse"  value="Back" name="back" id="bbutton" />
				<input type="hidden" name="_backpage" value="31" />
			</c:if>

			<c:if test="${DuoPerson.choosenDevice != 'tablet'}">
				<input type="submit" class="btn btn-inverse"  value="Back" name="back" id="bbutton" />
				<input type="hidden" name="_backpage" value="4" />
			</c:if>
		</c:if>

	</form:form>

</body>
</html>	