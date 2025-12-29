<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Quản Lý Danh Mục - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</head>
<body>
<%@include file="ad-menu.jsp" %>

<div class="main-content">
    <header>
        <h1>Quản Lý Lịch Sử Giao Dịch</h1>
    </header>

    <section class="transaction-management">
        <table class="transaction-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Sản Phẩm</th>
                <th>Loại Giao Dịch</th>
                <th>Số Lượng</th>
                <th>Người Thực Hiện</th>
                <th>Họ Tên</th>
                <th>Vai Trò</th>
                <th>Thời Gian</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="transaction" items="${transactionHistory}">
                <tr>
                    <td>${transaction.id}</td>
                    <td>${transaction.product_name}</td>
                    <td>
                        <c:choose>
                            <c:when test="${transaction.type == 'import'}">Nhập hàng</c:when>
                            <c:when test="${transaction.type == 'export'}">Xuất hàng</c:when>
                            <c:when test="${transaction.type == 'cancel'}">Đơn hủy</c:when>
                            <c:otherwise>Khác</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${transaction.quantity}</td>
                    <td>${transaction.username}</td>
                    <td>${transaction.name}</td> <!-- Họ Tên đã được ghép ở phía controller -->
                    <td>
                        <c:choose>
                            <c:when test="${transaction.role eq 1}">Admin</c:when>
                            <c:when test="${transaction.role eq 2}">User</c:when>
                            <c:when test="${transaction.role eq 3}">MOD Nhập Hàng</c:when>
                            <c:when test="${transaction.role eq 4}">MOD Xuất Hàng</c:when>
                            <c:when test="${transaction.role eq 5}">MOD Xem</c:when>
                            <c:otherwise>Unknown</c:otherwise>
                        </c:choose>


                    </td>
                    <td>${transaction.created_at}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </section>
</div>
</body>
</html>
