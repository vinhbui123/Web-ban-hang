<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/css/address-form.css">
<script src="${pageContext.request.contextPath}/js/address-form.js"></script>

<!-- Nút để mở popup -->
<button onclick="openAddressPopup()">Cập nhật địa chỉ</button>

<!-- Popup (ẩn khi chưa mở) -->
<div id="addressModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeAddressPopup()">&times;</span>

        <!-- ========== DANH SÁCH ĐỊA CHỈ ========== -->
        <div id="addressListView">
            <h3>Danh sách địa chỉ</h3>
            <div id="addressList" class="address-list">
                <c:forEach var="address" items="${addressList}">
                    <div class="address-card" data-address-id="${address.id}">
                        <p><strong>${address.fullName}</strong> - ${address.phone}</p>
                        <p>${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}</p>
                        <p>
                            Loại: ${address.addressType}
                            <c:if test="${address.isDefault}">
                                <span class="default-badge">Mặc định</span>
                            </c:if>
                        </p>

                        <div class="address-actions">
                            <label>
                                <input type="checkbox" name="defaultAddress"
                                       <c:if test="${address.isDefault}">checked</c:if>
                                       onchange="setDefaultAddress(${address.id})">
                                Đặt làm mặc định
                            </label>

                            <button type="button" class="edit-btn"
                                    data-id="${address.id}"
                                    data-fullname="${address.fullName}"
                                    data-phone="${address.phone}"
                                    data-province="${address.province}"
                                    data-district="${address.district}"
                                    data-ward="${address.ward}"
                                    data-detail="${address.addressDetail}"
                                    data-type="${address.addressType}"
                                    data-default="${address.isDefault ? 'true' : 'false'}"
                                    onclick="handleEditButton(this)">
                                Chỉnh sửa
                            </button>

                            <button type="button" class="delete-btn"
                                    onclick="deleteAddress(${address.id}, ${address.isDefault ? 'true' : 'false'});">
                                Xóa
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <button onclick="clearAddressForm(); showAddressFormOnly();" class="add-address-btn">+ Thêm địa chỉ mới
            </button>
        </div>

        <!-- ========== FORM CẬP NHẬT/THÊM ĐỊA CHỈ ========== -->
        <div id="addressFormView" style="display: none;">
            <input type="hidden" name="id" id="addressId"/>
            <h3>Địa chỉ của tôi</h3>
            <div class="form-row">
                <input type="text" name="fullName" id="fullName" placeholder="Họ và tên" required/>
                <input type="tel" name="phone" id="phone" placeholder="Số điện thoại" required/>
            </div>

            <div class="form-row">
                <input list="provinceList" id="province" placeholder="Tỉnh/Thành phố" required autocomplete="off"/>
                <datalist id="provinceList"></datalist>
            </div>
            <div class="form-row">
                <input list="districtList" id="district" placeholder="Quận/Huyện" required autocomplete="off"/>
                <datalist id="districtList"></datalist>
            </div>
            <div class="form-row">
                <input list="wardList" id="ward" placeholder="Phường/Xã" required autocomplete="off"/>
                <datalist id="wardList"></datalist>
            </div>

            <textarea name="addressDetail" id="addressDetail" class="address-detail-fixed" placeholder="Địa chỉ cụ thể"
                      required></textarea>

            <label style="padding-top: 5px;">Loại địa chỉ:</label>
            <div class="address-type">
                <label><input type="radio" name="addressType" value="Nhà riêng"/> Nhà Riêng</label>
                <label><input type="radio" name="addressType" value="Văn phòng"/> Văn Phòng</label>
            </div>

            <label>
                <input type="checkbox" name="isDefault" id="isDefault" value="true" checked/> Đặt làm mặc định
            </label>

            <div class="actions">
                <button type="button" class="btn-cancel" onclick="backToAddressList()">Trở lại</button>
                <button type="button" class="btn-submit" onclick="submitForm()">Lưu</button>
            </div>
        </div>
    </div>
</div>