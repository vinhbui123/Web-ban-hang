<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang Đăng Kí</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
    <script src="${pageContext.request.contextPath}/js/product.js"></script>



</head>
<body>

<%@include file="header.jsp" %>
<div class="wrapper">
    <div class="inner">
        <form action="register" method="POST">
            <h3 class="register-title">Đăng Kí Ngay Nào</h3>

            <!-- Lỗi hiển thị ra màn hình -->
            <%
                String error = (String) request.getAttribute("error");
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String confirmPassword = request.getParameter("confirmPassword");
                if (error == null) error = "";
                if (firstName == null) firstName = "";
                if (lastName == null) lastName = "";
                if (username == null) username = "";
                if (email == null) email = "";
                if (password == null) password = "";
                if (confirmPassword == null) confirmPassword = "";
            %>
            <c:if test="${not empty error}">
                <div style="padding: 15px; margin: 20px 0; border-radius: 5px; font-size: 16px; color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb;">
                        ${error}
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div style="padding: 15px; margin: 20px 0; border-radius: 5px; font-size: 16px; color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb;">
                        ${success}
                </div>
            </c:if>


            <div class="form-group">
                <div class="form-wrapper">
                    <label for="first-name">Họ</label>
                    <input type="text" id="first-name" name="firstName" class="form-control" value="<%= firstName %>" required>
                </div>
                <div class="form-wrapper">
                    <label for="last-name">Tên</label>
                    <input type="text" id="last-name" name="lastName" class="form-control" value="<%= lastName %>" required>
                </div>
            </div>

            <div class="form-wrapper">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control" value="<%=email%>" required>
            </div>

            <div class="form-wrapper">
                <label for="username">Tên người dùng</label>
                <input type="text" id="username" name="username" class="form-control" value="<%= username %>" required>
            </div>

            <div class="form-wrapper">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" class="form-control" value="<%= password %>" required>
            </div>

            <div class="form-wrapper">
                <label for="confirm-password">Xác nhận mật khẩu</label>
                <input type="password" id="confirm-password" name="confirmPassword" class="form-control" value="<%= confirmPassword %>" required>
            </div>

            <div class="checkbox">
                <label><input type="checkbox" id="terms" name="terms" required> Tôi chấp nhận Điều khoản sử dụng & Chính sách bảo mật.</label>
            </div>

            <div class="btn-register">
                <button type="submit">Đăng Kí Ngay</button>
            </div>
        </form>
    </div>
</div>

<%@include file="footer.jsp" %>
</body>
</html>
