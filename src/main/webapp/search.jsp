<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Yen Huong Admin
  Date: 07/01/2025
  Time: 11:27 SA
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HandMade</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <script src="js/product.js"></script>
    <title>Handmade</title>
</head>
<body>

<%@include file="header.jsp" %>

<div class="content">
    <div class="category">
        <div class="category-menu">
            <span onclick="toggleCategoryMenu()">Danh mục <i class="fa fa-chevron-down" id="arrow-icon"></i></span>
            <ul id="category-list">
                <li><a href="${pageContext.request.contextPath}/home">TRANG CHỦ</a></li>
                <li><a href="${pageContext.request.contextPath}/list-product?category=all">SẢN PHẨM</a></li>
                <c:if test="${not empty category}">
                    <c:forEach var="category" items="${category}">
                        <li>
                            <a href="${pageContext.request.contextPath}/list-product?category=${category.id}">${category.name}</a>
                        </li>
                    </c:forEach>
                </c:if>
            </ul>
        </div>
    </div>
    <div class="product-container">
        <h3 class="type">SẢN PHẨM LIÊN QUAN: ${keyword}</h3>
        <div class="product-list">
            <c:if test="${empty products}">
                <p>Không có sản phẩm hiển thị</p>
            </c:if>
            <c:forEach var="p" items="${products}">
                <div class="product-box" >
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
                                    <f:formatNumber value="${(p.price - (p.price * p.discount / 100))}"
                                                    pattern="#,##0đ"/>
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
        <div class= "pagination"></div>
    </div>
</div>

<%@include file="footer.jsp" %>

</body>
</html>
