package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import vn.edu.hcmuaf.fit.Web_ban_hang.utils.SettingDAO;

public class Role {
    private int id;

    public Role(int id) {
        this.id = id;
    }

    public Role() {
    }

    public boolean isAdmin() {
        return this.id == 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRole(){
        return SettingDAO.roleId(this.id);
    }
}
