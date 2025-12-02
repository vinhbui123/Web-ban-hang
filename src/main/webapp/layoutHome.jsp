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

    <script src="${pageContext.request.contextPath}/js/product.js"></script>
</head>
<body>

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
            <%-- Có thể thêm Dots/Pagination ở đây nếu cần --%>
        </div>
    </div>

    <%-- Khu vực Sản phẩm đã được cấu trúc lại --%>
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

<%-- Giả định header.jsp đã được include ở đây --%>
<%@ include file="footer.jsp" %>

<%-- Script Logic được đặt ở đây hoặc tốt nhất là chuyển sang js/product.js --%>
<script>

    // --- COUPON LOGIC (Cải thiện UX bằng cách thay đổi text nút) ---
    function saveCoupon(button, code) {
        navigator.clipboard.writeText(code).then(() => {
            // Thay đổi text nút thành 'Đã sao chép'
            const originalText = button.innerHTML;
            button.innerHTML = 'Đã sao chép';

            fetch('${pageContext.request.contextPath}/save-coupon', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: 'code=' + encodeURIComponent(code)
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        // Giữ trạng thái 'Đã sao chép' trong 1.5 giây
                        setTimeout(() => {
                            button.innerHTML = originalText;
                        }, 1500);
                    } else {
                        console.error("Lỗi khi lưu mã: " + data.message);
                        alert("Thất bại: " + data.message);
                        button.innerHTML = originalText; // Trả về nếu thất bại
                    }
                })
                .catch(() => {
                    alert("Có lỗi xảy ra khi lưu coupon.");
                    button.innerHTML = originalText; // Trả về nếu có lỗi mạng
                });
        });
    }

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

    // --- PRODUCT ADD TO CART LOGIC (AJAX) ---
    function addToCart(button, productId) {
        // Tắt nút và hiển thị loading/icon
        const originalIcon = button.innerHTML;
        button.disabled = true;
        button.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i>'; // Icon loading

        fetch('${pageContext.request.contextPath}/add-cart-ajax', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'id=' + encodeURIComponent(productId)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Thành công: Hiển thị icon checkmark
                    button.innerHTML = '<i class="fa-solid fa-check"></i>';

                    // Trả về icon ban đầu sau 1 giây
                    setTimeout(() => {
                        button.disabled = false;
                        button.innerHTML = originalIcon;
                    }, 1000);
                } else {
                    alert('Lỗi: ' + data.message);
                    button.disabled = false;
                    button.innerHTML = originalIcon;
                }
            })
            .catch(error => {
                console.error('Lỗi AJAX:', error);
                alert('Có lỗi mạng xảy ra.');
                button.disabled = false;
                button.innerHTML = originalIcon;
            });
    }

</script>
</body>
</html>