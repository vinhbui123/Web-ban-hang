<%--
  Created by IntelliJ IDEA.
  User: Yen Huong Admin
  Date: 26/12/2024
  Time: 10:12 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Chi tiết sản phẩm</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/product_detail.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/comment.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="flex-box">
    <div class="left">
        <div class="big-img">
            <!-- Khung chứa hình ảnh lớn -->
            <img id="main-img" src="${product.img}" alt="${product.name}">
        </div>
        <div class="image">
            <div class="small-img">
                <img src="${product.img}" onclick="showImg(this.src)"alt="Product image" >
            </div>
            <!-- Lặp qua các hình ảnh phụ -->
            <c:forEach var="image" items="${product.subImg}">
                <div class="small-img">
                    <!-- Khi nhấp vào hình ảnh nhỏ, nó sẽ cập nhật hình ảnh lớn -->
                    <img src="${image}" onclick="showImg(this.src)" alt="Product image">
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="right">
        <div class="breadcrumb">
            <ul>
                <li><a href="home">Trang chủ > </a></li>
                <li><a href="phu-kien.html"> Phụ kiện &gt; </a></li>
                <li>Chi tiết sản phẩm</li>
            </ul>
        </div>
        <div class="name">${product.name}</div>
        <div class="ratings">
            <i class="fas fa-star"></i>
            <i class="fas fa-star"></i>
            <i class="fas fa-star"></i>
            <i class="fas fa-star"></i>
            <i class="fas fa-star-half-alt"></i>
        </div>
        <p class="price">
            <c:choose>
                <c:when test="${product.discount > 0}">
                    <f:formatNumber value="${(product.price - (product.price * product.discount / 100))}" pattern="#,##0đ" />
                    <span class="gia-cu"><f:formatNumber value="${product.price}" pattern="#,##0đ" /></span>
                </c:when>
                <c:otherwise>
                    <f:formatNumber value="${product.price}" pattern="#,##0đ" />
                </c:otherwise>
            </c:choose>
        </p>
        <div class="color-selector">
            <p>Màu sắc :</p>
            <c:forEach var="color" items="${product.colors}" varStatus="status">
                <div class="color-item ${status.index == 0 ? 'active' : ''}">
                        ${color.name}
                </div>
            </c:forEach>


        </div>
        <div class="quantity">
            <p>Số lượng :</p>
            <!-- Đảm bảo input có name="quantity" để servlet nhận được -->
            <input type="number" name="quantity" min="1" value="1" id="quantity-input">
        </div>
        <div class="stock-quantity">
            Còn lại: ${product.stock}
        </div>
        <div class="btn-box">
            <!-- Đưa nút 'Thêm Vào Giỏ Hàng' vào trong form -->
            <input id="product-id" type="hidden" name="id" value="${product.id}">
            <button class="cart-btn">
                <i class="fa-solid fa-cart-plus"></i>Thêm Vào Giỏ Hàng
            </button>
            <button class="buy-btn">Mua Ngay</button>
        </div>
    </div>
</div>
<div class="describe-container">
    <h2>MÔ TẢ CHI TIẾT</h2>
    <p class="material">
        <strong>Chất liệu:</strong>
        <c:forEach var="material" items="${product.materials}" varStatus="status">
            <span>${material.name}</span>
            <c:if test="${!status.last}">, </c:if>
        </c:forEach>
    </p>
    <p class="description-text">${product.description}</p>
</div>

<div class="comment-section">
    <h2>ĐÁNH GIÁ SẢN PHẨM</h2>
    
    <div class="rating-overview">
        <div class="rating-number">
            <c:choose>
                <c:when test="${commentCount > 0}">
                    <fmt:formatNumber value="${averageRating}" pattern="#.0"/>
                </c:when>
                <c:otherwise>0.0</c:otherwise>
            </c:choose>
        </div>
        <div class="rating-stars">
            <c:forEach begin="1" end="5" var="i">
                <c:choose>
                    <c:when test="${i <= averageRating}">
                        <i class="fas fa-star"></i>
                    </c:when>
                    <c:when test="${i - 0.5 <= averageRating}">
                        <i class="fas fa-star-half-alt"></i>
                    </c:when>
                    <c:otherwise>
                        <i class="far fa-star"></i>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <span class="comment-count">${commentCount} đánh giá</span>
    </div>

    <c:if test="${sessionScope.user != null}">
        <div class="comment-form">
            <form action="comment" method="post">
                <input type="hidden" name="productId" value="${product.id}">
                <div class="rating-select">
                    <label for="rating">Đánh giá của bạn</label>
                    <select name="rating" id="rating" required>
                        <option value="5">5 sao - Rất tốt</option>
                        <option value="4">4 sao - Tốt</option>
                        <option value="3">3 sao - Bình thường</option>
                        <option value="2">2 sao - Tệ</option>
                        <option value="1">1 sao - Rất tệ</option>
                    </select>
                </div>
                <textarea name="content" placeholder="Chia sẻ nhận xét của bạn về sản phẩm..." required></textarea>
                <button type="submit" class="submit-comment">Gửi đánh giá</button>
            </form>
        </div>
    </c:if>
    <c:if test="${sessionScope.user == null}">
        <div class="login-notice">
            Vui lòng <a href="login">đăng nhập</a> để viết đánh giá
        </div>
    </c:if>

    <div class="comment-list">
        <c:choose>
            <c:when test="${not empty comments}">
                <c:forEach var="comment" items="${comments}">
                    <div class="comment-item">
                        <div class="comment-header">
                            <span class="comment-user">${comment.userName}</span>
                            <span class="comment-date">
                                <fmt:formatDate value="${comment.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                            </span>
                        </div>
                        <div class="comment-rating">
                            <c:forEach begin="1" end="${comment.rating}">
                                <i class="fas fa-star"></i>
                            </c:forEach>
                            <c:forEach begin="${comment.rating + 1}" end="5">
                                <i class="far fa-star"></i>
                            </c:forEach>
                        </div>
                        <div class="comment-content">
                            ${comment.content}
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-comments">
                    Chưa có đánh giá nào cho sản phẩm này
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="product-other">
    <h5>Sản phẩm khác</h5>
    <div class="product-list">
        <c:forEach var="p" items="${products}">
            <div class="product-box">
                <a href="product-detail?id=${p.id}">
                    <div class="hinh-sp">
                        <img src="${p.img}" alt="${p.name}">
                    </div>
                    <p class="ten-sp">${p.name}  </p>
                    <p class="gia-tien">
                        <c:choose>
                            <c:when test="${p.discount > 0}">
                                <f:formatNumber value="${(p.price - (p.price * p.discount / 100))}" pattern="#,##0đ" />
                                <span class="gia-cu"><f:formatNumber value="${p.price}" pattern="#,##0đ" /></span>
                            </c:when>
                            <c:otherwise>
                                <f:formatNumber value="${p.price}" pattern="#,##0đ" />
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <div class="add">
                        <p class="view">Lượt xem: ${p.view}</p>
                        <a href="add-cart?id=${p.id}">
                            <button type="button" class="add-to-cart"><i class="fa-solid fa-cart-plus"></i></button>
                        </a>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>
</div>
<div id="cart-popup" class="popup hidden">
    <div class="popup-content">
        <p>🛒 Sản phẩm đã được thêm vào giỏ hàng thành công!</p>
    </div>
</div>
<%@include file="footer.jsp" %>
<script src="${pageContext.request.contextPath}/js/product_detail.js"></script>
</body>
</html>