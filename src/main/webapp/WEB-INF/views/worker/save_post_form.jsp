<%--
  Created by IntelliJ IDEA.
  User: edutilos
  Date: 02.06.18
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>save_post_form</title>
</head>
<body>
    <form:form method="post" modelAttribute="worker" action="/worker/save_jdbc_post">
        id:
        <form:input path="id" type="text" />
        <form:errors path="id" />
        <br/>

        name:
        <form:input path="name" type="text" />
        <form:errors path="name" />
        <br/>

        age:
        <form:input path="age" type="text" />
        <form:errors path="age" />
        <br/>

        wage:
        <form:input path="wage" type="text" />
        <form:errors path="wage" />
        <br/>

        active:
        <form:input path="active" type="text" />
        <form:errors path="active" />
        <br/>

        <button type="submit">Save</button>
        <br/>
    </form:form>
</body>
</html>
