package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.material;

import jakarta.servlet.http.HttpServlet;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.MaterialDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MaterialProduct", urlPatterns = "/adminMaterials")
public class MaterialProduct extends HttpServlet {
    private final MaterialDao materialDao = new MaterialDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Material> materials = materialDao.getAll();
        req.setAttribute("materials", materials);
        req.getRequestDispatcher("/ad-material.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String name = req.getParameter("name");
        int id = req.getParameter("id") != null ? Integer.parseInt(req.getParameter("id")) : -1;

        boolean success = false;
        if ("add".equals(action)) {
            success = materialDao.addMaterial(name);
        } else if ("update".equals(action)) {
            success = materialDao.updateMaterial(id, name);
        } else if ("delete".equals(action)) {
            success = materialDao.deleteMaterial(id);
        }

        // redirect để load lại danh sách
        resp.sendRedirect(req.getContextPath() + "/adminMaterials?success=" + success);
    }
}
