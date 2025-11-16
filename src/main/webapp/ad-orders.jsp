<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map, java.util.List, java.util.LinkedHashMap, java.util.ArrayList" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
    <title>Quản Lý Đơn Hàng - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</head>
<body>
<%@include file="ad-menu.jsp" %>

<div class="main-content">
    <header>
        <h1>Quản Lý Đơn Hàng</h1>
    </header>

    <section class="order-management">
        <table class="transaction-table">
            <thead>
            <tr>
                <th>Mã Đơn</th>
                <th>Người Đặt</th>
                <th>Sản Phẩm (ID - Tên - SL)</th>
                <th>Tổng Tiền</th>
                <th>Thanh Toán</th>
                <th>Trạng Thái</th>
                <th>Thời gian</th>
                <th>Hành Động</th>
            </tr>
            </thead>

            <%
                // Gom đơn hàng theo order_id trong JSP (không tối ưu nhưng đủ dùng cho hiển thị)
                Map<Integer, List<Map<String, Object>>> groupedOrders = new LinkedHashMap<>();
                List<Map<String, Object>> rawOrders = (List<Map<String, Object>>) request.getAttribute("orderDetails");

                for (Map<String, Object> row : rawOrders) {
                    Integer orderId = (Integer) row.get("order_id");
                    groupedOrders.putIfAbsent(orderId, new ArrayList<>());
                    groupedOrders.get(orderId).add(row);
                }
                request.setAttribute("groupedOrders", groupedOrders);
            %>

            <c:forEach var="entry" items="${groupedOrders}">
                <c:set var="products" value="${entry.value}"/>
                <c:set var="firstRow" value="${products[0]}"/>
                <tr>
                    <td>${firstRow.order_id}</td>
                    <td>${firstRow.username}</td>
                    <td>
                        <div style="line-height: 1.6;">
                            <c:forEach var="p" items="${products}">
                                ID: ${p.product_id} - ${p.product_name} - SL: ${p.quantity}<br/>
                            </c:forEach>
                        </div>
                    </td>
                    <td>
                        <c:set var="total" value="0"/>
                        <c:forEach var="p" items="${products}">
                            <c:set var="total" value="${total + p.total_money}"/>
                        </c:forEach>
                            ${total}
                    </td>
                    <td>${firstRow.payment_code}</td>

                    <td>
                        <c:choose>
                            <c:when test="${firstRow.status == 0}">
                                <span class="status-pending">Đang chờ xác nhận</span>
                            </c:when>
                            <c:when test="${firstRow.status == 1}">
                                <span class="status-confirmed">Đã xác nhận</span>
                            </c:when>
                            <c:when test="${firstRow.status == 2}">
                                <span class="status-shipping">Đang giao hàng</span>
                            </c:when>
                            <c:when test="${firstRow.status == 3}">
                                <span class="status-done">Đã hoàn thành</span>
                            </c:when>
                            <c:when test="${firstRow.status == 4}">
                                <span class="status-cancelled">Đã huỷ</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-unknown">Không rõ</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td style="line-height: 1.5;">
                        <small>📦 Ngày đặt:
                            <fmt:formatDate value="${firstRow.create_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </small><br/>

                        <c:choose>
                            <c:when test="${firstRow.status == 1}">
                                <small>🕒 Ngày xác nhận:
                                    <fmt:formatDate value="${firstRow.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </small>
                            </c:when>
                            <c:when test="${firstRow.status == 2}">
                                <small>🕒 Ngày giao hàng:
                                    <fmt:formatDate value="${firstRow.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </small>
                            </c:when>
                            <c:when test="${firstRow.status == 3}">
                                <small>🕒 Ngày hoàn thành:
                                    <fmt:formatDate value="${firstRow.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </small>
                            </c:when>
                            <c:when test="${firstRow.status == 4}">
                                <small>🕒 Ngày huỷ:
                                    <fmt:formatDate value="${firstRow.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </small>
                            </c:when>
                            <c:otherwise>
                                <small>🕒 Chưa có cập nhật</small>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${firstRow.status == 0 && sessionScope.user.role.canOrder}">
                            <!-- Nút xác nhận -->
                            <form action="${pageContext.request.contextPath}/confirmOrder" method="post" style="margin-bottom: 5px;">
                                <input type="hidden" name="orderId" value="${firstRow.order_id}">
                                <button type="submit" class="btn-confirm"
                                        onclick="return confirm('Xác nhận xử lý đơn hàng này?')">Xác nhận
                                </button>
                            </form>
                        </c:if>

                        <c:if test="${(firstRow.status == 0 || firstRow.status == 1) && sessionScope.user.role.canOrder}">
                            <!-- Nút hủy -->
                            <form action="${pageContext.request.contextPath}/cancelOrder" method="post">
                                <input type="hidden" name="orderId" value="${firstRow.order_id}">
                                <button type="submit" class="btn-cancel"
                                        onclick="return confirm('Bạn chắc chắn muốn hủy đơn này?')">Hủy
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
