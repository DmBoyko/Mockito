<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytag" %>
<c:set var="app" value="${pageContext.request.contextPath}"/>
<c:set var="css" value="${pageContext.request.contextPath}/style.css"/>

<html>
<head>
    <title>Admin homepage</title>
    <link rel="stylesheet" type="text/css" href="${css}" media="all"/>
</head>
<body>
<div align="right">
    <c:out value="${sessionScope.user.firstName} ${sessionScope.user.lastName} ("/>
    <a href="${app}/j_spring_security_logout">Logout</a>
    <c:out value=")"/>
</div>
<div class="adminContainer">
    <a href='${app}/showEdit?type=add&login=newUser'>Add new user</a>
    <mytag:table list="${users}"/>
</div>
</body>
</html>