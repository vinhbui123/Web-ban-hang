//import com.google.gson.Gson;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.InventoryDao;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.OrderDao;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.db.DBConnect;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.dto.DetailOrderDTO;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.dto.OrderDTO;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.model.Order;
//import vn.edu.hcmuaf.fit.Nhom24_DoAnWeb.dao.model.OrderDetail;
//
//import java.sql.Connection;
//import java.util.List;
//
//public class MainTest {
//    public static void main(String[] args) {
//
//
//            String json = """
//        {
//          "userId": 11,
//          "status": 1,
//          "freeShipping": 0,
//          "paymentTypeId": 1,
//          "details": [
//            {
//              "productId": 1,
//              "price": 35000,
//              "quantity": 1
//            }
//          ]
//        }
//        """;
//
//            Gson gson = new Gson();
//            OrderDTO order = gson.fromJson(json, OrderDTO.class);
//
//            System.out.println("✅ Kết quả parse:");
//            System.out.println(order);
//
//            if (order.getDetails() == null) {
//                System.out.println("❌ details bị null");
//            } else {
//                System.out.println("✅ Có " + order.getDetails().size() + " sản phẩm");
//                for (DetailOrderDTO d : order.getDetails()) {
//                    System.out.println(d);
//                }
//            }
//        }
//    }
//
