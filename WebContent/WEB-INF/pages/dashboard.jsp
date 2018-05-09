<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tagLibs/mylib.tld" prefix="mylib"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<!-- <p>${fn:length(page)}</p> -->
	<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<a class="navbar-brand" href="dashboard"> Application - Computer
			Database </a>
	</div>
	</header>

	<section id="main">
	<div class="container">
		<h1 id="homeTitle">${nbComputers}&nbsp;Computers&nbsp;found</h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<form id="searchForm" action="dashboard" method="GET"
					class="form-inline">

					<input type="search" id="searchbox" name="search"
						class="form-control" placeholder="Search name"
						value="${searchParam}" /> <input type="submit" name="buttonTest"
						id="searchsubmit" value="Filter by name" class="btn btn-primary" />
				</form>
			</div>
			<div class="pull-right">
				<a class="btn btn-success" id="addComputer" href="addComputer">Add
					Computer</a> <a class="btn btn-default" id="editComputer" href="#"
					onclick="$.fn.toggleEditMode();">Edit</a>
			</div>
		</div>
	</div>

	<form id="deleteForm" action="dashboard" method="POST">
		<input type="hidden" name="selection" value="">
	</form>

	<div class="container" style="margin-top: 10px;">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<!-- Variable declarations for passing labels as parameters -->
					<!-- Table header for Computer Name -->

					<th class="editMode" style="width: 60px; height: 22px;"><input
						type="checkbox" id="selectall" /> <span
						style="vertical-align: top;"> - <a href="dashboard"
							id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
								class="fa fa-trash-o fa-lg"></i>
						</a>
					</span></th>
					<th>Computer name</th>
					<th>Introduced date</th>
					<!-- Table header for Discontinued Date -->
					<th>Discontinued date</th>
					<!-- Table header for Company -->
					<th>Company</th>

				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody id="results">
				<c:forEach items="${page}" var="computer">
					<tr>
						<td class="editMode"><input type="checkbox" name="cb"
							class="cb" value="${computer.id}"></td>
						<td><a href="editComputer?id=${computer.id}" onclick="">${computer.name}</a></td>
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

		<c:url var="myURLPage" value="dashboard">
			<c:param name="page" value="${currentPage}" />
		</c:url>

		<mylib:pagination uri="dashboard" search="${searchParam}"
			elementPerPage="${resultPerPage}" currentPage="${currentPage}"
			numberOfElement="${nbComputers}" />

		<div class="btn-group btn-group-sm pull-right" role="group">
			<a class="btn btn-default"
				href=<mylib:link uri="dashboard" search="${searchParam}" page="${currentPage}" count="10"/>>10</a>
			<a class="btn btn-default"
				href=<mylib:link uri="dashboard" search="${searchParam}" page="${currentPage}" count="50"/>>50</a>
			<a class="btn btn-default"
				href=<mylib:link uri="dashboard" search="${searchParam}" page="${currentPage}" count="100"/>>100</a>
		</div>
	</div>
	</footer>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>
</body>
</html>