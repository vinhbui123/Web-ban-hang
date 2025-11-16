package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.inventory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CategoryService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/adminInventory")
public class ImportAndExportProduct extends HttpServlet {
    private InventoryDao inventoryDao;

    @Override
    public void init() {
        inventoryDao = new InventoryDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");

        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            String type = req.getParameter("type");

            boolean success = false;
            if ("import".equalsIgnoreCase(type)) {
                success = inventoryDao.importProduct(productId, quantity, user.getId());
            } else if ("export".equalsIgnoreCase(type)) {
                success = inventoryDao.exportProduct(productId, quantity, user.getId(), type);
            }

            req.getSession().setAttribute("message", success ? " Thao tác thành công!" : " Thao tác thất bại!");
            req.getSession().setAttribute("messageType", success ? "success" : "error");

        } catch (NumberFormatException e) {
            req.getSession().setAttribute("message", " Dữ liệu không hợp lệ.");
            req.getSession().setAttribute("messageType", "error");
        }

        // Redirect để tránh lỗi F5 và dùng PRG
        resp.sendRedirect(req.getContextPath() + "/adminInventory");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String categoryIdParam = req.getParameter("category");
            List<Product> products;

            if (categoryIdParam != null) {
                int categoryId = Integer.parseInt(categoryIdParam);
                products = new ProductService().getByCategory(categoryId);
            } else {
                products = new ProductService().getAll();
            }

            List<Category> categories = new CategoryService().getAll();
            req.setAttribute("products", products);
            req.setAttribute("category", categories);

            // Truyền lại flash message nếu có
            Object msg = req.getSession().getAttribute("message");
            Object type = req.getSession().getAttribute("messageType");

            if (msg != null) {
                req.setAttribute("message", msg);
                req.setAttribute("messageType", type);
                req.getSession().removeAttribute("message");
                req.getSession().removeAttribute("messageType");
            }

            req.getRequestDispatcher("ad-inventory.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500, "Lỗi khi xử lý GET");
        }
    }
}
