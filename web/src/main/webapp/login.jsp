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

        .countdown {
            font-weight: bold;
            color: orange;
            text-align: center;
            margin-bottom: 15px;
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

            <!-- Hiển thị đếm ngược -->
            <c:if test="${not empty countdownTime}">
                <div class="countdown">Vui lòng thử lại sau: <span id="timer"></span></div>
                <script>
                    let seconds = ${countdownTime};
                    function updateTimer() {
                        const minutes = Math.floor(seconds / 60);
                        const secs = seconds % 60;
                        document.getElementById("timer").innerText =
                            (minutes < 10 ? "0" : "") + minutes + ":" + (secs < 10 ? "0" : "") + secs;
                        if (seconds > 0) {
                            seconds--;
                            setTimeout(updateTimer, 1000);
                        } else {
                            location.reload(); // Tự động reload lại
                        }
                    }
                    updateTimer();
                </script>
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
            <!-- CAPTCHA -->
            <div class="login__field">
                <i class="login__icon fas fa-robot"></i>
                <input type="text" class="login__input" name="captchaInput" placeholder="Nhập mã CAPTCHA" required>
            </div>

            <!-- CAPTCHA ảnh + nút reload nằm cạnh nhau -->
            <div style="display: flex; justify-content: center; align-items: center; margin-top: 10px; gap: 10px;">
                <img id="captchaImage"
                     src="${pageContext.request.contextPath}/captcha?ts=<%= System.currentTimeMillis() %>"
                     alt="CAPTCHA"
                     style="cursor: pointer; height: 45px;"
                     onclick="refreshCaptcha()"
                     title="Bấm để đổi mã CAPTCHA">
                <button type="button" onclick="refreshCaptcha()"
                        style="background: none; border: none; cursor: pointer; font-size: 20px; color: #007bff;"
                        title="Tải lại CAPTCHA">
                    <i class="fas fa-sync-alt"></i>
                </button>
            </div>



            <button type="submit" class="button login__submit">
                <span class="button__text">Đăng Nhập</span>
                <i class="button__icon fas fa-chevron-right"></i>
            </button>

            <div class="login__options">
                <a href="${pageContext.request.contextPath}/forget-password.jsp" class="login__link">Quên mật khẩu?</a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="login__link">Bạn chưa có tài khoản? Đăng ký</a>
            </div>

            <!-- Google -->
            <div class="login__options" style="text-align: center; margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/google-login" class="button" style="background-color: #db4a39; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none;">
                    <i class="fab fa-google"></i> Đăng nhập với Google
                </a>
            </div>

            <!-- Facebook -->
            <div class="login__options" style="text-align: center; margin-top: 10px;">
                <a href="${pageContext.request.contextPath}/facebook-login" class="button" style="background-color: #3b5998; color: white; padding: 10px 20px; border-radius: 5px;text-decoration: none;">
                    <i class="fab fa-facebook-f"></i> Đăng nhập với Facebook
                </a>
            </div>
        </form>
    </div>
</div>
<script>
    function refreshCaptcha() {
        const img = document.getElementById("captchaImage");
        img.src = img.src.split("?")[0] + "?ts=" + new Date().getTime();
    }
</script>
<%@ include file="footer.jsp" %>

</body>
</html>
