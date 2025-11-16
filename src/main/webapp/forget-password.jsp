<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Quên Mật Khẩu</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forget-pass.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
  <script src="${pageContext.request.contextPath}/jsbapp/js/index.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <style>
    .alert {
      padding: 10px;
      margin: 10px 0;
      border-radius: 5px;
      font-size: 14px;
    }
    .alert-success {
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }
    .alert-danger {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }
  </style>
</head>
<body>

<div class="wrapper">
  <div class="container">
    <div class="title-section">
      <h1><i class="fa-solid fa-lock"></i></h1>
      <h2 class="title">Đặt lại mật khẩu</h2>
      <p class="para">Hãy nhập email của bạn vào ô bên dưới, chúng tôi sẽ gửi mã xác nhận.</p>
    </div>

    <!-- Hiển thị thông báo thành công hoặc lỗi -->
    <c:if test="${not empty message}">
      <div class="alert alert-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post" class="form">
      <div class="input-group">
        <label for="email" class="label-title">Hãy nhập email</label>
        <input type="email" id="email" name="email" placeholder="Email" required>
        <span class="icon"><i class="fa-solid fa-envelope"></i></span>
      </div>
      <div class="input-group">
        <button class="submit-btn" type="submit">Gửi mã xác nhận</button>
      </div>
    </form>
  </div>
</div>

</body>
</html>
