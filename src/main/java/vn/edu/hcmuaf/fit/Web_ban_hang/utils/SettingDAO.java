package vn.edu.hcmuaf.fit.Web_ban_hang.utils;

public class SettingDAO {

    public static int toPaymentTypeId(String paymentType) {
        switch (paymentType.toLowerCase()) {
            case "cod": return 1;
            case "qr": return 2;
            default:
                return -1;
        }
    }
    public static String toStatusDetails(int index){
        switch (index) {
            case 1:
                return "Đã xác nhận";
            case 2:
                return "Đang giao hàng";
            case 3:
                return "Đã hoàn thành";
            case 4:
                return "Đã huỷ";
            case 0:
                return "Đang chờ xác nhận";
            default:
                return "Không xác định"; // không xđ
        }
    }

    public static String roleId(int id){
        return switch (id) {
            case 0 -> "user";
            case 1 -> "admin";
            default -> "Unknown";
        };
    }

    // <%--<c:when test="${firstRow.status == 0}">Đang chờ xác nhận</c:when>--%>
    // <%--<c:when test="${firstRow.status == 1}">Đã xác nhận</c:when>--%>
    // <%--<c:when test="${firstRow.status == 2}">Đang giao hàng</c:when>--%>
    // <%--<c:when test="${firstRow.status == 3}">Đã hoàn thành</c:when>--%>
    // <%--<c:when test="${firstRow.status == 4}">Đã huỷ</c:when>--%>

}
