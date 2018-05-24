<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<div class="col-xs-8 col-xs-offset-2 box">
				<div class="label label-default pull-right">id: ${computer.id}</div>
				<c:if test="${!empty erreur}">
					<div class="alert alert-danger">${erreur}</div>
				</c:if>
				<c:if test="${!empty message}">
					<div id="success" class="alert alert-success">${message}</div>
				</c:if>
				<h1>Edit Computer</h1>

				<form action="/cdb/computer/${computer.id}" id="computerForm" name="computerForm"
					method="POST">
					<input type="hidden" value="${computer.id}" id="idComputer"
						name="idComputer" />
					<fieldset>
						<div class="form-group">
							<label for="computerName">Computer name</label> <input
								type="text" class="form-control" id="computerName"
								name="computerName" placeholder="Computer name"
								value="${computer.name}" required>
						</div>
						<div class="form-group">
							<label for="introduced">Introduced date</label> <input
								type="date" class="form-control" id="introduced"
								name="introduced" placeholder="Introduced date"
								value="${computer.introduced}">
						</div>
						<div class="form-group">
							<label for="discontinued">Discontinued date</label> <input
								type="date" class="form-control" id="discontinued"
								name="discontinued" placeholder="Discontinued date"
								value="${computer.discontinued}" min="${computer.introduced}">
						</div>
						<div class="form-group">
							<label for="companyId">Company</label> <select
								class="form-control" name="companyId"
								value="${computer.manufacturerId}">
								<option value="0">No company</option>
								<c:forEach items="${companies}" var="company">
									<c:choose>
										<c:when test="${company.id == computer.manufacturerId}">
											<option value="${company.id}" selected="selected">${company.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${company.id}">${company.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</fieldset>
					<div class="actions pull-right">
						<input type="submit" name="buttonTest" id="buttonEdit"
							value="Edit" class="btn btn-primary"> or <a
							href="/cdb/computer" class="btn btn-default">Cancel</a>
					</div>
				</form>
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