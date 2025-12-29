<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.NumberFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Map<String, Object> stats = (Map<String, Object>) request.getAttribute("stats");
    NumberFormat format = NumberFormat.getInstance();
%>

<html>
<head>
    <title>Thống kê đơn hàng</title>
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

<%@include file="ad-menu.jsp" %>

<div class="main-content">
    <header>
        <h1>Thống Kê Đơn Hàng</h1>
    </header>

    <div class="stat-row">
        <div class="stat-card total-orders">
            <div class="title">🧾 Tổng số đơn hàng</div>
            <div class="value"><%= stats.get("total_orders") %></div>
        </div>

        <div class="stat-card pending-orders">
            <div class="title">⏳ Đơn chờ xử lý</div>
            <div class="value"><%= stats.get("pending_orders") %></div>
        </div>

        <div class="stat-card confirmed-orders">
            <div class="title">✅ Đã xác nhận</div>
            <div class="value"><%= stats.get("confirmed_orders") %></div>
        </div>

        <div class="stat-card done-orders">
            <div class="title">✔️ Hoàn thành</div>
            <div class="value"><%= stats.get("done_orders") %></div>
        </div>

        <div class="stat-card cancelled-orders">
            <div class="title">❌ Đã huỷ</div>
            <div class="value"><%= stats.get("cancelled_orders") %></div>
        </div>

        <div class="stat-card total-revenue">
            <div class="title">💰 Tổng doanh thu</div>
            <div class="value"><%= stats.get("total_revenue") != null
                    ? format.format(stats.get("total_revenue"))
                    : "0" %> VNĐ VNĐ</div>
        </div>

        <div class="stat-card total-users" style="background-color:#34495e;">
            <div class="title">👥 Tổng người dùng</div>
            <div class="value">${userStats.total_users}</div>
        </div>

        <div class="stat-card total-lock-users" style="background-color:#7f8c8d;">
            <div class="title">🔒 Người dùng bị khóa</div>
            <div class="value">${userStats.locked_users}</div>
        </div>

    </div>
</div>

</body>
</html>
