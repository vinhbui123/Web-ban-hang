<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tài khoản bị khóa</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
            text-align: center;
            padding-top: 100px;
        }

        .message-box {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 30px 40px;
            display: inline-block;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #d9534f;
        }

        p {
            font-size: 16px;
            color: #333;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #0275d8;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        a:hover {
            background-color: #025aa5;
        }
    </style>
</head>
<body>
<div class="message-box">
    <h2>Tài khoản của bạn đã bị khóa</h2>
    <p>Vui lòng liên hệ quản trị viên để được hỗ trợ mở khóa tài khoản.</p>
    <a href="<%= request.getContextPath() %>/login">Quay lại đăng nhập</a>
</div>
</body>
</html>
