package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.inventory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;

import java.io.IOException;

import java.util.List;
import java.util.Map;

@WebServlet(name = "InventoryTransactions", urlPatterns = "/adminTransactions")
public class InventoryTransactions extends HttpServlet {
//    private AdminDao adminDao = new AdminDao();
    private final InventoryDao inventoryDao = new InventoryDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy danh sách lịch sử giao dịch từ AdminDao
//        List<Map<String, Object>> transactionHistory = adminDao.getTransactionHistory();
        List<Map<String, Object>> transactionHistory = inventoryDao.getTransactionHistory();

        // Log để kiểm tra kết quả trả về từ phương thức getTransactionHistory
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Map<String, Object> row : transactionHistory) {
                System.out.println("🧾 Log row: " + row);
            }
            // In toàn bộ danh sách giao dịch
        }

        // Đưa dữ liệu vào request để truyền tới JSP
        request.setAttribute("transactionHistory", transactionHistory);

        // Log trước khi chuyển tiếp dữ liệu đến JSP
        System.out.println("Forwarding to JSP with transaction data...");


        request.getRequestDispatcher("ad-transactions.jsp").forward(request, response);
    }
}
