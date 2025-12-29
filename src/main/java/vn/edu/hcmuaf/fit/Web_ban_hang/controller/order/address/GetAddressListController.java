package vn.edu.hcmuaf.fit.Web_ban_hang.controller.order.address;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.AddressService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/get-address-list")
public class GetAddressListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                out.print("{ \"status\": false, \"message\": \"Phiên đăng nhập đã hết hạn.\" }");
                return;
            }

            AddressService addressService = new AddressService();
            List<Address> addressList = addressService.getAddressByIdUser(user.getId());

            // Trả về JSON response
            Map<String, Object> result = new HashMap<>();
            result.put("status", true);
            result.put("addressList", addressList);
            out.print(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{ \"status\": false, \"message\": \"Lỗi xử lý: " + e.getMessage() + "\" }");
        } finally {
            out.flush();
            out.close();
        }
    }
}
