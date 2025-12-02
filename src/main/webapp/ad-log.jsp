<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
    <title>Nhật ký hoạt động người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<%@include file="ad-menu.jsp" %>

<div class="main-content">
    <h1>Nhật ký hoạt động</h1>

    <table border="1" cellpadding="8" cellspacing="0" width="100%">
        <thead>
        <tr style="background-color: #f0f0f0;">
            <th>ID</th>
            <th>Tên đăng nhập</th>
            <th>Email</th>
            <th>Hành động</th>
            <th>Trạng thái</th>
            <th>IP</th>
            <th>Ghi chú</th>
            <th>Thời gian</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${logs}">
            <tr>
                <td>${log.id}</td>
                <td>${log.username != null ? log.username : '-'}</td>
                <td>${log.email != null ? log.email : '-'}</td>
                <td>${log.action}</td>
                <td>
                    <c:choose>
                        <c:when test="${fn:toLowerCase(fn:trim(log.status)) eq 'thành công'}">
                            <span style="color:green">✔ Thành công</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color:red">✗ Thất bại</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${log.ipAddress}</td>
                <td>${log.note}</td>
                <td><fmt:formatDate value="${log.timestamp}" pattern="HH:mm dd-MM-yyyy"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
