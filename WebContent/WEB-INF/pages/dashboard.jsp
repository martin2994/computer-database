<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tagLibs/mylib.tld" prefix="mylib"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><spring:message code="application.name" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">

<c:set var="currentPath" value="${pageContext.request.contextPath}"></c:set>

<!-- Bootstrap -->
<link href="${currentPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="${currentPath}/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="${currentPath}/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<!-- <p>${fn:length(page)}</p> -->
	<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<a class="navbar-brand" href="${currentPath}/computer"> <spring:message code="application.title" /></a>
	</div>
	</header>

	<section id="main">
	<div class="container">
		<h1 id="homeTitle">${nbComputers}&nbsp<spring:message code="dashboard.text.found"/></h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<form id="searchForm" action="computer" method="GET"
					class="form-inline">

					<input type="search" id="searchbox" name="search"
						class="form-control" placeholder="Search name"
						value="${searchParam}" /> <input type="submit" name="buttonTest"
						id="searchsubmit" value="Filter by name" class="btn btn-primary" />
				</form>
			</div>
			<div class="pull-right">
				<a class="btn btn-success" id="addComputer" href="${currentPath}/computer/add"><spring:message code="dashboard.button.add"/></a> <a class="btn btn-default" id="editComputer" href="#"
					onclick="$.fn.toggleEditMode();"><spring:message code="dashboard.button.edit"/></a>
			</div>
		</div>
	</div>

	<form id="deleteForm" action="${currentPath}/computer/delete" method="POST">
		<input type="hidden" name="selection" id="selection" value=""/>
	</form>

	<div class="container" style="margin-top: 10px;">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<!-- Variable declarations for passing labels as parameters -->
					<!-- Table header for Computer Name -->

					<th class="editMode" style="width: 60px; height: 22px;"><input
						type="checkbox" id="selectall" /> <span
						style="vertical-align: top;"> - <a href="#"
							id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
								class="fa fa-trash-o fa-lg" id="trash"></i>
						</a>
					</span></th>
					<th><spring:message code="label.text.computerName"/></th>
					<th><spring:message code="label.text.introduced"/></th>
					<!-- Table header for Discontinued Date -->
					<th><spring:message code="label.text.discontinued"/></th>
					<!-- Table header for Company -->
					<th><spring:message code="label.text.company"/></th>

				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody id="results">
				<c:forEach items="${page}" var="computer">
					<tr>
						<td class="editMode"><input type="checkbox" name="cb" id="name"
							class="cb" value="${computer.id}"></td>
						<td><a href="computer/${computer.id}" onclick="">${computer.name}</a></td>
						<td>${computer.introduced}</td>
						<td>${computer.discontinued}</td>
						<td>${computer.manufacturer}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</section>
	<footer class="navbar-fixed-bottom">
	<div class="container text-center">

		<mylib:pagination uri="computer" search="${searchParam}"
			elementPerPage="${resultPerPage}" currentPage="${currentPage}"
			numberOfElement="${nbComputers}" />

		<div class="btn-group btn-group-sm pull-right" role="group">
			<a class="btn btn-default"
				href=<mylib:link uri="${currentPath}/computer" search="${searchParam}" page="${currentPage}" count="10"/>>10</a>
			<a class="btn btn-default"
				href=<mylib:link uri="${currentPath}/computer" search="${searchParam}" page="${currentPage}" count="50"/>>50</a>
			<a class="btn btn-default"
				href=<mylib:link uri="${currentPath}/computer" search="${searchParam}" page="${currentPage}" count="100"/>>100</a>
		</div>
	</div>
	</footer>
	<script src="${currentPath}/js/jquery.min.js"></script>
	<script src="${currentPath}/js/bootstrap.min.js"></script>
	<script src="${currentPath}/js/dashboard.js"></script>
</body>
</html>