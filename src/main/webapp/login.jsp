<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng Nhập</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Thư viện FontAwesome và CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">

    <style>
        .error-message {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">
    <div class="screen">
        <form class="login" action="login" method="post">
            <div class="login-title"><h3>Đăng Nhập Tài Khoản!</h3></div>

            <!-- Thông báo đăng ký thành công -->
            <c:if test="${not empty success}">
                <div style="padding: 15px; margin: 20px 0; border-radius: 5px; font-size: 16px; color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; text-align: center;">
                        ${success}
                </div>
            </c:if>

            <!-- Hiển thị lỗi -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>

            <!-- Tên đăng nhập -->
            <div class="login__field">
                <i class="login__icon fas fa-user"></i>
                <input type="text" class="login__input" name="username" placeholder="Tên đăng nhập" required value="${username}">
            </div>

            <!-- Mật khẩu -->
            <div class="login__field">
                <i class="login__icon fas fa-lock"></i>
                <input type="password" class="login__input" name="password" placeholder="Mật khẩu" required>
            </div>

            <button type="submit" class="button login__submit">
                <span class="button__text">Đăng Nhập</span>
                <i class="button__icon fas fa-chevron-right"></i>
            </button>

            <div class="login__options">
                <a href="${pageContext.request.contextPath}/forget-password.jsp" class="login__link">Quên mật khẩu?</a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="login__link">Bạn chưa có tài khoản? Đăng ký</a>
            </div>

        </form>
    </div>
</div>
<%@ include file="footer.jsp" %>

</body>
</html>
