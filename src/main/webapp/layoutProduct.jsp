<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/26/2024
  Time: 9:38 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HandMade</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">

    <script src="src/main/webapp/jsbapp/js/index.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css.css">
    <script src="src/main/webapp/js/product.js"></script>
</head>
<body>


<%@include file="header.jsp" %>

<div>
    <!-- Chèn trang list-product.jsp vào đây -->
    <jsp:include page="list-product.jsp"/>
</div>

<%@include file="footer.jsp" %>

</body>
</html>
