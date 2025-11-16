<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ page import="vn.edu.hcmuaf.fit.Web_ban_hang.dao.session.Cart" %>--%>
<%@ page import="vn.edu.hcmuaf.fit.Web_ban_hang.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Header</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
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
                    <img src="images/logo.png">
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
                <c:set var="user" value="${sessionScope.user}"/>
                <div class="header-account">
                                <span class="account-icon">
                                  <c:choose>
                                      <c:when test="${user.getUsername() != null}">
                                          <a href="account"><i
                                                  class="fas fa-user"></i></a>
                                      </c:when>
                                      <c:otherwise>
                                          <a href="login"><i class="fas fa-user"></i></a>
                                      </c:otherwise>
                                  </c:choose>
                                </span>
                    <div class="account-info">
                        <c:choose>
                            <c:when test="${not empty user.getUsername()}">
                                <span class="account-text">Xin chào, ${user.getFirstName()} ${user.getLastName()}!</span>
                                <a href="${pageContext.request.contextPath}/change-password"><span class="account-menu"> Đổi mật khẩu </span></a>
                                <a href="logout"><span class="account-menu"> Đăng Xuất <i
                                        class="fas fa-sign-out-alt"></i></span></a>
                            </c:when>
                            <c:otherwise>
                                <span class="account-text"><a href="login">Đăng Nhập</a> / <a href="login">Đăng Ký</a></span>
                                <c:if test="${empty user.getUsername()}">
                                    <span> Xin Chào khách hàng </span>
                                </c:if>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                <div class="header-cart" onclick="window.location.href='cart'">
                    <i class="fas fa-cart-shopping"></i>

                    <c:if test="${sessionScope.cart !=  null}">
                        <span class="cart-count">${sessionScope.cart.getList().size()}</span>
                    </c:if>
                    <span class="cart-text"> Giỏ Hàng</span>
                </div>
                <c:if test="${not empty sessionScope.user}">
                    <div class="header-purchase" onclick="window.location.href='purchase'">
                        <i class="fas fa-receipt"></i>
                        <span class="cart-text">Đơn Mua</span>
                    </div>
                </c:if>
                <c:set var="user" value="${sessionScope.user}" />
                <c:if test="${user != null && user.role != 0}">
                    <a href="${pageContext.request.contextPath}/adminProduct" class="admin-btn">
                        <i class="fa-solid fa-user-tie"></i> Trang Quản Trị
                    </a>
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