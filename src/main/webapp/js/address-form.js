function showPopupError(inputElement, message) {
    const popup = document.createElement('div');
    popup.className = 'input-popup-error';
    popup.textContent = message;

    document.body.appendChild(popup);

    const rect = inputElement.getBoundingClientRect();
    popup.style.top = `${rect.top + window.scrollY - 35}px`;
    popup.style.left = `${rect.left + window.scrollX}px`;

    setTimeout(() => {
        popup.remove();
    }, 1000); // Tự ẩn sau 1 giây
}

document.addEventListener("DOMContentLoaded", () => {
    const provinceInput = document.getElementById('province');
    const districtInput = document.getElementById('district');
    const wardInput = document.getElementById('ward');

    const provinceListData = document.getElementById('provinceList');

    window.openAddressPopup = async function () {
        document.getElementById("addressModal").style.display = "block";

        const listView = document.getElementById('addressListView');
        const formView = document.getElementById('addressFormView');

        listView.style.display = 'block';
        formView.style.display = 'none';

        await reloadAddressList();

        fetchProvinces();
    };

    window.closeAddressPopup = function () {
        document.getElementById("addressModal").style.display = "none";
    };

    window.submitForm = function () {
        let isValid = true;

        // Lấy các trường dữ liệu
        const fullNameInput = document.querySelector('input[name="fullName"]');
        const phoneInput = document.querySelector('input[name="phone"]');
        const provinceInput = document.getElementById('province');
        const districtInput = document.getElementById('district');
        const wardInput = document.getElementById('ward');
        const addressDetailInput = document.querySelector('textarea[name="addressDetail"]');
        const addressTypeInput = document.querySelector('input[name="addressType"]:checked');

        // Kiểm tra từng trường
        if (!fullNameInput.value.trim()) {
            showPopupError(fullNameInput, "Vui lòng nhập họ và tên");
            isValid = false;
        }
        if (!phoneInput.value.trim()) {
            showPopupError(phoneInput, "Vui lòng nhập số điện thoại");
            isValid = false;
        }
        if (!provinceInput.value.trim()) {
            showPopupError(provinceInput, "Vui lòng chọn tỉnh/thành phố");
            isValid = false;
        }
        if (!districtInput.value.trim()) {
            showPopupError(districtInput, "Vui lòng chọn quận/huyện");
            isValid = false;
        }
        if (!wardInput.value.trim()) {
            showPopupError(wardInput, "Vui lòng chọn phường/xã");
            isValid = false;
        }
        if (!addressDetailInput.value.trim()) {
            showPopupError(addressDetailInput, "Vui lòng nhập địa chỉ cụ thể");
            isValid = false;
        }
        if (!addressTypeInput) {
            // Lấy radio group để báo lỗi
            const addressTypeRadios = document.querySelectorAll('input[name="addressType"]');
            if (addressTypeRadios.length > 0) {
                showPopupError(addressTypeRadios[0], "Vui lòng chọn loại địa chỉ");
            }
            isValid = false;
        }

        if (!isValid) return; // Nếu có lỗi thì không gửi form

        // Nếu hợp lệ thì tiếp tục gửi dữ liệu như cũ
        const rawAddressType = addressTypeInput?.value || '';
        let addressTypeCode = '';
        if (rawAddressType === 'Nhà riêng') {
            addressTypeCode = 'HOME';
        } else if (rawAddressType === 'Văn phòng') {
            addressTypeCode = 'OFFICE';
        }
        const data = {
            id: parseInt(document.querySelector('input[name="id"]').value) || null,
            fullName: document.querySelector('input[name="fullName"]').value,
            phone: document.querySelector('input[name="phone"]').value,
            province: document.getElementById('province').value,
            district: document.getElementById('district').value,
            ward: document.getElementById('ward').value,
            addressDetail: document.querySelector('textarea[name="addressDetail"]').value,
            addressType: addressTypeCode,
            isDefault: document.querySelector('input[name="isDefault"]').checked
        };

        fetch(`${contextPath}/address-form`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(res => res.json())
            .then(response => {
                if (response.status) {
                    // Cập nhật lại phần address-details nếu có addressDefault mới
                    if (response.addressDefault) {
                        updateAddressDetails(response.addressDefault);
                        if (!document.getElementById('shipping-methods')) {
                            const shippingInfoContainer = document.querySelector('.shipping-info');
                            if (shippingInfoContainer) {
                                const div = document.createElement('div');
                                div.id = 'shipping-methods';
                                shippingInfoContainer.appendChild(div);
                            }
                        }
                        const redNotice = document.querySelector('.shipping-info div[style*="red"]');
                        if (redNotice) redNotice.remove();
                        loadShippingMethods();
                    }
                    alert("Lưu địa chỉ thành công!");
                    closeAddressPopup();
                } else {
                    alert(response.message || "Có lỗi xảy ra khi lưu địa chỉ.");
                }
            })
            .catch(err => {
                console.error("Error:", err);
                alert("Có lỗi xảy ra khi lưu địa chỉ.");
            });
    };

    provinceInput.addEventListener('blur', () => {
        districtInput.value = "";
        wardInput.value = "";
        document.getElementById('districtList').innerHTML = "";
        document.getElementById('wardList').innerHTML = "";
        if (isProvinceValid()) {
            fetchDistricts();
        }

    });

    districtInput.addEventListener('blur', () => {
        wardInput.value = "";
        document.getElementById('wardList').innerHTML = "";
        if (isDistrictValid()) {
            fetchWards();
        }

    });

    wardInput.addEventListener('blur', () => {
        isWardValid();
    });

    async function fetchProvinces() {
        try {
            const response = await fetch(`${contextPath}/provinces`);
            const data = await response.json();
            if (data.code === 200 && data.data) {
                provinceListData.innerHTML = "";
                data.data.forEach(province => {
                    const option = document.createElement('option');
                    option.value = province.name;
                    option.dataset.provinceId = province.id;
                    provinceListData.appendChild(option);
                });
            } else {
                console.error('Lỗi khi lấy tỉnh:', data.message);
            }
        } catch (error) {
            console.error('Lỗi kết nối tới servlet /provinces:', error);
        }
    }
});

async function fetchDistricts() {
    const provinceInput = document.getElementById('province');
    const provinceListData = document.getElementById('provinceList');
    const districtListData = document.getElementById('districtList');

    const provinceValue = provinceInput.value.trim();
    const matchedOption = Array.from(provinceListData.options).find(option => option.value === provinceValue);

    if (!matchedOption) {
        showPopupError(provinceInput, "Tỉnh/Thành không hợp lệ");
        return;
    }

    const provinceId = matchedOption.dataset.provinceId;

    try {
        const response = await fetch(`${contextPath}/districts?province_id=${provinceId}`);
        const data = await response.json();
        if (data.code === 200 && data.data) {
            districtListData.innerHTML = "";
            data.data.forEach(district => {
                const option = document.createElement('option');
                option.value = district.name;
                option.dataset.districtId = district.id;
                districtListData.appendChild(option);
            });
        } else {
            console.error('Lỗi khi lấy quận/huyện:', data.message);
        }
    } catch (error) {
        console.error('Lỗi kết nối tới servlet /districts:', error);
    }
}

async function fetchWards() {
    const districtInput = document.getElementById('district');
    const districtListData = document.getElementById('districtList');
    const wardListData = document.getElementById('wardList');

    const districtValue = districtInput.value.trim();
    const matchedOption = Array.from(districtListData.options).find(option => option.value === districtValue);

    if (!matchedOption) {
        showPopupError(districtInput, "Quận/huyện không hợp lệ");
        return;
    }

    const districtId = matchedOption.dataset.districtId;

    try {
        const response = await fetch(`${contextPath}/wards?district_id=${districtId}`);
        const data = await response.json();

        if (data.code === 200 && data.data) {
            wardListData.innerHTML = "";
            data.data.forEach(ward => {
                const option = document.createElement('option');
                option.value = ward.name;
                option.dataset.wardId = ward.id;
                wardListData.appendChild(option);
            });
        } else {
            console.error('Lỗi khi lấy phường/xã:', data.message);
        }
    } catch (error) {
        console.error('Lỗi kết nối tới servlet /wards:', error);
    }
}

function isProvinceValid() {
    const provinceInput = document.getElementById('province');
    const provinceListData = document.getElementById('provinceList');
    const provinceValue = provinceInput.value.trim();

    const options = Array.from(provinceListData.options);
    const matchedOption = options.find(option => option.value === provinceValue);
    if (!matchedOption) {
        showPopupError(provinceInput, "Tỉnh/Thành không hợp lệ");
        provinceInput.classList.add('input-error');
        provinceInput.focus();
        return false;
    }
    provinceInput.classList.remove('input-error');
    return true;
}

function isDistrictValid() {
    const districtInput = document.getElementById('district');
    const districtListData = document.getElementById('districtList');
    const districtValue = districtInput.value.trim();

    const options = Array.from(districtListData.options);
    const matchedOption = options.find(option => option.value === districtValue);
    if (!matchedOption) {
        showPopupError(districtInput, "Quận/huyện không hợp lệ");
        districtInput.classList.add('input-error');
        districtInput.focus();
        return false;
    }
    districtInput.classList.remove('input-error');
    return true;
}

function isWardValid() {
    const wardInput = document.getElementById('ward');
    const wardListData = document.getElementById('wardList');
    const wardValue = wardInput.value.trim();

    const options = Array.from(wardListData.options);
    const matchedOption = options.find(option => option.value === wardValue);
    if (!matchedOption) {
        showPopupError(wardInput, "Phường/xã không hợp lệ");
        wardInput.classList.add('input-error');
        wardInput.focus();
        return false;
    }
    wardInput.classList.remove('input-error');
    return true;
}

// Dùng để đổ dữ liệu từ danh sách vào form
function editAddress(data) {
    clearAddressForm(); // Xoá trước để tránh giữ giá trị cũ

    document.getElementById("addressId").value = data.id || "";
    document.getElementById("fullName").value = data.fullName || "";
    document.getElementById("phone").value = data.phone || "";
    document.getElementById("province").value = data.province || "";
    document.getElementById("district").value = data.district || "";
    document.getElementById("ward").value = data.ward || "";

    document.getElementById("addressDetail").value = data.addressDetail || "";

    document.querySelectorAll('input[name="addressType"]').forEach(radio => {
        radio.checked = radio.value === data.addressType;
    });

    document.getElementById("isDefault").checked = data.isDefault === true;
}

// Dùng để clear form khi "Thêm mới"
function clearAddressForm() {
    document.getElementById("addressId").value = "";
    document.getElementById("fullName").value = "";
    document.getElementById("phone").value = "";
    document.getElementById("province").value = "";
    document.getElementById("district").value = "";
    document.getElementById("ward").value = "";
    document.getElementById("addressDetail").value = "";
    document.querySelectorAll('input[name="addressType"]').forEach(r => r.checked = false);
    document.getElementById("isDefault").checked = false;
    document.getElementById("districtList").innerHTML = "";
    document.getElementById("wardList").innerHTML = "";
}

async function reloadAddressList() {
    try {
        const res = await fetch(`${contextPath}/get-address-list`);
        const data = await res.json();

        if (!data.status) {
            throw new Error(data.message || "Không thể lấy danh sách địa chỉ");
        }

        const addressListContainer = document.querySelector('.address-list');
        addressListContainer.innerHTML = ''; // Xóa danh sách cũ

        data.addressList.forEach(address => {
            const addressCard = createAddressCard(address);
            addressListContainer.appendChild(addressCard);
        });
    } catch (error) {
        console.error('Lỗi khi load danh sách địa chỉ:', error);
        throw error; // để `await reloadAddressList()` ở nơi gọi có thể catch
    }
}

function createAddressCard(address) {
    const div = document.createElement('div');
    div.className = 'address-card';
    div.setAttribute('data-address-id', address.id);

    div.innerHTML = `
            <p><span>${address.fullName}</span> - ${address.phone}</p>
            <p>${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}</p>
            <p>
                Loại: ${address.addressType}
                ${address.isDefault ? '<span class="default-badge">Mặc định</span>' : ''}
            </p>
    
            <div class="address-actions">
                <label>
                    <input type="checkbox" name="defaultAddress"
                           ${address.isDefault ? 'checked' : ''}
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
        `;

    return div;
}

function backToAddressList() {
    document.getElementById('addressFormView').style.display = 'none';
    document.getElementById('addressListView').style.display = 'block';
}

function deleteAddress(addressId, isDefault) {
    if (isDefault) {
        showPopupError(document.querySelector(`[data-address-id="${addressId}"]`), 'Không thể xóa địa chỉ mặc định.');
        return;
    }

    fetch(`${contextPath}/delete-address`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({addressId: addressId})
    })
        .then(response => response.json())
        .then(data => {
            if (data.status) {
                // Xóa địa chỉ khỏi giao diện
                const addressElement = document.querySelector(`[data-address-id="${addressId}"]`);
                if (addressElement) {
                    addressElement.remove();
                }
                // Nếu muốn đồng bộ lại danh sách, có thể reload hoặc cập nhật lại addressList ở đây
                // location.reload(); // hoặc gọi hàm load lại danh sách địa chỉ
            } else {
                alert(data.message || 'Có lỗi xảy ra khi xóa địa chỉ.');
            }
        })
        .catch(() => {
            alert('Có lỗi xảy ra khi xóa địa chỉ.');
        });
}

