<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>menuAdmin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">

</head>
<body>
<div class="sidebar">
    <h2>Kênh Người Bán</h2>
    <ul>
        <li><a href="home">Trang Chủ</a></li>
        <li><a href="admin">Thống Kê Tổng Quan</a></li>
        <li><a href="adminProduct">Quản Lý Sản Phẩm</a></li>
        <li><a href="adminCategory">Quản lý Danh Mục</a>
        <li><a href="adminMaterials">Quản lý Chất Liệu Sản Phẩm</a>
        <li><a href="adminOrders">Quản Lý Đơn Hàng</a></li>
        <li><a href="adminUsers">Quản Lý Tài Khoản</a>
        <li><a href="adminLogs">Lịch Sử Hoạt Động</a>
        <li><a href="adminInventory">Quản Lý Xuất Nhập Sản Phẩm</a></li>
        <li><a href="adminTransactions">Quản Lý Giao Dịch Sản Phẩm</a></li>
        <li><a href="adminComment">Quản Lý Đánh Giá</a></li>
        <li><a href="logout" class="btn-logout">Đăng Xuất</a></li>
    </ul>
</div>

</body>
</html>
