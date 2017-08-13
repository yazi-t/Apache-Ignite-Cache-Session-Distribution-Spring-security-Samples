<%--
  Created by IntelliJ IDEA.
  User: yazi
  Date: 8/12/2017
  Time: 1:34 PM
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
<h1>Home Page</h1>
<form id="logout" action="<c:url value="/logout"/>" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <button type="submit">Logout</button>
</form>
<p>Ignite In-Memory Data Fabric is capable of caching web sessions of all Java Servlet containers that follow Java Servlet 3.0 Specification, including Apache Tomcat, Eclipse Jetty, Oracle WebLogic, and others.</p>
<br>

</body>
</html>
