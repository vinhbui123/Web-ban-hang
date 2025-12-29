<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tài khoản bị khóa tạm thời</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial;
            background-color: #f8d7da;
            color: #721c24;
            text-align: center;
            padding: 50px;
        }
        .login-btn {
            margin-top: 30px;
            padding: 10px 20px;
            font-size: 1rem;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .disabled-btn {
            background-color: #ccc;
            cursor: not-allowed;
        }
    </style>
</head>
<body>

<h2>🚫 Tài khoản của bạn đã bị khóa tạm thời</h2>
<p>Do truy cập trái phép quá nhiều lần. Bạn có thể đăng nhập lại sau:</p>

    <%
    long currentTime = System.currentTimeMillis();
    long lockedUntil = 0;
    if (session.getAttribute("lockedUntil") != null) {
        lockedUntil = (Long) session.getAttribute("lockedUntil");
    }

    boolean canLogin = currentTime >= lockedUntil;
%>

<form action="login.jsp" method="get">
    <button type="submit" class="login-btn <%= canLogin ? "" : "disabled-btn" %>" <%= canLogin ? "" : "disabled" %>>
