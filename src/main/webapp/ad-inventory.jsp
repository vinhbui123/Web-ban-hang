<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/7/2025
  Time: 1:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quản Lý Xuất Nhập Sản Phẩm - Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
    <script>
        window.contextPath = "<%= request.getContextPath() %>";
    </script>
    <script src="${pageContext.request.contextPath}/js/product.js"></script>


</head>
<body>
<%@include file="ad-menu.jsp" %>
<div class="main-content">
    <header>
        <h1>Quản Lý Xuất Nhập Sản Phẩm</h1>

    </header>
    <c:if test="${not empty message}">
        <div class="alert ${messageType}">
            <p>${message}</p>
        </div>
    </c:if>
    <!-- Form Nhập hàng bằng ID -->
    <div style="display: flex; gap: 20px; flex-wrap: wrap; margin-bottom: 20px;">

        <!-- Form Nhập hàng -->
        <c:if test="${sessionScope.user.role.canImport}">
            <form action="${pageContext.request.contextPath}/adminInventory" method="post" class="inventory-form">
                <input type="hidden" name="type" value="import">
                <label><strong>Nhập hàng</strong></label><br>
                <input type="number" name="productId" placeholder="ID sản phẩm" required>
                <input type="number" name="quantity" placeholder="Số lượng nhập" min="1" required>
                <button type="submit" class="btn-import">Nhập hàng</button>
            </form>
        </c:if>

        <!-- Form Xuất hàng -->
        <c:if test="${sessionScope.user.role.canExport}">
            <form action="${pageContext.request.contextPath}/adminInventory" method="post" class="inventory-form">
                <input type="hidden" name="type" value="export">
                <label><strong>Xuất hàng</strong></label><br>
                <input type="number" name="productId" placeholder="ID sản phẩm" required>
                <input type="number" name="quantity" placeholder="Số lượng xuất" min="1" required>
                <button type="submit" class="btn-export">Xuất hàng</button>
            </form>
        </c:if>

    </div>



    <section class="product-management">
        <div class="category-container">
            <h3>Danh mục:</h3>
            <ul class="category-list">
                <li><a href="${pageContext.request.contextPath}/adminInventory">Tất cả</a></li>
                <c:forEach var="category" items="${category}">
                    <li>
                        <a href="${pageContext.request.contextPath}/adminInventory?category=${category.id}">
                                ${category.name}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>


        <table class="product-table">
            <thead>
            <tr>
                <th>Ảnh</th>
                <th>Mã Sản Phẩm</th>
                <th>Tên Sản Phẩm</th>
                <th>Giá</th>
                <th>Số Lượng</th>
                <th>Danh Mục</th> <!-- Thêm id để ẩn -->
                <th>Hành Động</th>
            </tr>
            </thead>
            <div class="product-box">
                <tbody>
                <c:forEach var="product" items="${products}">

                <tr>
                    <td><img src="${product.img}" alt="${product.name}"
                             style="width: 50px; height: 50px; object-fit: cover;"></td>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td><f:formatNumber value="${product.price}" pattern="#,##0đ"/></td>
                    <td>${product.stock}</td>
                    <td>
                        <c:forEach var="category" items="${category}">
                            <c:if test="${category.id == product.catalog_id}">
                                ${category.name}
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <!-- Quyền Nhập hàng -->
                        <!-- Quyền Nhập hàng -->
                        <c:choose>
                            <c:when test="${sessionScope.user.role.canImport}">
                                <button class="btn-import" onclick="openImportModal(${product.id}, '${product.name}')">
                                    Nhập
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn-disabled" onclick="alert('Bạn không có quyền nhập hàng!')">
                                    Nhập
                                </button>
                            </c:otherwise>
                        </c:choose>

                        <!-- Quyền Xuất hàng -->
                        <c:choose>
                            <c:when test="${sessionScope.user.role.canExport}">
                                <button class="btn-export" onclick="openExportModal(${product.id}, '${product.name}')">
                                    Xuất
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn-disabled" onclick="alert('Bạn không có quyền xuất hàng!')">
                                    Xuất
                                </button>
                            </c:otherwise>
                        </c:choose>


                    </td>


                </tr>
            </div>
            </c:forEach>

            </tbody>
        </table>


        <div class="pagination"></div>
    </section>

    <div id="importModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeImportModal()">&times;</span>
            <h2>Nhập Hàng</h2>
            <form id="importForm" action="${pageContext.request.contextPath}/adminInventory" method="post">
                <input type="hidden" id="importProductId" name="productId">
                <input type="hidden" name="type" value="import">

                <label for="importProductName">Tên Sản Phẩm:</label>
                <input type="text" id="importProductName" readonly>

                <label for="importQuantity">Số Lượng Nhập:</label>
                <input type="number" id="importQuantity" name="quantity" min="1" required>

                <button type="submit" class="btn-save">Nhập Hàng</button>
            </form>
        </div>
    </div>

    <div id="exportModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeExportModal()">&times;</span>
            <h2>Xuất Hàng</h2>
            <form id="exportForm" action="${pageContext.request.contextPath}/adminInventory" method="post">
                <input type="hidden" id="exportProductId" name="productId">
                <input type="hidden" name="type" value="export"> <!-- ✅ Gán type cố định là 'export' -->

                <label for="exportProductName">Tên Sản Phẩm:</label>
                <input type="text" id="exportProductName" readonly>

                <label for="exportQuantity">Số Lượng Xuất:</label>
                <input type="number" id="exportQuantity" name="quantity" min="1" required>

                <button type="submit" class="btn-save">Xuất Hàng</button>
            </form>
        </div>
    </div>
</div>
</body>


</html>
