<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<c:set var="css" value="${pageContext.request.contextPath}/style.css"/>
<c:set var="app" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <c:if test="${type=='edit'}">
        <title>Edit user</title>
    </c:if>
    <c:if test="${type=='add'}">
        <title>Add user</title>
    </c:if>
    <link rel="stylesheet" type="text/css" href="${css}" media="all"/>
</head>
<body>
<div align="right">
    <c:out value="${sessionScope.user.firstName} ${sessionScope.user.lastName} ("/>
    <a href="${app}/j_spring_security_logout">Logout</a>
    <c:out value=")"/>
</div>
<div class="loginform">
    <c:if test="${type=='add'}">
        <h1 class="loginpageTitle">Add user</h1>
    </c:if>
    <c:if test="${type=='edit'}">
        <h1 class="loginpageTitle">Edit user</h1>
    </c:if>

    <sf:form method="POST" modelAttribute="newUser"
             action="${app}/edit?type=${type}">
        <table class="loginTable">
            <c:if test="${not empty errorMsg}">
                <tr>
                    <td class="error" colspan='2'>
                        <c:out value="${errorMsg}"/>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td><sf:label path="login">Login:</sf:label></td>
                <c:if test="${type=='edit'}">
                    <td>
                        <sf:input path="login" size="15" readonly="true"/><br/>
                    </td>
                </c:if>
                <c:if test="${type=='add'}">
                    <td>
                        <small>No spaces, please.</small>
                        <br/>
                        <sf:input path="login" size="15"/><br/>
                        <small><sf:errors path="login"
                                          cssClass="errorIn"/></small>
                    </td>
                </c:if>
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
                    <sf:select path="role">
                        <sf:option value="ROLE_USER" label="user"/>
                        <sf:option value="ROLE_ADMIN" label="admin"/>
                    </sf:select>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input name="commit" type="submit" value="Ok "/>
                    <input value="Cancel" onClick="location.href='${app}/admin'"
                           type="button"/>
                </td>
            </tr>
        </table>
    </sf:form>
</div>
</body>
</html>