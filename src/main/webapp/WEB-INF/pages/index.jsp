<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security"
           uri="http://www.springframework.org/security/tags" %>
<c:set var="app" value="${pageContext.request.contextPath}"/>
<c:set var="css" value="${pageContext.request.contextPath}/style.css"/>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytag" %>

<security:authorize
        access="isAuthenticated()">
    <security:authorize access="hasRole('ROLE_ADMIN')">
        <jsp:forward page="/admin"/>
    </security:authorize>
    <security:authorize access="hasRole('ROLE_USER')">
        <jsp:forward page="/hiUser"/>
    </security:authorize>
</security:authorize>

<html>
<head>
    <title>Login page</title>
    <link rel="stylesheet" type="text/css" href="${css}" media="all"/>
</head>
<body>
<div class="loginform">
    <h1 class="loginpageTitle">
        Login Page
    </h1>
    <sf:form id="loginForm" method="POST" action="j_spring_security_check">
        <table class="loginTable">
            <c:if test="${not empty error || not empty errorMsg}">
                <tr>
                    <td class="error" colspan='2'>
                        Login was not successful, try again.<br/>
                        Caused
                        : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"]
                            .message}
                        <c:out value="${errorMsg}"/>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td>Login</td>
                <td><input type="text" name="j_username"/></td>
                <sf:errors path="login" cssClass="error"/>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="j_password"/></td>
                <sf:errors path="password" cssClass="error"/>
            </tr>
            <%--<tr>--%>
                <%--<td colspan="2">--%>
                    <%--<script type="text/javascript"--%>
                            <%--src="http://api.recaptcha.net/challenge?k=<6LftC-YSAAAAABDdxcCPOyEkJ4Mb4i4uE6rrSUah>">--%>
                    <%--</script>--%>
                    <%--<noscript>--%>
                        <%--<iframe src="http://api.recaptcha.net/noscript?k=<6LftC-YSAAAAABDdxcCPOyEkJ4Mb4i4uE6rrSUah>"--%>
                                <%--height="300" width="500"--%>
                                <%--frameborder="0"></iframe>--%>
                        <%--<input type="text" name="recaptcha_challenge_field"--%>
                               <%--rows="3" cols="40">--%>
                        <%--</textarea>--%>
                        <%--<input type="hidden" name="recaptcha_response_field"--%>
                               <%--value="manual_challenge">--%>
                    <%--</noscript>--%>
                <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td colspan='2'>
                    <input type="submit" value="Login">
                    <input type="button" value="Sign in"
                           onclick="location.href='${app}/signup'"/>
                </td>
            </tr>
        </table>
    </sf:form>
</div>
</body>
</html>