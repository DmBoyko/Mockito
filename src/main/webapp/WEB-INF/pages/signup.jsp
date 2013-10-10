<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<c:set var="app" value="${pageContext.request.contextPath}"/>
<c:set var="css" value="${pageContext.request.contextPath}/style.css"/>
<%@ page session="false" %>

<html>
<head>
    <title>Registration page</title>
    <link rel="stylesheet" type="text/css" href="${css}" media="all"/>
</head>
<body>
<div class="loginform">
    <h1 class="loginpageTitle">Registration</h1>
    <sf:form method="POST" modelAttribute="newUser">
        <table class="loginTable">
            <c:if test="${not empty error || not empty errorMsg || not empty
             errorMsgCap}">
                <tr>
                    <td class="error" colspan='2'>
                        Signin was not successful, try again.<br/>
                        Caused : <c:out value="${errorMsgCap}"/>
                        <c:out value="${errorMsg}"/>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td><sf:label path="login">Login:</sf:label></td>
                <td>
                    <small>No spaces, please.</small>
                    <br/>
                    <sf:input path="login"/><br/>
                    <small><sf:errors path="login" cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label path="firstName">First Name:</sf:label></td>
                <td>
                    <small>No spaces, please.</small>
                    <br/>
                    <sf:input path="firstName"/><br/>
                    <small><sf:errors path="firstName"
                                      cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label path="lastName">Last Name:</sf:label></td>
                <td>
                    <small>No spaces, please.</small>
                    <br/>
                    <sf:input path="lastName"/><br/>
                    <small><sf:errors path="lastName"
                                      cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label path="password">Password:</sf:label></td>
                <td>
                    <small>6 characters or more(be tricky!)</small>
                    <br/>
                    <sf:password path="password" size="30" showPassword="true"/><br/>
                    <small><sf:errors path="password"
                                      cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label
                        path="confirmPassword">Confirm Password:</sf:label></td>
                <td>
                    <small>6 characters or more(be tricky!)</small>
                    <br/>
                    <sf:password path="confirmPassword" size="30"
                                 showPassword="true"/><br/>
                    <small><sf:errors path="confirmPassword"
                                      cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label path="email">Email Address:</sf:label></td>
                <td>
                    <small>In case you forget something</small>
                    <br/>
                    <sf:input path="email" size="30"/><br/>
                    <small><sf:errors path="email" cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label path="birthDate">BirthDate :</sf:label></td>
                <td>
                    <small>Format - dd-MM-yyyy</small>
                    <br/>
                    <sf:input path="birthDate" size="30"/><br/>
                    <small><sf:errors path="birthDate"
                                      cssClass="errorIn"/></small>
                </td>
            </tr>
            <tr>
                <td><sf:label path="role">Role</sf:label></td>
                <td>
                    <small>Chose the role of user. In this time - you can choose
                        only 'user'
                    </small>
                    <br/>
                    <sf:select path="role">
                        <sf:option value="ROLE_USER" label="user"/>
                    </sf:select>
                </td>
            </tr>
            <tr>
                <td colspan='2'>
                    <script type="text/javascript"
                            src="http://api.recaptcha.net/challenge?k=<6LftC-YSAAAAABDdxcCPOyEkJ4Mb4i4uE6rrSUah>">
                    </script>
                    <noscript>
                        <iframe src="http://api.recaptcha.net/noscript?k=<6LftC-YSAAAAABDdxcCPOyEkJ4Mb4i4uE6rrSUah>"
                                height="300" width="500"
                                frameborder="0"></iframe>
                        <br>
                        <input type="text" name="recaptcha_challenge_field"
                               rows="3" cols="40">
                        </textarea>
                        <input type="hidden" name="recaptcha_response_field"
                               value="manual_challenge">
                    </noscript>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input name="commit" type="submit" value="Ok "/>
                    <input value="Cancel" onClick="location.href='${app}/index'"
                           type="button"/>
                </td>
            </tr>
            </tr>
        </table>
    </sf:form>
</div>
</body>
</html>