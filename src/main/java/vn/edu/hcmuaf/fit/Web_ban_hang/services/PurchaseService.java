package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.PurchaseDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.OrderDTO;

import java.util.List;

public class PurchaseService {

    PurchaseDao purchaseDao = new PurchaseDao();


    public List<OrderDTO> getAllPurchaseByUserID(int userID) {
        return purchaseDao.getAllPurchaseByUser(userID);
    }

}
