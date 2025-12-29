package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressDao {

    public Address getAddressById(int id) {
        String sql = "SELECT * FROM user_addresses WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Address address = new Address();
                address.setId(rs.getInt("id"));
                address.setUserId(rs.getInt("user_id"));
                address.setFullName(rs.getString("full_name"));
                address.setPhone(rs.getString("phone"));
                address.setProvince(rs.getString("province"));
                address.setDistrict(rs.getString("district"));
                address.setWard(rs.getString("ward"));
                address.setAddressDetail(rs.getString("address_detail"));
                address.setAddressType(rs.getString("address_type"));
                address.setDefault(rs.getBoolean("is_default"));
                return address;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Gọi tất cả địa chỉ cụ thể của user dựa theo id user
    public List<Address> getAddressByIdUser(int userId) {
        List<Address> addressList = new ArrayList<>();
        String sql = "SELECT * FROM user_addresses WHERE user_id = ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Address address = new Address();
                address.setId(rs.getInt("id"));
                address.setUserId(rs.getInt("user_id"));
                address.setFullName(rs.getString("full_name"));
                address.setPhone(rs.getString("phone"));
                address.setProvince(rs.getString("province"));
                address.setDistrict(rs.getString("district"));
                address.setWard(rs.getString("ward"));
                address.setAddressDetail(rs.getString("address_detail"));
                address.setAddressType(rs.getString("address_type"));
                address.setDefault(rs.getBoolean("is_default"));
                addressList.add(address);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressList;
    }

    public boolean updateAddress(Address address) {
        String sql = "UPDATE user_addresses SET " +
                "full_name = ?, phone = ?, province = ?, district = ?, ward = ?, " +
                "address_detail = ?, address_type = ?, is_default = ? " +
                "WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getFullName());
            stmt.setString(2, address.getPhone());
            stmt.setString(3, address.getProvince());
            stmt.setString(4, address.getDistrict());
            stmt.setString(5, address.getWard());
            stmt.setString(6, address.getAddressDetail());
            stmt.setString(7, address.getAddressType());
            stmt.setBoolean(8, address.isDefault());
            // reset địa chỉ mặc định (đảm bảo lưu duy nhất 1 địa chỉ mặc định trong sơ đồ)
            if (address.isDefault()) {
                resetDefaultAddress(conn, address.getUserId());
            }
            stmt.setInt(9, address.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertAddressAndSetDefault(Address address) {
        String sql = "INSERT INTO user_addresses (user_id, full_name, phone, province, district, ward, address_detail, address_type, is_default)"
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, address.getUserId());
            stmt.setString(2, address.getFullName());
            stmt.setString(3, address.getPhone());
            stmt.setString(4, address.getProvince());
            stmt.setString(5, address.getDistrict());
            stmt.setString(6, address.getWard());
            stmt.setString(7, address.getAddressDetail());
            stmt.setString(8, address.getAddressType());
            stmt.setBoolean(9, address.isDefault());
            // reset địa chỉ mặc định (đảm bảo lưu duy nhất 1 địa chỉ mặc định trong sơ đồ)
            if (address.isDefault()) {
                resetDefaultAddress(conn, address.getUserId());
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean resetDefaultAddress(Connection conn, int userId) throws SQLException {
        String sql = "UPDATE user_addresses SET is_default = 0 WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteAddress(int addressId) {
        String sql = "DELETE FROM user_addresses WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, addressId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean setDefault(Address address) {
        String setSql = "UPDATE user_addresses SET is_default = 1 WHERE id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Reset tất cả địa chỉ của user này về không mặc định
            resetDefaultAddress(conn, address.getUserId());

            // Cập nhật địa chỉ được chọn thành mặc định
            try (PreparedStatement setStmt = conn.prepareStatement(setSql)) {
                setStmt.setInt(1, address.getId());
                int affected = setStmt.executeUpdate();

                conn.commit(); // Xác nhận transaction nếu thành công
                return affected > 0;
            } catch (SQLException e) {
                conn.rollback(); // Nếu có lỗi thì rollback
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Address getAddressDefault(int userId) {
        String sql = "SELECT * FROM user_addresses WHERE user_id = ? AND is_default = 1";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Address address = new Address();
                address.setId(rs.getInt("id"));
                address.setUserId(rs.getInt("user_id"));
                address.setFullName(rs.getString("full_name"));
                address.setPhone(rs.getString("phone"));
                address.setProvince(rs.getString("province"));
                address.setDistrict(rs.getString("district"));
                address.setWard(rs.getString("ward"));
                address.setAddressDetail(rs.getString("address_detail"));
                address.setAddressType(rs.getString("address_type"));
                address.setDefault(rs.getBoolean("is_default"));
                return address;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}