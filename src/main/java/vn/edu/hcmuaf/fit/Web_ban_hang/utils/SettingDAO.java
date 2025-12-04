package vn.edu.hcmuaf.fit.Web_ban_hang.utils;

public class SettingDAO {

    public static int toPaymentTypeId(String paymentType) {
        return switch (paymentType.toLowerCase()) {
            case "cod" -> 1;
            case "qr" -> 2;
            default -> -1;
        };
    }
    public static String toStatusDetails(int index){
        return switch (index) {
            case 1 -> "Đã xác nhận";
            case 2 -> "Đang giao hàng";
            case 3 -> "Đã hoàn thành";
            case 4 -> "Đã huỷ";
            case 0 -> "Đang chờ xác nhận";
            default -> "Không xác định"; // không xđ
        };
    }

    public static String roleId(int id){
        return switch (id) {
            case 0 -> "user";
            case 1 -> "admin";
            default -> "unknown";
        };
    }


}
