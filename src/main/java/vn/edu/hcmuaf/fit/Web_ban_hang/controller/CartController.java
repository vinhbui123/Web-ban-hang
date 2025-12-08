package vn.edu.hcmuaf.fit.Web_ban_hang.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;
import vn.edu.hcmuaf.fit.Web_ban_hang.session.Cart;

import java.io.IOException;

@WebServlet(name = "CartController", value = "/cart")
public class CartController extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }

        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "update":
                updateCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            default:
                viewCart(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Hiển thị trang giỏ hàng
     */
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("isCartEmpty", true);
            request.setAttribute("message", "Giỏ hàng của bạn đang trống.");
        } else {
            // Cập nhật stock mới nhất
            cart.refreshStock();
        }

        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int quantity = 1;
            try {
                String qtyParam = request.getParameter("quantity");
                if (qtyParam != null && !qtyParam.isEmpty()) {
                    quantity = Integer.parseInt(qtyParam);
                }
            } catch (NumberFormatException ignored) {
            }

            Product product = productService.getById(id);
            if (product != null) {
                HttpSession session = request.getSession();
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }

                boolean success = cart.add(product, quantity);
                session.setAttribute("cart", cart);

                if (!success) {
                    // Không đủ hàng trong kho
                    response.sendRedirect("cart?error=outofstock");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Redirect về trang trước hoặc giỏ hàng
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("cart");
        }
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                boolean success = cart.update(id, quantity);
                session.setAttribute("cart", cart);

                if (!success) {
                    response.sendRedirect("cart?error=outofstock");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        response.sendRedirect("cart");
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                cart.remove(id);
                session.setAttribute("cart", cart);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        response.sendRedirect("cart");
    }
}
