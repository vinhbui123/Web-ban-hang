<%--
  Created by IntelliJ IDEA.
  User: NguyenDucHuy
  Date: 10/05/2025
  Time: 11:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/purchase-detail.css">
<script src="${pageContext.request.contextPath}/js/purchase-detail.js"></script>


<i class="fa-solid fa-list-ul status-list-icon" onclick="openPurchaseDetailPopup()"
   style="cursor: pointer; margin-left: 5px;"></i>

<!-- Popup (ẩn khi chưa mở) -->
<div id="purchaseModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closePurchaseDetailPopup()">&times;</span>


    </div>
</div>