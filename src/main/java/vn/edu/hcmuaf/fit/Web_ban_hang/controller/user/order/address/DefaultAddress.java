package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.order.address;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.AddressService;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(value = "/default-address")
public class DefaultAddress extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        try {
            String jsonData = ReadJsonUtil.read(request);
            Gson gson = new Gson();
            Map<String, String> data = gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {
            }.getType());

            int addressId = Integer.parseInt(data.get("addressId"));
            User user = (User) session.getAttribute("user");

            if (user == null) {
                out.print("{ \"status\": false, \"message\": \"Phiên đăng nhập đã hết hạn.\" }");
                return;
            }

            AddressService addressService = new AddressService();
            Address selectedAddress = addressService.getAddressById(addressId);

            if (selectedAddress == null || selectedAddress.getUserId() != user.getId()) {
                out.print("{ \"status\": false, \"message\": \"Địa chỉ không hợp lệ.\" }");
                return;
            }

            boolean updated = addressService.setDefault(selectedAddress);
            if (updated) {
                selectedAddress.setDefault(true);
                session.setAttribute("addressDefault", selectedAddress);
                String jsonResponse = new Gson().toJson(selectedAddress);
                out.print("{ \"status\": true, \"message\": \"Lưu địa chỉ thành công.\", \"addressDefault\": "
                        + jsonResponse + " }");
            } else {
                out.print("{ \"status\": false, \"message\": \"Không cập nhật được địa chỉ mặc định.\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{ \"status\": false, \"message\": \"Lỗi xử lý: " + e.getMessage() + "\" }");
        } finally {
            out.flush();
            out.close();
        }
    }
}
