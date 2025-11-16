<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/27/2024
  Time: 12:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shopping cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">

</head>
<body>
<%@include file="header.jsp" %>
<!-- Tiêu đề chính -->
<div class="cart-header">
    <div class="logo">HAND MADE STUDIO</div>
    <div class="title">| Giỏ Hàng</div>
</div>
<!-- Giỏ hàng -->
<div class="cart-container">
    <!-- Tiêu đề cột -->
    <div class="menu-info">
        <div class="checkbox-info"></div>
        <div class="product-info">Sản Phẩm</div>
        <div class="price-info">Đơn Giá</div>
        <div class="quantity-info">Số Lượng</div>
        <div class="total-info">Số Tiền</div>
        <div class="action-info">Thao Tác</div>
    </div>
    <%-- Kiểm tra xem giỏ hàng có trống không --%>
    <c:if test="${isCartEmpty}">
        <div class="alert alert-warning"
             style="color: red; padding: 10px; border: 1px solid red; background-color: #f8d7da; margin-top: 20px;">
                ${message}
        </div>
    </c:if>

    <c:forEach items="${sessionScope.cart.list}" var="cp">
        <!-- Sản phẩm trong giỏ -->
        <div class="cart-item" data-product-id="${cp.id}">
            <div class="checkbox-info">
                <input type="checkbox" class="product-checkbox checkbox-style" ${cp.selected ? 'checked' : ''}>
            </div>

            <div class="product-info">
                <img src="${cp.img}" alt="..." class="product-img">
                <div class="product-detail">
                    <span class="product-name">${cp.name}</span>
                    <span class="stock-quantity">Còn lại: ${cp.stock}</span>
                </div>
            </div>

            <div class="price-info">
                <h4 data-price="${cp.price}"><f:formatNumber value="${cp.price}" pattern="#,##0"/></h4>
            </div>

            <div class="quantity-info">
                <input type="hidden" name="id" value="${cp.id}">
                <button type="button" class="quantity-decrease" onclick="updateQuantity(-1, this)">-</button>
                <input type="number" value="${cp.quantity}" min="1" class="quantity-input" readonly>
                <button type="button" class="quantity-increase" onclick="updateQuantity(1, this)">+</button>
            </div>

            <div class="total-info">
                <h4><f:formatNumber value="${cp.price * cp.quantity}" pattern="#,##0 VND"/></h4>
            </div>

            <div class="action-info">
                <button type="button" class="remove-btn">Xoá</button>
            </div>
        </div>
    </c:forEach>

    <!-- Thanh toán -->
    <div class="cart-summary">
        <div class="left-summary">
            <input type="checkbox" id="select-all" checked> <label for="select-all">Chọn tất cả</label>
            <button id="remove-selected-btn" class="remove-btn">Xoá</button>
        </div>
        <div class="product-total">
            Tổng số lượng: <span id="total-quantity"> ${sessionScope.cart.getTotalQuantityAll()}</span>
            | Tổng tiền: <span id="total-amount"><f:formatNumber value="${sessionScope.cart.total}"
                                                                 pattern="#,##0 VND"/></span>
        </div>

        <form method="GET" action="${pageContext.request.contextPath}/checkout">
            <button type="submit" class="checkout-btn">Thanh Toán</button>
        </form>


    </div>

</div>
<%@include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/cart.js"></script>

</body>
</html>
