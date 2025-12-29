package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CategoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;

import java.util.List;

public class CategoryService {

    static CategoryDao categoryDao = new CategoryDao();

    public List<Category> getAll() {
        return categoryDao.getAll();
    }
    public String getCategoryNameById(int categoryId) {
        Category category = categoryDao.getById(categoryId);
        return (category != null) ? category.getName() : "Không xác định";
    }
}
