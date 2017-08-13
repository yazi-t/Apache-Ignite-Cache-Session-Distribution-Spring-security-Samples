<%--
  Created by IntelliJ IDEA.
  User: yazi
  Date: 8/12/2017
  Time: 1:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>
</head>
<body>

<h1>Login Page</h1>

<h3>Ignite spring security session replication</h3>
<p>Test username: user password:1234</p>
<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
    <span style="color: red"><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message.toLowerCase()}"/></span>
</c:if>

<form class="login-form" action="<c:url value="/login"/>" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input name="username" type="text" required="required" autocomplete="off" placeholder="Username"/>
    <input type="password" name="password" required="required" autocomplete="off" placeholder="Password"/>
    <button type="submit">Login</button>
</form>
</body>
</html>
