<%--
  Created by IntelliJ IDEA.
  User: edutilos
  Date: 02.06.18
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>findAll.jsp</title>
</head>
<body>
<c:forEach var="worker" items="${workers}">
    <c:out value="${worker.id}" />
    <c:out value="${worker.name}" />
    <c:out value="${worker.age}" />
    <c:out value="${worker.wage}" />
    <c:out value="${worker.active}" />
    <br/>
</c:forEach>
</body>
</html>
