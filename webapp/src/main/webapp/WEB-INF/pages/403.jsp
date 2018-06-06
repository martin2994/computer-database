<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title><spring:message code="application.name" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<c:set var="currentPath" value="${pageContext.request.contextPath}"></c:set>

<!-- Bootstrap -->
<link href="${currentPath}/resources/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="${currentPath}/resources/css/font-awesome.css" rel="stylesheet"
	media="screen">
<link href="${currentPath}/resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="${currentPath}/computer"><spring:message
					code="application.title" /> </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				<spring:message code="text.error403" />
				<br />
				<!-- stacktrace -->
			</div>
		</div>
	</section>

	<script src="${currentPath}/resources/js/jquery.min.js"></script>
	<script src="${currentPath}/resources/js/bootstrap.min.js"></script>
	<script src="${currentPath}/resources/js/dashboard.js"></script>

</body>
</html>