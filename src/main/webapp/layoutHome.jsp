<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HandMade</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <script src="${pageContext.request.contextPath}/js/product.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/cart.js" defer></script>
</head>

<body data-context-path="${pageContext.request.contextPath}">

<%-- Giả định header.jsp đã được include ở đây --%>
<%@ include file="header.jsp" %>

<div class="mainBody">

    <%-- Khu vực Banner đã được tối ưu cho JS mới --%>
    <div id="content-banner" class="mainBanner">
        <div class="banner-show">
            <button class="prev" onclick="changeBanner(-1)">&#10094;</button>
            <div class="list-images">
                <img src="${pageContext.request.contextPath}/images/banner-index1.png" class="slide"
                     style="display: none;" alt="">
                <img src="${pageContext.request.contextPath}/images/banner-index2.png" class="slide"
                     style="display: none;" alt="">
                <img src="${pageContext.request.contextPath}/images/banner-index3.png" class="slide"
                     style="display: none;" alt="">
                <img src="${pageContext.request.contextPath}/images/banner-index4.png" class="slide"
                     style="display: none;" alt="">
                <img src="${pageContext.request.contextPath}/images/banner-index5.png" class="slide"
                     style="display: none;" alt="">
            </div>
            <button class="next" onclick="changeBanner(1)">&#10095;</button>
        </div>
    </div>

    <%-- Khu vực Sản phẩm đã được cấu trúc lại --%>
    <div id="product-body-container">
        <h3>SẢN PHẨM CÓ LƯỢT XEM NHIỀU NHẤT</h3>
        <div class="product-list">
            <c:forEach var="p" items="${productViewest}">
                <div class="product-box">
                    <div class="product-id hidden">${p.id}</div>
                    <a href="product-detail?id=${p.id}">
                        <c:if test="${p.discount != 0}">
                            <div class="discount">-${p.discount}%</div>
                        </c:if>
                        <div class="hinh-sp">
                            <img src="${p.img}" alt="${p.name}">
                        </div>
                        <p class="ten-sp">${p.name}</p>
                        <p class="gia-tien">
                            <c:choose>
                                <c:when test="${p.discount > 0}">
                                    <f:formatNumber
                                            value="${p.price - (p.price * p.discount / 100)}"
                                            pattern="#,##0đ"/>
                                    <span class="gia-cu"><f:formatNumber value="${p.price}" pattern="#,##0đ"/></span>
                                </c:when>
                                <c:otherwise>
                                    <f:formatNumber value="${p.price}"
                                                    pattern="#,##0đ"/>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <div class="add">
                            <p class="view">Lượt xem: ${p.view}</p>
                            <button type="button" class="add-to-cart"
                                    style="margin-left: auto">
                                <i class="fa-solid fa-cart-plus"></i>
                            </button>
                            <p class="hidden stock-quantity">Còn lại: ${p.stock}</p>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
        <div id="cart-popup" class="popup hidden">
            <div class="popup-content">
                <p>Sản phẩm đã được thêm vào giỏ hàng thành công!</p>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
<script>

    // --- BANNER LOGIC (Đã thêm Auto-Play và Stop-on-Hover) ---
    let currentIndex = 0;
    let slideInterval;
    const slides = document.querySelectorAll('.slide');
    const bannerContainer = document.querySelector('.mainBanner');

    function showSlide(index) {
        if (slides.length === 0) return;

        currentIndex = (index + slides.length) % slides.length;

        slides.forEach(slide => slide.style.display = 'none');
        if (slides[currentIndex]) {
            slides[currentIndex].style.display = 'block';
        }
    }

    function changeBanner(step) {
        stopAutoPlay(); // Dừng nếu người dùng tương tác
        showSlide(currentIndex + step);
        startAutoPlay(); // Chạy lại sau khi chuyển
    }

    //tự di chuyển banner trong vòng 3 giây khi người dùng không di chuột vào
    function startAutoPlay() {
        if (slideInterval) clearInterval(slideInterval);
        slideInterval = setInterval(() => {
            showSlide(currentIndex + 1);
        }, 4000); // Tự động chuyển sau 3 giây
    }

    function stopAutoPlay() {
        clearInterval(slideInterval);
    }

    document.addEventListener("DOMContentLoaded", function () {
        if (slides.length > 0) {
            showSlide(0); // Hiển thị slide đầu tiên
            startAutoPlay();

            // Dừng/Chạy khi hover
            if (bannerContainer) {
                bannerContainer.addEventListener('mouseenter', stopAutoPlay);
                bannerContainer.addEventListener('mouseleave', startAutoPlay);
            }
        }
    });


</script>
</body>
</html>