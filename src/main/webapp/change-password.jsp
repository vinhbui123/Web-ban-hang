<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/change-password.css">

    <script>
        function togglePasswordVisibility(inputId, eyeIconId) {
            const input = document.getElementById(inputId);
            const eyeIcon = document.getElementById(eyeIconId);
            const isPassword = input.type === "password";

            input.type = isPassword ? "text" : "password";
            eyeIcon.classList.toggle("fa-eye-slash", !isPassword);
            eyeIcon.classList.toggle("fa-eye", isPassword);
        }
    </script>
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">
    <h2 style="color: #333">Đổi mật khẩu</h2>

    <% if (request.getAttribute("error") != null) { %>
    <p class="message error"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <p class="message success"><%= request.getAttribute("success") %></p>
    <% } %>

    <form action="change-password" method="post">
        <div class="form-group">
            <label class="change-password-labels" for="currentPassword">Mật khẩu hiện tại:</label>
            <input class="input-password" type="password" id="currentPassword" name="currentPassword" required>
            <i id="eyeIconCurrent" class="fas fa-eye-slash" onclick="togglePasswordVisibility('currentPassword', 'eyeIconCurrent')"></i>
        </div>

        <div class="form-group">
            <label class="change-password-labels" for="newPassword">Mật khẩu mới:</label>
            <input class="input-password" type="password" id="newPassword" name="newPassword" required>
            <i id="eyeIconNew" class="fas fa-eye-slash" onclick="togglePasswordVisibility('newPassword', 'eyeIconNew')"></i>
        </div>

        <div class="form-group">
            <label class="change-password-labels" for="confirmPassword">Xác nhận mật khẩu mới:</label>
            <input class="input-password" type="password" id="confirmPassword" name="confirmPassword" required>
            <i id="eyeIconConfirm" class="fas fa-eye-slash" onclick="togglePasswordVisibility('confirmPassword', 'eyeIconConfirm')"></i>
        </div>

        <button type="submit" class="btn">Đổi mật khẩu</button>
    </form>
</div>

</body>
</html>
