package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AddressDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;

import java.util.List;

public class AddressService {
    AddressDao addressDao = new AddressDao();

    // Thêm địa chỉ giao hàng vào db
    public boolean insertAddressAndSetDefault(Address address) {return addressDao.insertAddressAndSetDefault(address);}

    // Lấy danh sách địa chỉ đơn hàng theo id
    public List<Address> getAddressByIdUser(int userId) {
        return addressDao.getAddressByIdUser(userId);
    }

    // Cập nhật địa chỉ giao hàng của user
    public boolean updateAddress(Address address) {
        return addressDao.updateAddress(address);
    }

    public boolean deleteAddress(int idAddress) {
        return addressDao.deleteAddress(idAddress);
    }

    public boolean setDefault(Address address) {
        return addressDao.setDefault(address);
    }

    public Address getAddressDefault(int userId) {
        return addressDao.getAddressDefault(userId);
    }

    public Address getAddressById(int id) {
        return addressDao.getAddressById(id);
    }
}
