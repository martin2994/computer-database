<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Bootstrap -->
<link href="/cdb/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/cdb/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="/cdb/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<a class="navbar-brand" href="/cdb/computer"> Application - Computer
			Database </a>
	</div>
	</header>

	<section id="main">
	<div class="container">
		<div class="row">
			<div class="container">
				<c:if test="${!empty erreur}">
					<div class="alert alert-danger">${erreur}</div>
				</c:if>
				<c:if test="${!empty message}">
					<div id="success" class="alert alert-success">${message}</div>
				</c:if>
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<form:form modelAttribute="computer" name="computerForm" id="computerForm"
						action="/cdb/computer/add" method="POST">
						<fieldset>
							<div class="form-group">
								<form:label path="name" for="computerName">Computer name</form:label> <form:input
									type="text" class="form-control" path="name" name="computerName"
									placeholder="Computer name" required="true"/>
							</div>
							<div class="form-group">
								<form:label path="introduced" for="introduced">Introduced date</form:label> <form:input
									type="date" class="form-control" path="introduced" id="introduced"
									name="introduced" placeholder="Introduced date"/>
							</div>
							<div class="form-group">
								<form:label path="discontinued" for="discontinued">Discontinued date</form:label> <form:input
									type="date" class="form-control" path="discontinued" id="discontinued"
									name="discontinued" placeholder="Discontinued date"/>
							</div>
							<div class="form-group">
								<form:label path="manufacturerId" for="companyId">Company</form:label> <form:select
									class="form-control" path="manufacturerId" name="companyId">
									<option value="0">No company</option>
									<c:forEach items="${companies}" var="company">
										<option value="${company.id}">${company.name}</option>
									</c:forEach>
								</form:select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" id="buttonAdd" name="buttonTest" value="Add"
								class="btn btn-primary"> or <a href="/cdb/computer"
								class="btn btn-default">Cancel</a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
	<script src="/cdb/js/jquery.min.js"></script>
	<script
		src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
	<script src="/cdb/js/bootstrap.min.js"></script>
	<script src="/cdb/js/validation.js"></script>
</body>
</html>