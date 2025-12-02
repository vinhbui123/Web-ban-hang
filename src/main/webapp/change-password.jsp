<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="vn.edu.hcmuaf.fit.Web_ban_hang.model.User" %>

<%
    request.setCharacterEncoding("UTF-8");
    HttpSession sessionUser = request.getSession();
    User user = (User) sessionUser.getAttribute("user"); // Đổi từ "currentUser" ➜ "user"

//    if (user == null) {
//        response.sendRedirect("login.jsp");
//        return;
//    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            width: 40%;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background: #f9f9f9;
            text-align: center;
        }

        h2 {
            color: #333;
        }

        .form-group {
            margin: 15px 0;
            position: relative;
            text-align: left;
        }

        label {
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 10px;
            padding-right: 35px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 15px;
        }

        .btn {
            background: #007bff;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 15px;
        }

        .btn:hover {
            background: #0056b3;
        }

        .message {
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 15px;
        }

        .error {
            background: #ffdddd;
            color: #d8000c;
            border: 1px solid #d8000c;
        }

        .success {
            background: #ddffdd;
            color: #4caf50;
            border: 1px solid #4caf50;
        }

        .form-group i {
            position: absolute;
            top: 70%;
            right: 10px;
            transform: translateY(-50%);
            font-size: 18px;
            cursor: pointer;
            color: #444;
        }
    </style>

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
    <h2>Đổi mật khẩu</h2>

    <% if (request.getAttribute("error") != null) { %>
    <p class="message error"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <p class="message success"><%= request.getAttribute("success") %></p>
    <% } %>

    <form action="change-password" method="post">
        <div class="form-group">
            <label for="currentPassword">Mật khẩu hiện tại:</label>
            <input type="password" id="currentPassword" name="currentPassword" required>
            <i id="eyeIconCurrent" class="fas fa-eye-slash" onclick="togglePasswordVisibility('currentPassword', 'eyeIconCurrent')"></i>
        </div>

        <div class="form-group">
            <label for="newPassword">Mật khẩu mới:</label>
            <input type="password" id="newPassword" name="newPassword" required>
            <i id="eyeIconNew" class="fas fa-eye-slash" onclick="togglePasswordVisibility('newPassword', 'eyeIconNew')"></i>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
            <i id="eyeIconConfirm" class="fas fa-eye-slash" onclick="togglePasswordVisibility('confirmPassword', 'eyeIconConfirm')"></i>
        </div>

        <button type="submit" class="btn">Đổi mật khẩu</button>
    </form>
</div>

</body>
</html>
