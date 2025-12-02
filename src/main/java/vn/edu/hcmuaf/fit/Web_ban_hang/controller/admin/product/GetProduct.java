package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;
import java.io.IOException;
import com.google.gson.Gson;

@WebServlet(name = "GetProduct", value = "/getProduct")
public class GetProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ.");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            ProductService productService = new ProductService();
            Product product = productService.getById(productId); // cần đảm bảo có setMaterials()

            if (product != null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                String json = new Gson().toJson(product);
                resp.getWriter().write(json);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm.");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
        }
    }
}
