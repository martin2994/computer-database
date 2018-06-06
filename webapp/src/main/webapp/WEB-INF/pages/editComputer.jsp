<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><spring:message code="application.name" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<c:set var="currentPath" value="${pageContext.request.contextPath}"></c:set>

<!-- Bootstrap -->
<link href="${currentPath}/resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="${currentPath}/resources/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="${currentPath}/resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<a class="navbar-brand" href="${currentPath}/computer"> <spring:message
				code="application.title" /></a>
		<form action="${currentPath}/logout" method="post">
		<input class="btn btn-link navbar-brand navbar-right" value="Logout" type="submit">
	</form>
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
				<h1><spring:message code="editComputer.text.title"/></h1>

				<form:form action="${currentPath}/computer/${computer.id}" modelAttribute="computer" id="computerForm" name="computerForm"
					method="POST">
					<form:input type="hidden" path="id" value="${computer.id}" id="idComputer"
						name="idComputer" />
					<fieldset>
							<div class="form-group">
								<form:label path="name" for="computerName">
									<spring:message code="label.text.computerName" />
								</form:label>
								<form:input type="text" class="form-control" path="name"
									name="computerName" id="name" placeholder="Computer name" />
									<label hidden=true id="nameProblem" class="error text-danger" required=true><spring:message code="text.javascript.required"/></label>
							</div>
						<div class="form-group">
							<form:label path="introduced" for="introduced"><spring:message code="label.text.introduced"/></form:label> <form:input
								type="date" class="form-control" path="introduced" id="introduced"
								name="introduced" placeholder="Introduced date"
								value="${computer.introduced}"/>
						</div>
						<div class="form-group">
							<form:label path="discontinued" for="discontinued"><spring:message code="label.text.discontinued"/></form:label> <form:input
								type="date" class="form-control" path="discontinued" id="discontinued"
								name="discontinued" placeholder="Discontinued date"
								value="${computer.discontinued}" min="${computer.introduced}"/>
						</div>
						<div class="form-group">
							<form:label path="manufacturerId" for="companyId"><spring:message code="label.text.company"/></form:label> 
							<form:select
								class="form-control" path="manufacturerId" name="companyId"
								value="${computer.manufacturerId}">
								<option value="0"><spring:message code="text.noCompany"/></option>
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
							</form:select>
						</div>
					</fieldset>
					<div class="actions pull-right">
						<input type="submit" name="buttonTest" id="buttonEdit"
							value="<spring:message code='text.edit'/>" class="btn btn-primary"> <spring:message code="text.or"/> <a
							href="${currentPath}/computer" class="btn btn-default"> <spring:message code="text.cancel"/></a>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	</section>
	<script src="${currentPath}/resources/js/jquery.min.js"></script>
	<script src="${currentPath}/resources/js/bootstrap.min.js"></script>
	<script src="${currentPath}/resources/js/validation.js"></script>
</body>
</html>