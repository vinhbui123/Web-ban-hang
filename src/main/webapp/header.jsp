<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="vn.edu.hcmuaf.fit.Web_ban_hang.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Header</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>
<header class="mainHeader mainHeader_temp" id="site-header">
    <div class=" mainHeader-center">
        <div class="container-header">
            <div class="header-logo">
                <a href="home">
                    <img src="images/logo.png" alt="Logo">
                </a>
            </div>

            <div class="header-action">
                <div class="header-search">
                    <div class="search-box">
                        <form id="searchForm" action="search" method="get">
                            <input type="text" name="keyword" placeholder="Tìm Kiếm Sản Phẩm" required>
                            <button type="submit"><i class="fas fa-search"></i></button>
                        </form>
                    </div>
                </div>

                <%-- Get User from Session --%>
                <c:set var="user" value="${sessionScope.user}"/>

                <div class="header-account">
                    <span class="account-icon">
                        <c:choose>
                            <%-- 1. Check if User is Logged In --%>
                            <c:when test="${user != null && not empty user.username}">
                                <a href="${pageContext.request.contextPath}/account">
                                    <c:choose>
                                        <%-- Case A: User has NO Avatar -> Show Default Icon --%>
                                        <c:when test="${empty user.avatar}">
                                            <i class="fas fa-user"></i>
                                        </c:when>

                                        <%-- Case B: User HAS Avatar -> Show Base64 Image --%>
                                        <c:otherwise>
                                            <img src="data:image/jpeg;base64,${user.avatar}"
                                                 alt="Avatar"
                                                 style="width: 50px; height: 50px; border-radius: 50%; object-fit: cover; vertical-align: middle; border: 1px solid #fff;">
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </c:when>

                            <%-- 2. User is NOT Logged In -> Show Default Icon --%>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/login"><i class="fas fa-user"></i></a>
                            </c:otherwise>
                        </c:choose>
                    </span>

                    <div class="account-info">
                        <c:choose>
                            <c:when test="${user != null && not empty user.username}">
                                <span class="account-text">Xin chào, ${user.firstName} ${user.lastName}!</span>
                                <a href="${pageContext.request.contextPath}/change-password"><span class="account-menu"> Đổi mật khẩu </span></a>
                                <a href="${pageContext.request.contextPath}/logout"><span class="account-menu"> Đăng Xuất <i class="fas fa-sign-out-alt"></i></span></a>
                            </c:when>
                            <c:otherwise>
                                <span class="account-text"><a href="${pageContext.request.contextPath}/login">Đăng Nhập</a> / <a href="${pageContext.request.contextPath}/register">Đăng Ký</a></span>
                                <span> Xin Chào khách hàng </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="header-cart" onclick="window.location.href='cart'">
                    <i class="fas fa-cart-shopping"></i>
                    <c:if test="${sessionScope.cart != null}">
                        <span class="cart-count">${sessionScope.cart.list.size()}</span>
                    </c:if>
                    <span class="cart-text"> Giỏ Hàng</span>
                </div>

                <c:if test="${not empty user}">
                    <div class="header-purchase" onclick="window.location.href='purchase'">
                        <i class="fas fa-receipt"></i>
                        <span class="cart-text">Đơn Mua</span>
                    </div>

                    <%-- Admin Check (Role != 0) --%>
                    <c:if test="${user.role != 0}">
                        <a href="${pageContext.request.contextPath}/adminProduct" class="admin-btn">
                            <i class="fa-solid fa-user-tie"></i> Trang Quản Trị
                        </a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>

    <div class="headerMenu">
        <div class="container-menu">
            <ul class="menu">
                <li><a href="${pageContext.request.contextPath}/home">TRANG CHỦ</a></li>
                <li><a href="${pageContext.request.contextPath}/list-product?category=all">SẢN PHẨM</a></li>
                <c:if test="${not empty sessionScope.category}">
                    <c:forEach var="category" items="${sessionScope.category}">
                        <li>
                            <a href="${pageContext.request.contextPath}/list-product?category=${category.id}">${category.name}</a>
                        </li>
                    </c:forEach>
                </c:if>
            </ul>
        </div>
    </div>
</header>
</body>
</html>