package vn.edu.hcmuaf.fit.Web_ban_hang.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;
import vn.edu.hcmuaf.fit.Web_ban_hang.session.Cart;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartController", urlPatterns = { "/cart", "/api/cart" })
public class CartController extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // CHECK LOGIN
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action != null) {
            handleFormRequest(request, response);
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.refreshStock();
        } else {
            request.setAttribute("isCartEmpty", true);
            request.setAttribute("message", "Giỏ hàng của bạn đang trống.");
        }
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reqPath = request.getServletPath();

        // CHECK LOGIN
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            if (reqPath.equals("/api/cart")) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                Map<String, Object> result = new HashMap<>();
                result.put("status", false);
                result.put("message", "Bạn cần đăng nhập để thực hiện chức năng này!");
                result.put("redirect", "login.jsp");
                out.print(gson.toJson(result));
                out.flush();
            } else {
                response.sendRedirect("login.jsp");
            }
            return;
        }

        // Xử lý JSON
        if (reqPath.equals("/api/cart")) {
            handleApiRequest(request, response);
        } else {
            // Xử lý form submit (nếu có)
            handleFormRequest(request, response);
        }
    }

    private void handleApiRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String jsonData = ReadJsonUtil.read(request);
            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

            // Mặc định là action add nếu không có action
            String action = "add";
            if (jsonObject.has("action")) {
                action = jsonObject.get("action").getAsString();
            }

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }

            Map<String, Object> result = new HashMap<>();

            if ("add".equals(action) || !jsonObject.has("action")) { // Default add implied by cart.js body
                int productId = jsonObject.get("productId").getAsInt();
                int quantity = jsonObject.has("quantity") ? jsonObject.get("quantity").getAsInt() : 1;

                Product product = productService.getById(productId);
                if (product != null) {
                    boolean success = cart.add(product, quantity);
                    if (success) {
                        result.put("status", true);
                        result.put("message", "Đã thêm vào giỏ hàng!");
                    } else {
                        result.put("status", false);
                        result.put("message", "Sản phẩm đã hết hàng hoặc không đủ số lượng!");
                    }
                } else {
                    result.put("status", false);
                    result.put("message", "Sản phẩm không tồn tại!");
                }
            } else if ("update".equals(action)) {
                int productId = jsonObject.get("id").getAsInt();
                int quantity = jsonObject.get("quantity").getAsInt();
                boolean success = cart.update(productId, quantity);
                if (success) {
                    result.put("status", true);
                    result.put("message", "Cập nhật thành công!");
                } else {
                    result.put("status", false);
                    result.put("message", "Không đủ hàng trong kho!");
                }
            } else if ("remove".equals(action)) {
                int productId = jsonObject.get("id").getAsInt();
                cart.remove(productId);
                result.put("status", true);
                result.put("message", "Đã xóa sản phẩm!");
            }

            result.put("cartSize", cart.getTotalQuantityAll());
            result.put("total", cart.getTotalWithDiscount());

            out.print(gson.toJson(result));

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("message", "Lỗi xử lý: " + e.getMessage());
            out.print(gson.toJson(error));
        }
        out.flush();
    }

    private void handleFormRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if ("remove".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                HttpSession session = request.getSession();
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null)
                    cart.remove(id);
            } catch (Exception ignored) {
            }
        } else if ("update".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                int qty = Integer.parseInt(request.getParameter("quantity"));
                HttpSession session = request.getSession();
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null)
                    cart.update(id, qty);
            } catch (Exception ignored) {
            }
        }
        response.sendRedirect("cart");
    }
}