function setDefaultAddress(addressId) {
    fetch(`${contextPath}/default-address`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({addressId: addressId})
    })
        .then(res => res.json())
        .then(data => {
            console.log("📦 JSON response từ server:", data); // 👉 In toàn bộ JSON
            console.log("📮 addressDefault từ server:", data.addressDefault);
            if (data.status) {
                // Cập nhật lại phần address-details nếu có addressDefault mới

                if (data.addressDefault) {
                    updateAddressDetails(data.addressDefault);
                    loadShippingMethods();
                }
                // Cập nhật trạng thái mặc định trong danh sách địa chỉ
                const addressCards = document.querySelectorAll('.address-card');
                addressCards.forEach(card => {
                    const cardId = parseInt(card.getAttribute('data-address-id'));
                    const checkbox = card.querySelector('input[name="defaultAddress"]');
                    const defaultBadge = card.querySelector('.default-badge');

                    if (cardId === addressId) {
                        checkbox.checked = true;
                        if (!defaultBadge) {
                            const badge = document.createElement('span');
                            badge.className = 'default-badge';
                            badge.textContent = 'Mặc định';
                            card.querySelector('p:nth-child(3)').appendChild(badge);
                        }
                    } else {
                        checkbox.checked = false;
                        if (defaultBadge) {
                            defaultBadge.remove();
                        }
                    }
                });
                alert("Đặt địa chỉ mặc định thành công!");

            } else {
                alert(data.message || "Không thể đặt mặc định. Đã xảy ra lỗi.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Không thể đặt mặc định. Đã xảy ra lỗi.");
        });
}

function updateAddressDetails(address) {
    const addressDetails = document.querySelector('.address-details');
    if (!addressDetails) return;

    const html = `
        <strong>${address.fullName}, SĐT: ${address.phone}</strong><br>
        ${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}
    `;

    addressDetails.innerHTML = html;

    let addressInput = document.getElementById('address-data');
    if (!addressInput) {
        addressInput = document.createElement('input');
        addressInput.type = 'hidden';
        addressInput.id = 'address-data';
        addressDetails.appendChild(addressInput); // Gắn ngay sau địa chỉ
    }
    addressInput.value = JSON.stringify(address);
}

function showAddressFormOnly() {
    document.getElementById('addressListView').style.display = 'none';
    document.getElementById('addressFormView').style.display = 'block';

}

function handleEditButton(btn) {
    const data = {
        id: parseInt(btn.dataset.id),
        fullName: btn.dataset.fullname,
        phone: btn.dataset.phone,
        province: btn.dataset.province,
        district: btn.dataset.district,
        ward: btn.dataset.ward,
        addressDetail: btn.dataset.detail,
        addressType: btn.dataset.type,
        isDefault: btn.dataset.default === 'true'
    };

    editAddress(data);
}