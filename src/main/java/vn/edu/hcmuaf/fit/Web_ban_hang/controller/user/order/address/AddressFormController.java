package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.order.address;

import com.google.gson.Gson;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.AddressService;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/address-form")
public class AddressFormController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // Save Address
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        User user = (User) session.getAttribute("user");
        if (user == null) {
            out.print("{ \"status\": false, \"message\": \"Bạn cần đăng nhập để thực hiện thao tác này.\" }");
            out.flush();
            out.close();
            return;
        }

        try {
            String json = ReadJsonUtil.read(request);
            Gson gson = new Gson();
            Address address = gson.fromJson(json, Address.class);

            address.setUserId(user.getId()); // Gắn user_id từ session
            AddressService addressService = new AddressService();
            boolean success;

            if (address.getId() != null && address.getId() > 0) {
                // Cập nhật
                success = addressService.updateAddress(address);
            } else {
                // Thêm mới
                success = addressService.insertAddressAndSetDefault(address);
            }

            if (success) {
                // Chỉ cập nhật addressDefault nếu địa chỉ mới được đặt làm mặc định
                if (address.isDefault()) {
                    Address newDefault = addressService.getAddressDefault(user.getId());
                    session.setAttribute("addressDefault", newDefault);
                    String jsonResponse = new Gson().toJson(newDefault);
                    // Trả về cả thông tin addressDefault mới
                    out.print("{ \"status\": true, \"message\": \"Lưu địa chỉ thành công.\" , \"addressDefault\": "
                            + jsonResponse + " }");
                } else {
                    out.print("{ \"status\": true, \"message\": \"Lưu địa chỉ thành công.\" }");
                }

            } else {
                out.print("{ \"status\": false, \"message\": \"Không thể lưu địa chỉ.\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{ \"status\": false, \"message\": \"Đã xảy ra lỗi khi xử lý địa chỉ.\" }");
        } finally {
            out.flush();
            out.close();
        }
    }
}
