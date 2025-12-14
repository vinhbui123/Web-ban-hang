<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>HandMade</title>
                <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese"
                    rel="stylesheet">
                <link rel="stylesheet"
                    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/filter.css">
                <script src="${pageContext.request.contextPath}/js/product.js" defer></script>
                <script src="${pageContext.request.contextPath}/js/cart.js" defer></script>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/top-product.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/comment.css">


            </head>

            <body data-context-path="${pageContext.request.contextPath}">
                <%@include file="header.jsp" %>

                    <div class="content">

                        <!-- ===== FORM LỌC SẢN PHẨM ===== -->
                        <div class="filter-box">
                            <form method="get" action="${pageContext.request.contextPath}/list-product">
                                <div class="filter-group">
                                    <label>Danh mục:</label>
                                    <select name="category">
                                        <option value="all" ${param.category=='all' ? 'selected' : '' }>Tất cả sản phẩm
                                        </option>
                                        <c:forEach var="cat" items="${requestScope.categories}">
                                            <option value="${cat.id}" ${param.category==cat.id.toString() ? 'selected'
                                                : '' }>${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="filter-group">
                                    <label>Giá tối thiểu:</label>
                                    <input type="number" name="minPrice" value="${param.minPrice}"
                                        placeholder="VD: 10000" min="0" step="1000" />
                                </div>

                                <div class="filter-group">
                                    <label>Giá tối đa:</label>
                                    <input type="number" name="maxPrice" value="${param.maxPrice}"
                                        placeholder="VD: 50000" min="0" step="1000" />
                                </div>



                                <div class="filter-buttons">
                                    <button type="submit" class="btn-filter">
                                        <i class="fas fa-filter"></i> Lọc
                                    </button>
                                    <a href="${pageContext.request.contextPath}/list-product?category=all"
                                        class="btn-reset">
                                        <i class="fas fa-undo"></i> Đặt lại
                                    </a>
                                </div>
                            </form>
                        </div>

                        <div class="product-container">
                            <div class="top-product-header">
                                <h3 class="type">${categoryName}</h3>
                                <div>
                                    <a href="${pageContext.request.contextPath}/list-product?type=top-selling">
                                        Sản phẩm nổi bật
                                    </a>
                                    <a href="${pageContext.request.contextPath}/list-product?type=top-viewed">
                                        Xem nhiều nhất
                                    </a>
                                </div>
                            </div>
                            <div class="product-list">
                                <c:forEach var="p" items="${products}">
                                    <div class="product-box">
                                        <div class="product-id hidden">${p.id}</div>
                                        <a href="product-detail?id=${p.id}">
                                            <c:if test="${p.discount !=0}">
                                                <div class="discount">-${p.discount}%</div>
                                            </c:if>
                                            <div class="hinh-sp">
                                                <img src="${p.img}" alt="${p.name}">
                                            </div>
                                            <p class="ten-sp">${p.name} </p>
                                            <p class="gia-tien">
                                                <c:choose>
                                                    <c:when test="${p.discount > 0}">
                                                        <f:formatNumber
                                                            value="${(p.price - (p.price * p.discount / 100))}"
                                                            pattern="#,##0đ" />
                                                        <span class="gia-cu">
                                                            <f:formatNumber value="${p.price}" pattern="#,##0đ" />
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <f:formatNumber value="${p.price}" pattern="#,##0đ" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>

                                            <div class="add">
                                                <p class="view">Lượt xem: ${p.view}</p>
                                                <button type="button" class="add-to-cart" style="margin-left: auto">
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
                            <div class="pagination"></div>
                        </div>
                    </div>

                    <%@include file="footer.jsp" %>

            </body>

            </html>