<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/6/2025
  Time: 1:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thanh Toán</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/coupon.css">
</head>
<body>
<%@include file="header.jsp" %>
<header>
    <div class="header-container">
        <div class="logo">HAND MADE STUDIO</div>
        <div class="title">| Thanh Toán</div>
    </div>
</header>
<main>
    <!-- Phần Địa Chỉ Nhận Hàng -->
    <section class="shipping-address">
        <div class="address-header">
            <span>📍 Địa Chỉ Nhận Hàng</span>
            <%@include file="address-form.jsp" %>
        </div>
        <div class="address-details">
            <c:choose>
                <c:when test="${not empty sessionScope.addressDefault}">
                    <c:set var="address" value="${sessionScope.addressDefault}"/>
                    <span style="font-weight: bold">${address.fullName}, SĐT: ${address.phone}</span><br>
                    ${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}
                    <%-- Thêm input hidden chứa JSON địa chỉ mặc định cho JS --%>
                    <%
                        String addressDefaultJson = "";
                        if (session.getAttribute("addressDefault") != null) {
                            addressDefaultJson = new com.google.gson.Gson().toJson(session.getAttribute("addressDefault"));
                        }
                    %>
                    <input type="hidden" id="address-data" value='<%= addressDefaultJson %>'/>
                </c:when>
                <c:otherwise>
                    <em>Không có địa chỉ mặc định được lưu!</em>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <!-- Danh Sách Sản Phẩm -->
    <section class="product-list">
        <!-- Tiêu đề cột -->
        <div class="menu-info">
            <div class="product-info-header">Sản phẩm</div>
            <div class="category-info-header">Phân loại</div>
            <div class="price-info-header">Đơn giá</div>
            <div class="quantity-info-header">Số lượng</div>
            <div class="total-info-header">Thành tiền</div>
        </div>

        <!-- Hiển thị sản phẩm đã chọn -->
        <c:forEach var="product" items="${cart.list}">
            <c:if test="${product.selected}">
                <div class="product-item">
                    <div class="product-info">
                        <div class="product-id" style="display: none;">${product.id}</div>
                        <img src="${product.img}" alt="${product.name}" class="product-image">
                        <div class="product-details">
                            <p class="product-name">${product.name}</p>
                        </div>
                    </div>
                    <div class="category-info">
                        <p class="product-variant"></p>
                    </div>
                    <div class="price-info"><c:out value="${product.price}"/> VND</div>
                    <div class="quantity-info">${product.quantity}</div>
                    <div class="total-info"><c:out value="${product.price * product.quantity}"/> VND</div>
                </div>
            </c:if>
        </c:forEach>

        <div class="shipping-method-section">
            <div class="note-and-shipping">
                <!-- Lời nhắn -->
                <div class="message-box">
                    <label for="message"><strong>Lời nhắn:</strong></label><br>
                    <textarea id="message" placeholder="Lưu ý cho Người bán..." rows="5" required></textarea>
                </div>

                <!-- Phương thức vận chuyển -->
                <div class="shipping-info">
                    <strong>Phương thức vận chuyển:</strong>
                    <c:choose>
                        <c:when test="${empty sessionScope.addressDefault}">
                            <div style="color: red;">
                                <span>Vui lòng cập nhật địa chỉ đơn hàng</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="shipping-methods">
                                <div class="loading"></div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </section>
    <section class="payment-methods">
        <h2>Phương thức thanh toán</h2>
        <div class="payment-tabs">
            <button class="tab active" onclick="showPayment('cod')">Thanh toán khi nhận hàng</button>
            <button class="tab" onclick="showPayment('qr')">QR chuyển khoản</button>
        </div>

        <div class="payment-content" id="cod">
            <p>Bạn đã chọn phương thức <strong>Thanh toán khi nhận hàng</strong>. Vui lòng kiểm tra lại đơn hàng trước
                khi giao.</p>
            <p>Phí thu hộ: ₫0 VNĐ. Ưu đãi về phí vận chuyển (nếu có) áp dụng cả với phí thu hộ.</p>
        </div>

        <div class="payment-content hidden" id="qr">
            <p>Bạn đã chọn phương thức <strong>QR chuyển khoản</strong>.</p>
            <div class="qr-container">
                <img src="${pageContext.request.contextPath}/images/qrcode.png" alt="QR Code" class="qr-code">
                <p>Quét mã QR để thanh toán.</p>
            </div>
        </div>
        <!-- Button mở popup -->
        <div class="coupon-selector" style="display: flex; justify-content: flex-end; margin-bottom: 10px;">
            <button class="open-coupon-popup" style="margin-top: 5px;" onclick="openCouponPopup()">Chọn mã giảm giá
            </button>
        </div>

        <!-- Popup mã giảm giá -->
        <div id="coupon-popup">
            <h3>🎁 Mã giảm giá khả dụng</h3>
            <ul id="coupon-list"></ul>
            <button onclick="closeCouponPopup()">Đóng</button>
        </div>
        <div id="popup-overlay"></div>
        <div class="order-summary">
            <div id="applied-coupon-info" style="text-align: right; margin-bottom: 10px;"></div>
            <p>
            <div class="total-info total-order-value" id="total-order-value"
                 style="display: flex;justify-content: flex-end;margin: 8px 0;font-size: 1em;">
                <span>Tổng cộng:</span> <f:formatNumber value="${sessionScope.cart.total}" pattern="#,#00"/>₫
            </div>
            </p>
            <p>Phí vận chuyển: <span class="shipping-fee">0đ</span></p>
            <p><strong>Tổng thanh toán: <span><f:formatNumber value="${sessionScope.cart.total}"
                                                              pattern="#,##0 VND"/></span></strong></p>
        </div>
        <button class="submit-order" onclick="placeOrder()">Đặt hàng</button>
    </section>
</main>
<input type="hidden" id="selectedCouponCode" name="couponCode" value="">
<div id="couponModalPopup" class="coupon-modal" style="display:none;">
    <div class="coupon-modal-content">
        <span class="coupon-close" onclick="closeCouponPopup()">&times;</span>
        <h3>Danh sách mã giảm giá đã lưu</h3>
        <div class="coupon-list-section">
            <c:choose>
                <c:when test="${not empty sessionScope.savedCoupons}">
                    <c:forEach var="coupon" items="${sessionScope.savedCoupons}">
                        <div class="coupon-card-item">
                            <h4>${coupon.code}</h4>
                            <p>${coupon.description}</p>
                            <p>Giảm:
                                <c:if test="${coupon.discountPercent > 0}">
                                    ${coupon.discountPercent}%
                                    <c:if test="${coupon.maxDiscountValue != null}">
                                        (tối đa ${coupon.formattedDiscountValue})
                                    </c:if>
                                </c:if>
                                <c:if test="${coupon.discountValue > 0}">
                                    ${coupon.formattedDiscountValue}
                                </c:if>
                            </p>
                            <p>Đơn tối thiểu: ${coupon.formattedMinOrderAmount}</p>
                            <p>HSD: ${coupon.formattedExpiredDate}</p>
                            <button onclick="applyCoupon('${coupon.code}')">Dùng mã này</button>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>Bạn chưa có mã giảm giá nào.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>
<script src="${pageContext.request.contextPath}/js/checkout.js"></script>
<script>
    const userId = "${sessionScope.user.id}";
</script>
</body>
</html>
