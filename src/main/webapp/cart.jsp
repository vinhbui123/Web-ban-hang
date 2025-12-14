<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Giỏ Hàng - HandMade</title>
                <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese"
                    rel="stylesheet">
                <link rel="stylesheet"
                    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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

                        <!-- Thông báo lỗi nếu có -->
                        <c:if test="${param.error == 'outofstock'}">
                            <div class="alert alert-warning"
                                style="color: red; padding: 10px; border: 1px solid red; background-color: #f8d7da; margin-top: 20px;">
                                Không đủ hàng trong kho!
                            </div>
                        </c:if>

                        <!-- Kiểm tra xem giỏ hàng có trống không -->
                        <c:choose>
                            <c:when test="${isCartEmpty or empty sessionScope.cart or empty sessionScope.cart.list}">
                                <div class="empty-cart" style="text-align: center; padding: 50px;">
                                    <p><i class="fas fa-shopping-cart" style="font-size: 3em; color: #ccc;"></i></p>
                                    <p>Giỏ hàng của bạn đang trống.</p>
                                    <a href="${pageContext.request.contextPath}/list-product"
                                        class="continue-shopping-btn"
                                        style="display: inline-block; padding: 10px 20px; background-color: #5a9153; color: white; text-decoration: none; border-radius: 5px; margin-top: 10px;">
                                        <i class="fas fa-arrow-left"></i> Tiếp tục mua sắm
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${sessionScope.cart.list}" var="cp">
                                    <!-- Sản phẩm trong giỏ -->
                                    <div class="cart-item" data-product-id="${cp.id}">
                                        <div class="checkbox-info">
                                            <input type="checkbox" class="product-checkbox" ${cp.selected ? 'checked'
                                                : '' }>
                                        </div>

                                        <div class="product-info">
                                            <img src="${cp.img}" alt="${cp.name}" class="product-img">
                                            <div class="product-detail">
                                                <span class="product-name">${cp.name}</span>
                                                <span class="stock-quantity">Còn lại: ${cp.stock}</span>
                                            </div>
                                        </div>

                                        <div class="price-info">
                                            <h4>
                                                <c:choose>
                                                    <c:when test="${cp.discount > 0}">
                                                        <f:formatNumber
                                                            value="${cp.price - (cp.price * cp.discount / 100)}"
                                                            pattern="#,##0đ" />
                                                        <span class="gia-cu"
                                                            style="text-decoration: line-through; color: #999; font-size: 0.8em;">
                                                            <f:formatNumber value="${cp.price}" pattern="#,##0đ" />
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <f:formatNumber value="${cp.price}" pattern="#,##0đ" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </h4>
                                        </div>

                                        <div class="quantity-info">
                                            <form action="${pageContext.request.contextPath}/cart" method="post"
                                                class="quantity-form" style="display: flex; align-items: center;">
                                                <input type="hidden" name="action" value="update">
                                                <input type="hidden" name="id" value="${cp.id}">
                                                <button type="submit" name="quantity" value="${cp.quantity - 1}"
                                                    class="quantity-btn" ${cp.quantity <=1 ? 'disabled' : ''
                                                    }>-</button>
                                                <input type="text" value="${cp.quantity}" readonly
                                                    class="quantity-input" style="width: 40px; text-align: center;">
                                                <button type="submit" name="quantity" value="${cp.quantity + 1}"
                                                    class="quantity-btn" ${cp.quantity>= cp.stock ? 'disabled' :
                                                    ''}>+</button>
                                            </form>
                                        </div>

                                        <div class="total-info">
                                            <h4>
                                                <c:choose>
                                                    <c:when test="${cp.discount > 0}">
                                                        <f:formatNumber
                                                            value="${(cp.price - (cp.price * cp.discount / 100)) * cp.quantity}"
                                                            pattern="#,##0đ" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <f:formatNumber value="${cp.price * cp.quantity}"
                                                            pattern="#,##0đ" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </h4>
                                        </div>

                                        <div class="action-info">
                                            <a href="${pageContext.request.contextPath}/cart?action=remove&id=${cp.id}"
                                                class="remove-btn"
                                                onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                                Xoá
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>

                                <!-- Thanh toán -->
                                <div class="cart-summary">
                                    <div class="left-summary">
                                        <input type="checkbox" id="select-all" checked>
                                        <label for="select-all">Chọn tất cả</label>
                                    </div>
                                    <div class="product-total">
                                        Tổng số lượng: <span>${sessionScope.cart.totalQuantityAll}</span>
                                        | Tổng tiền: <span>
                                            <f:formatNumber value="${sessionScope.cart.totalWithDiscount}"
                                                pattern="#,##0đ" />
                                        </span>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">
                                        Thanh Toán
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <%@include file="footer.jsp" %>

            </body>

            </html>