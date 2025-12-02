<%@ page import="vn.edu.hcmuaf.fit.Web_ban_hang.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trang cá nhân</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>

<%@include file="header.jsp" %>

<div class="container">
    <!-- Thông báo -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success" style="color: green; border: 1px solid green; background-color: #e6ffe6; padding: 10px; margin-top: 20px;">
                ${successMessage}
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" style="color: red; border: 1px solid red; background-color: #ffe6e6; padding: 10px; margin-top: 20px;">
                ${errorMessage}
        </div>
    </c:if>

    <!-- Form hồ sơ -->
    <form class="account" method="post" action="${pageContext.request.contextPath}/account" enctype="multipart/form-data">
        <div class="account-header">
            <h3 class="account-title">Hồ sơ của tôi</h3>
            <p class="account-subtitle">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
        </div>
        <hr class="header-line"/>

        <!-- Kiểm tra có tạo thông tin người dùng mới overwrite thông tin cũ không thì lấy thông tin từ session để cập nhật lại -->
        <c:set var="user" value="${requestScope.user != null ? requestScope.user : sessionScope.user}" />

        <div class="account-body">
            <!-- Thông tin tài khoản -->
            <div class="account-details">
                <div class="login__field">
                    <label for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" class="login__input" value="${user.username}" readonly>
                </div>
                <div class="login__field">
                    <label for="fullName">Họ và tên</label>
                    <input type="text" id="fullName" name="fullName" class="login__input" value="${user.firstName} ${user.lastName}">
                </div>
                <div class="login__field">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" class="login__input" value="${user.email}">
                </div>
                <div class="login__field">
                    <label for="phoneNumber">Số điện thoại</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" class="login__input" value="${user.phoneNumber}">
                </div>
                <div class="login__field">
                    <label for="address">Địa chỉ</label>
                    <input type="text" id="address" name="address" class="login__input" value="${user.address}">
                </div>
                <div class="login__field">
                    <label for="bio">Giới thiệu bản thân</label>
                    <textarea id="bio" name="bio" class="login__input" rows="4">${user.bio}</textarea>
                </div>
            </div>

            <!-- Avatar -->
            <div class="avatar-section">
                <div class="avatar-container" id="avatarPreview">
                    <img id="avatarImage"
                         src="${pageContext.request.contextPath}/${user.avatar != null ? user.avatar : 'images/logo.png'}"
                         alt="Avatar"
                         style="width: 120px; height: 120px; object-fit: cover; border-radius: 50%;">
                </div>
                <p>File tối đa 1 MB (.JPEG, .PNG)</p>
                <div class="btn-click">
                    <label class="avatar-upload-label" for="avatarUpload">Chọn ảnh</label>
                    <input type="file" id="avatarUpload" name="avatarUpload" class="avatar-upload" accept="image/*">
                </div>
            </div>
        </div>

        <div class="btn-container">
            <button type="submit" class="btn-save">Lưu</button>
        </div>
    </form>
</div>
<c:if test="${not empty sessionScope.savedCoupons}">
    <div class="discount-main">
        <h3>Mã giảm giá đã lưu</h3>
        <div class="discount-cointainer">
            <c:forEach var="coupon" items="${sessionScope.savedCoupons}">
                <div class="discount-card">
                    <div class="coupon-discount">
                        <c:choose>
                            <c:when test="${coupon.discountValue > 0}">
                                Giảm ${coupon.formattedDiscountValue}
                            </c:when>
                            <c:when test="${coupon.discountPercent > 0}">
                                Giảm ${coupon.discountPercent}%
                            </c:when>
                            <c:otherwise>
                                Ưu đãi đặc biệt
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="discount-requirement">
                        Đơn hàng từ ${coupon.formattedMinOrderAmount}
                    </div>
                    <div class="code">
                        <span class="label">Mã:</span> <span class="discount-code">${coupon.code}</span>
                    </div>
                    <div class="expiration">
                        Hết hạn: ${coupon.formattedExpiredDate}
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<%@include file="footer.jsp" %>

</body>
</html>
