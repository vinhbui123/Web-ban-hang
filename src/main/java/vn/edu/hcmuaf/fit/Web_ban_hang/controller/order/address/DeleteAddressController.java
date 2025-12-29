package vn.edu.hcmuaf.fit.Web_ban_hang.controller.order.address;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.AddressService;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(value = "/delete-address")
public class DeleteAddressController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        String jsonData = ReadJsonUtil.read(request);
        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());

        try {
            int addressId = Integer.parseInt(String.valueOf(data.get("addressId")));
            Address addressDefault = (Address) session.getAttribute("addressDefault");

            // Kiểm tra có phải mặc định không
            if (addressDefault != null && addressDefault.getId() == addressId) {
                out.print("{ \"status\": false, \"message\": \"Không thể xóa địa chỉ mặc định!\" }");
                return;
            }

            AddressService addressService = new AddressService();
            if (addressService.deleteAddress(addressId)) {
                out.print("{ \"status\": true }");
            } else {
                out.print("{ \"status\": false, \"message\": \"Dữ liệu chưa được xóa!\" }");
            }
        } catch (NumberFormatException e) {
            out.print("{ \"status\": false, \"message\": \"Lỗi dữ liệu đầu vào!\" }");
        } finally {
            out.flush();
            out.close();
        }
    }
}
