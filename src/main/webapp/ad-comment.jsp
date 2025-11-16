<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quản Lý Đánh Giá - Admin</title>
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>
<body>

<%@include file="ad-menu.jsp" %>

<div class="main-content">
    <header>
        <h1>Quản Lý Đánh Giá Sản Phẩm</h1>
    </header>

    <section class="comment-management">
        <table class="product-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Sản phẩm</th>
                <th>Người dùng</th>
                <th>Đánh giá</th>
                <th>Nội dung</th>
                <th>Thời gian</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cmt" items="${comments}">
                <tr>
                    <td>${cmt.id}</td>
                    <td>${cmt.productId}</td>
                    <td>${cmt.userName}</td>
                    <td>${cmt.rating} <i class="fa-solid fa-star" style="color: #f5c518;"></i></td>
                    <td>${cmt.content}</td>
                    <td>${cmt.createdAt}</td>
                    <td>
                        <c:if test="${sessionScope.user.role.canDelete}">
                            <form action="${pageContext.request.contextPath}/adminDelete" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${cmt.id}">
                                <button class="btn-delete" type="submit" onclick="return confirm('Bạn có chắc muốn xoá đánh giá này?');">
                                    <i class="fa-solid fa-trash"></i> Xoá
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</div>

</body>
</html>
