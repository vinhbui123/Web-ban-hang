<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại Mật Khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset-pass.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/js/all.min.js"></script>

    <!-- ✅ Style nội bộ dành cho thông báo -->
    <style>
        .server-message {
            margin-top: 10px;
            margin-bottom: 10px;
            display: block;
            text-align: center;
            font-size: 16px;
            line-height: 1.6;
            padding: 6px 12px;
        }
        .error-message {
            color: red;
            font-weight: bold;
        }
        .success-message {
            color: green;
            font-weight: bold;
        }
    </style>

    <!-- ✅ Chuyển hướng sau khi thành công -->
    <c:if test="${not empty success}">
        <script>
            setTimeout(function () {
                window.location.href = "${pageContext.request.contextPath}/login";
            }, 5000);
        </script>
    </c:if>
</head>
<body>

<div class="wrapper">
    <div class="container">
        <div class="title-section">
            <h1><i class="fa-solid fa-lock"></i></h1>
            <h2 class="title">Tạo mật khẩu mới</h2>
            <p class="para">Nhập mật khẩu mới của bạn.</p>

            <!-- ✅ Thông báo từ server -->
            <c:if test="${not empty error}">
                <p class="error-message server-message">${error}</p>
            </c:if>
            <c:if test="${not empty success}">
                <p class="success-message server-message">${success}</p>
            </c:if>
        </div>

        <form action="${pageContext.request.contextPath}/reset-password" method="post" class="form" onsubmit="return validateForm();">
            <input type="hidden" name="token" value="${param.token != null ? param.token : token}">

            <!-- ⚠️ Lỗi client -->
            <p id="error-message" class="error-message server-message"></p>

            <div class="input-group">
                <label for="newPassword" class="label-title">Mật khẩu mới</label>
                <input type="password" id="newPassword" name="newPassword" placeholder="Nhập mật khẩu mới" required>
                <span class="icon"><i class="fa-solid fa-key"></i></span>
                <i id="eyeIconNew" class="fas fa-eye-slash" onclick="togglePasswordVisibility('newPassword', 'eyeIconNew')"></i>
            </div>

            <div class="input-group">
                <label for="confirmPassword" class="label-title">Xác nhận mật khẩu</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu mới" required>
                <span class="icon"><i class="fa-solid fa-key"></i></span>
                <i id="eyeIconConfirm" class="fas fa-eye-slash" onclick="togglePasswordVisibility('confirmPassword', 'eyeIconConfirm')"></i>
            </div>

            <div class="input-group">
                <button class="submit-btn" type="submit">Xác nhận</button>
            </div>

            <div class="input-group">
                <a href="${pageContext.request.contextPath}/login" class="back-btn">Quay lại trang đăng nhập</a>
            </div>
        </form>
    </div>
</div>

<!-- ✅ Script xử lý logic -->
<script>
    function validateForm() {
        var newPassword = document.getElementById("newPassword").value;
        var confirmPassword = document.getElementById("confirmPassword").value;
        var errorMessage = document.getElementById("error-message");

        if (newPassword.length < 8) {
            errorMessage.innerText = "Mật khẩu phải có ít nhất 8 ký tự.";
            return false;
        }
        if (newPassword !== confirmPassword) {
            errorMessage.innerText = "Mật khẩu xác nhận không khớp.";
            return false;
        }
        return true;
    }

    function togglePasswordVisibility(inputId, eyeIconId) {
        var input = document.getElementById(inputId);
        var eyeIcon = document.getElementById(eyeIconId);
        if (input.type === "password") {
            input.type = "text";
            eyeIcon.classList.remove("fa-eye-slash");
            eyeIcon.classList.add("fa-eye");
        } else {
            input.type = "password";
            eyeIcon.classList.remove("fa-eye");
            eyeIcon.classList.add("fa-eye-slash");
        }
    }

    // ✅ Tự động ẩn thông báo sau vài giây
    window.addEventListener("DOMContentLoaded", function () {
        const messages = document.querySelectorAll(".server-message");
        messages.forEach(function (msg) {
            setTimeout(() => {
                msg.style.transition = "opacity 1s";
                msg.style.opacity = 0;
            }, 4000);
        });
    });
</script>

</body>
</html>
