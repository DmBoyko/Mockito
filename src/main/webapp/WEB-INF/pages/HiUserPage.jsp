<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="app" value="${pageContext.request.contextPath}"/>
<c:set var="css" value="${pageContext.request.contextPath}/style.css"/>

<html>
<head>
    <title>Hi user!</title>
    <link rel="stylesheet" type="text/css" href="${css}" media="all"/>
</head>
<body>
<div class="hellomessage">
    <h4 align="center" class="hello">
        <strong>
            <c:out value="Hello, "/>
            <c:out value="${sessionScope.user.firstName} ${sessionScope.user.lastName} !"/>
        </strong>
    </h4>

    <div align="center">
        <c:out value="Click "/>
        <a href="<c:url value="${app}/j_spring_security_logout"></c:url>">Here</a>
        <c:out value=" to logout"/>
    </div>
</div>
</body>
</html>