<%-- 
    Document   : DuoRegistration Program
    Created on : Nov 25, 2014, 2:16:54 PM
    Author     : Daniel Yu (danielyu@uchicago.edu)
	Copyright 2014 University of Chicago
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Duo Tablet Enrollment</title>
    </head>
    <body>
        <h1>Duo Tablet Enrollment</h1>
		<form:form method="post" commandName="DuoPerson">
			<table>
				<tr>
					<td colspan="2">
						<form:label class="control-label" path="tabletName"><b>What operating system does this tablet run?</b></form:label>
						</td>
					</tr>
					<tr>
						<td>

						<form:select path="deviceOS">
							<form:option value="" label="--- Select ---"/>
							<form:options items="${tabletOSList}" />
						</form:select>	

					</td>
					<td>
						<div class="control-group error">
							<form:errors path="deviceOS" class="help-inline" />
						</div>
					</td>
				</tr>

				<tr><td colspan="2">&nbsp;</td></tr>

				<tr>
					<td colspan="2">
						<form:label class="control-label" path="tabletName"><b>Please give your tablet a name:</b></form:label>
						</td>
					</tr>
					<tr>
						<td><form:input path="tabletName" maxlength="50"/></td>
					<td>
						<div class="control-group error">
							<form:errors path="tabletName" class="help-inline" />
						</div>
					</td>
				</tr>


			</table>

			<input type="submit" value="Continue" name="enrollsteps"/>
			&nbsp&nbsp&nbsp&nbsp
			<input type="submit" value="Back" name="back" />
			<input type="hidden" name="_backpage" value="2" />
			<input type="hidden" name="_page" value="31" />


		</form:form>
    </body>
</html>
