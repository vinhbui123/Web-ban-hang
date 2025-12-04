<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <script src="${pageContext.request.contextPath}/js/product.js"></script>
</head>
<body>

<%@ include file="header.jsp" %>
<div class="mainBody">
    <div id="content-banner" class="mainBanner">
        <div class="banner-show">
            <button class="prev" onclick="changeBanner(-1)">&#10094;</button>
            <div class="list-images">
                <img src="${pageContext.request.contextPath}/images/banner-index1.png" class="slide"
                     style="display: block;">
                <img src="${pageContext.request.contextPath}/images/banner-index2.png" class="slide"
                     style="display: none;">
                <img src="${pageContext.request.contextPath}/images/banner-index3.png" class="slide"
                     style="display: none;">
                <img src="${pageContext.request.contextPath}/images/banner-index4.png" class="slide"
                     style="display: none;">
                <img src="${pageContext.request.contextPath}/images/banner-index5.png" class="slide"
                     style="display: none;">
            </div>
            <button class="next" onclick="changeBanner(1)">&#10095;</button>
        </div>
    </div>
    <div id="product-body-container">
        <h3>SẢN PHẨM CÓ LƯỢT XEM NHIỀU NHẤT</h3>
        <div class="product-list">
            <c:forEach var="p" items="${productViewest}">
                <div class="product-box">
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
                                    <f:formatNumber value="${p.price - (p.price * p.discount / 100)}" pattern="#,##0đ"/>
                                    <span class="gia-cu"><f:formatNumber value="${p.price}" pattern="#,##0đ"/></span>
                                </c:when>
                                <c:otherwise>
                                    <f:formatNumber value="${p.price}" pattern="#,##0đ"/>
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
</div>
<%@ include file="footer.jsp" %>

<script>
    // --- BANNER LOGIC (Moved outside saveCoupon and made global) ---
    let currentIndex = 0;
    const slides = document.querySelectorAll('.slide');

    // This function must be GLOBAL for the HTML buttons to call it
    function changeBanner(step) {
        if (slides.length === 0) return;

        // Hide current slide
        if (slides[currentIndex]) {
            slides[currentIndex].style.display = "none";
        }

        // Calculate new index
        currentIndex = (currentIndex + step + slides.length) % slides.length;

        // Show new slide
        if (slides[currentIndex]) {
            slides[currentIndex].style.display = "block";
        }
    }

    // Initialize the banner when the page loads
    document.addEventListener("DOMContentLoaded", function () {
        if (slides.length > 0) {
            // Ensure only the first slide is visible initially (redundant but safe)
            for (let i = 0; i < slides.length; i++) {
                slides[i].style.display = "none";
            }
            slides[0].style.display = "block";
            currentIndex = 0;
        }
    });
</script>
</body>
</html>