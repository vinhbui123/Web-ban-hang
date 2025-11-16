<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Quản Lý Danh Mục - Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
  <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</head>
<body>
<%@include file="ad-menu.jsp" %>

<div class="main-content">
  <header>
    <h1>Quản Lý Danh Mục</h1>
  </header>

  <section class="category-management">
    <c:if test="${sessionScope.user.role.canAdd}">
      <button class="btn-add" onclick="openCategoryModal()">
        <i class="fa-solid fa-plus"></i> Thêm Danh Mục
      </button>
    </c:if>

    <table class="product-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Tên Danh Mục</th>
        <th>Hành Động</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="category" items="${category}">
        <tr>
          <td>${category.id}</td>
          <td>${category.name}</td>
          <td>
            <c:if test="${sessionScope.user.role.canEdit}">
              <button class="btn-edit" onclick="openEditCategoryModal('${category.id}', '${category.name}')">
                <i class="fa-solid fa-pen"></i>
              </button>
            </c:if>

            <c:if test="${sessionScope.user.role.canDelete}">
              <form action="${pageContext.request.contextPath}/removeCategory" method="post" style="display: inline;">
                <input type="hidden" name="categoryId" value="${category.id}">
                <button type="submit" class="btn-delete" onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">
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

  <div id="categoryModal" class="modal">
    <div class="modal-content">
      <span class="close" onclick="closeCategoryModal()">&times;</span>
      <h2>Thêm Danh Mục</h2>
      <form id="categoryForm" action="${pageContext.request.contextPath}/adminCategory" method="post">
        <label for="categoryName">Tên Danh Mục:</label>
        <input type="text" id="categoryName" name="categoryName" required>

        <button type="submit" class="btn-save">Lưu Danh Mục</button>
      </form>
    </div>
  </div>
  <div id="editCategoryModal" class="modal">
    <div class="modal-content">
      <span class="close" onclick="closeEditCategoryModal()">&times;</span>
      <h2>Chỉnh Sửa Danh Mục</h2>
      <form id="editCategoryForm" action="${pageContext.request.contextPath}/editCategory" method="post">
        <input type="hidden" id="editCategoryId" name="categoryId">
        <label for="editCategoryName">Tên Danh Mục:</label>
        <input type="text" id="editCategoryName" name="categoryName" required>
        <button type="submit" class="btn-save">Cập Nhật</button>
      </form>
    </div>
  </div>

</div>


<script>
  function openCategoryModal() {
    document.getElementById("categoryModal").style.display = "block";
  }
  function closeCategoryModal() {
    document.getElementById("categoryModal").style.display = "none";
  }
</script>
<script>
  // Mở modal thêm danh mục
  function openAddCategoryModal() {
    document.getElementById("addCategoryModal").style.display = "block";
  }
  function closeAddCategoryModal() {
    document.getElementById("addCategoryModal").style.display = "none";
  }

  // Mở modal chỉnh sửa danh mục
  function openEditCategoryModal(id, name) {
    document.getElementById("editCategoryId").value = id;
    document.getElementById("editCategoryName").value = name;
    document.getElementById("editCategoryModal").style.display = "block";
  }
  function closeEditCategoryModal() {
    document.getElementById("editCategoryModal").style.display = "none";
  }
</script>

</body>
</html>
