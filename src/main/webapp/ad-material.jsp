<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quản Lý Chất Liệu - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</head>
<body>
<%@include file="ad-menu.jsp" %>

<div class="main-content">
    <header>
        <h1>Quản Lý Chất Liệu</h1>
    </header>

    <section class="material-management">
        <c:if test="${sessionScope.user.role.canAdd}">
            <button class="btn-add" onclick="openMaterialModal()">
                <i class="fa-solid fa-plus"></i> Thêm Chất Liệu
            </button>
        </c:if>

        <table class="product-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên Chất Liệu</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="material" items="${materials}">
                <tr>
                    <td>${material.id}</td>
                    <td>${material.name}</td>
                    <td>
                        <c:if test="${sessionScope.user.role.canEdit}">
                            <button class="btn-edit" onclick="openEditMaterialModal('${material.id}', '${material.name}')">
                                <i class="fa-solid fa-pen"></i>
                            </button>
                        </c:if>
                        <c:if test="${sessionScope.user.role.canDelete}">
                            <form action="${pageContext.request.contextPath}/adminMaterials" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${material.id}">
                                <button type="submit" class="btn-delete" onclick="return confirm('Bạn có chắc muốn xóa chất liệu này?');">
                                    <i class="fa-solid fa-trash"></i>
                                </button>
                            </form>
                        </c:if>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>

    <!-- Modal thêm chất liệu -->
    <div id="materialModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeMaterialModal()">&times;</span>
            <h2>Thêm Chất Liệu</h2>
            <form id="materialForm" action="${pageContext.request.contextPath}/adminMaterials" method="post">
                <input type="hidden" name="action" value="add">
                <label for="materialName">Tên Chất Liệu:</label>
                <input type="text" id="materialName" name="name" required>
                <button type="submit" class="btn-save">Lưu Chất Liệu</button>
            </form>
        </div>
    </div>

    <!-- Modal chỉnh sửa chất liệu -->
    <div id="editMaterialModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeEditMaterialModal()">&times;</span>
            <h2>Chỉnh Sửa Chất Liệu</h2>
            <form id="editMaterialForm" action="${pageContext.request.contextPath}/adminMaterials" method="post">
                <input type="hidden" id="editMaterialId" name="id">
                <input type="hidden" name="action" value="update">
                <label for="editMaterialName">Tên Chất Liệu:</label>
                <input type="text" id="editMaterialName" name="name" required>
                <button type="submit" class="btn-save">Cập Nhật</button>
            </form>
        </div>
    </div>

</div>

<script>
    function openMaterialModal() {
        document.getElementById("materialModal").style.display = "block";
    }

    function closeMaterialModal() {
        document.getElementById("materialModal").style.display = "none";
    }

    function openEditMaterialModal(id, name) {
        document.getElementById("editMaterialId").value = id;
        document.getElementById("editMaterialName").value = name;
        document.getElementById("editMaterialModal").style.display = "block";
    }

    function closeEditMaterialModal() {
        document.getElementById("editMaterialModal").style.display = "none";
    }

</script>
</body>
</html>
