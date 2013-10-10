<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="list" required="true" type="java.util.List" %>

<table class="myTable">
    <thead>
    <tr>
        <td>Login</td>
        <td>First Name</td>
        <td>Last Name</td>
        <td>Email</td>
        <td>Role</td>
        <td>Birthday</td>
        <td>Actions</td>
    </tr>
    </thead>
    <c:forEach var="item" items="${list}">
        <c:set var="role" value="${item.role.name}"/>
        <c:if test="${role=='ROLE_USER'}">
            <c:set var="color" value="#48663C"/>
        </c:if>
        <c:if test="${role=='ROLE_ADMIN'}">
            <c:set var="color" value="#663926"/>
        </c:if>
        <tr style="background: ${color}">
            <td>${item.login}</td>
            <td>${item.firstName }</td>
            <td>${item.lastName}</td>
            <td>${item.email}</td>
            <td>${item.role.name}</td>
            <td><fmt:formatDate value="${item.birthDate}"
                                pattern="dd.MM.yyyy"></fmt:formatDate></td>
            <td>
                <form action="controller.do" method="POST">
                    <a href="<c:url value="/showEdit?type=edit&login=${item.login}">
                    </c:url>">Edit</a>
                    <a href="<c:url value="/delete?login=${item.login}"></c:url>"
                       onclick="return confirm('Are you sure?');">Delete</a>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>