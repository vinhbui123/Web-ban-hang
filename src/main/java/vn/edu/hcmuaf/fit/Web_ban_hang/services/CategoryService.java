package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CategoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;

import java.util.List;

public class CategoryService {

    static CategoryDao categoryDao = new CategoryDao();

    public List<Category> getAll() {
        List<Category> category = categoryDao.getAll();
        System.out.println("CategoryService.getAll() retrieved " + category.size() + " category.");
        return category;
    }
    public Category getById(int id) {
        return categoryDao.getById(id);
    }
    public String getCategoryNameById(int categoryId) {
        Category category = categoryDao.getById(categoryId);
        return (category != null) ? category.getName() : "Không xác định";
    }
}
