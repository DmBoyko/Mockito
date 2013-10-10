<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>Successfull Sign Up</title>
</head>
<body>
<div align="center">
    <h1 align="center">Successfull Sign Up</h1>

    <p align="center">Congratulations! Your signup was successful. You can now
        signin in your page! </p>

    <a href="${pageContext.request.contextPath}/" title="Home">Login page</a>
</div>
</body>
</html>