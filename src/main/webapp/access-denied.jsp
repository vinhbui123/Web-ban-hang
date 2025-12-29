<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Access Denied</title>
    <style>
        body {
            background-color: #f8d7da;
            font-family: Arial, sans-serif;
            text-align: center;
            padding-top: 100px;
        }
        .message-box {
            display: inline-block;
            background-color: white;
            padding: 30px;
            border: 1px solid #f5c2c7;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #842029;
        }
        p {
            color: #6c757d;
        }
        a {
            margin-top: 20px;
            display: inline-block;
            text-decoration: none;
            color: #0d6efd;
        }
    </style>
</head>
<body>
<div class="message-box">
    <h1>403 - Access Denied</h1>
    <p>Bạn không có quyền truy cập vào trang này.</p>
    <a href="<%= request.getContextPath() %>/home">Quay về trang chủ</a>
</div>
</body>
</html>
