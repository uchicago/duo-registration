var mav = '<c:out value="${devReActivate}"/>';
alert(mav);
bajb_backdetect.OnBack = function(e) {

	if (mav == "true") {
		alert("Please Do Not Use the Browser Back Button, you will corrupt your registration data!");
	} else {
		document.getElementById('bbutton').click();
	}

};
