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
    <title>findById.jsp</title>
</head>
<body>
    <c:out value="${worker.id}" /><br/>
    <c:out value="${worker.name}" /><br/>
    <c:out value="${worker.age}" /><br/>
    <c:out value="${worker.wage}" /><br/>
    <c:out value="${worker.active}" /><br/>
</body>
</html>
