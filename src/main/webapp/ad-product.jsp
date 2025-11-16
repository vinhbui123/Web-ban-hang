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
    <title>Quản Lý Sản Phẩm - Admin</title>
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
        <h1>Quản Lý Sản Phẩm</h1>

    </header>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert ${sessionScope.messageType}">
            <p>${sessionScope.message}</p>
        </div>
        <c:remove var="message" scope="session"/>
        <c:remove var="messageType" scope="session"/>
    </c:if>

    <section class="product-management">
        <c:choose>
            <c:when test="${sessionScope.user.role == 1}">
                <button class="btn-add" onclick="openModal('add')">
                    <i class="fa-solid fa-plus"></i> Thêm Sản Phẩm
                </button>
            </c:when>
            <c:otherwise>
                <button class="btn-add disabled" style=" opacity: 0.5; cursor: not-allowed; pointer-events: auto;"
                        onclick="alert('Bạn không có quyền thêm sản phẩm!')">
                    <i class="fa-solid fa-plus"></i> Thêm Sản Phẩm
                </button>
            </c:otherwise>
        </c:choose>

        <div class="category-container">
            <h3>Danh mục:</h3>
            <ul class="category-list">
                <li><a href="${pageContext.request.contextPath}/adminProduct">Tất cả</a></li>
                <c:forEach var="category" items="${category}">
                    <li>
                        <a href="${pageContext.request.contextPath}/adminProduct?category=${category.id}">
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
                <th>ID</th>
                <th>Tên Sản Phẩm</th>
                <th>Giá</th>
                <th>Số Lượng</th>
                <th>Danh Mục</th>
                <th>Chất Liệu</th><!-- Thêm id để ẩn -->
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
                        <c:forEach var="m" items="${product.materials}">
                            <li>${m.name}</li>
                        </c:forEach>

                    </td>
                    <td>
                        <!-- Quyền Sửa -->
                        <c:choose>
                            <c:when test="${sessionScope.user.role == 1}">
                                <i class="fa-solid fa-pen-to-square btn-edit"
                                   onclick="openModal('edit', ${product.id})"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fa-solid fa-pen-to-square btn-disabled"
                                   onclick="alert('Bạn không có quyền chỉnh sửa sản phẩm!');"></i>
                            </c:otherwise>
                        </c:choose>

                        <!-- Quyền Xoá -->
                        <c:choose>
                            <c:when test="${sessionScope.user.role == 1}">
                                <form action="${pageContext.request.contextPath}/adminRemove" method="post"
                                      style="display: inline;">
                                    <input type="hidden" name="productId" value="${product.id}">
                                    <button type="submit" class="btn-delete"
                                            onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?');">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <i class="fa-solid fa-trash btn-disabled"
                                   onclick="alert('Bạn không có quyền xoá sản phẩm!');"></i>
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


    <!-- Modal Thêm Sản Phẩm -->
    <div id="addProductModal" class="modal">
        <div class="modal-content ">
            <span class="close" onclick="closeModal('addProductModal')">&times;</span>
            <h2>Thêm Sản Phẩm</h2>
            <form id="addProductForm" action="${pageContext.request.contextPath}/adminAdd" method="post"
                  enctype="multipart/form-data">
                <label for="addProductName">Tên Sản Phẩm:</label>
                <input type="text" id="addProductName" name="name" placeholder="Nhập tên sản phẩm" required>

                <label for="addPrice">Giá:</label>
                <input type="number" id="addPrice" name="price" placeholder="Nhập giá sản phẩm" required>

                <label for="addQuantity">Số Lượng:</label>
                <input type="number" id="addQuantity" name="quantity" placeholder="Nhập số lượng sản phẩm" required>
                <label>Chất liệu:</label>
                <div class="material-checkbox-group">
                    <c:forEach var="m" items="${materials}">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="materialIds" value="${m.id}" id="material_${m.id}">
                            <label class="form-check-label" for="material_${m.id}">${m.name}</label>
                        </div>
                    </c:forEach>
                </div>


                <label for="addCategory">Danh Mục:</label>
                <select id="addCategory" name="category" required>
                    <option value="">Chọn Danh Mục</option>
                    <c:forEach var="category" items="${category}">
                        <option value="${category.id}">${category.name}</option>
                    </c:forEach>
                </select>

                <label for="addDescription">Mô Tả:</label>
                <input type="text" id="addDescription" name="description" placeholder="Nhập mô tả sản phẩm" required>

                <label for="addImage">Ảnh sản phẩm:</label>
                <!-- Thẻ input ảnh -->
                <input type="file" name="image" accept="image/*" required><br>

                <!-- Ảnh xem trước -->
                <div style="position: relative; display: inline-block;">
                    <img id="addPreviewImage" src="" alt="Ảnh xem trước"
                         style="max-width: 150px; margin-top: 10px; border: 1px solid #ccc; border-radius: 8px;">

                    <!-- Nút x để xóa ảnh -->
                    <span id="removeImageBtn"
                          style="position: absolute; top: 0; right: 0; background: red; color: white; padding: 2px 6px; border-radius: 50%; cursor: pointer; font-weight: bold; display: none;">&times;</span>
                </div>

                <button type="submit" class="btn-save">Lưu Sản Phẩm</button>
            </form>
        </div>
    </div>

    <!-- Modal Sửa Sản Phẩm -->
    <div id="editProductModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal('editProductModal')">&times;</span>
            <h2>Sửa Sản Phẩm</h2>
            <form id="editProductForm" method="post" enctype="multipart/form-data">
                <input type="hidden" id="editProductId" name="productId">

                <label for="editProductName">Tên Sản Phẩm:</label>
                <input type="text" id="editProductName" name="name" required>

                <label for="editPrice">Giá:</label>
                <input type="number" id="editPrice" name="price" required>
                <label for="editCategory">Danh Mục:</label>
                <select id="editCategory" name="category" required>
                    <option value="">Chọn Danh Mục</option>
                    <c:forEach var="category" items="${category}">
                        <option value="${category.id}">${category.name}</option>
                    </c:forEach>
                </select>
                <label>Chất liệu:</label>
                <div class="material-checkbox-group" id="editMaterialGroup">
                    <c:forEach var="m" items="${materials}">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="materialIds" value="${m.id}" id="edit_material_${m.id}">
                            <label class="form-check-label" for="edit_material_${m.id}">${m.name}</label>
                        </div>
                    </c:forEach>
                </div>



                <label for="editDescription">Mô Tả:</label>
                <input type="text" id="editDescription" name="description" required>

                <label for="editImage">Ảnh sản phẩm:</label>
                <input type="file" name="image" accept="image/*"><br>

                <!-- Hiển thị ảnh hiện tại -->
                <img id="editPreviewImage" src="" alt="Ảnh hiện tại" style="max-width: 150px; margin-top: 10px;">

                <!-- Lưu tên ảnh cũ -->
                <input type="hidden" name="oldImage" id="editOldImage">


                <button type="submit" class="btn-save">Cập Nhật Sản Phẩm</button>
            </form>
        </div>
    </div>
</div>
</body>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const fileInput = document.querySelector("input[name='image']");
        const previewImage = document.getElementById("addPreviewImage");
        const removeBtn = document.getElementById("removeImageBtn");

        fileInput.addEventListener("change", function () {
            const file = fileInput.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    previewImage.src = e.target.result;
                    removeBtn.style.display = "block";
                };
                reader.readAsDataURL(file);
            } else {
                previewImage.src = "";
                removeBtn.style.display = "none";
            }
        });

        removeBtn.addEventListener("click", function () {
            fileInput.value = ""; // Xóa file đã chọn
            previewImage.src = ""; // Xóa ảnh hiển thị
            removeBtn.style.display = "none";
        });
    });
</script>

</html>
