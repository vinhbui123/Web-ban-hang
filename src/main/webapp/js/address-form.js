document.addEventListener("DOMContentLoaded", () => {
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
        // Simple validation - just check if fields are filled
        const fullNameInput = document.querySelector('input[name="fullName"]');
        const phoneInput = document.querySelector('input[name="phone"]');
        const provinceInput = document.getElementById('province');
        const districtInput = document.getElementById('district');
        const wardInput = document.getElementById('ward');
        const addressDetailInput = document.querySelector('textarea[name="addressDetail"]');
        const addressTypeInput = document.querySelector('input[name="addressType"]:checked');

        // Simple required field validation
        if (!fullNameInput.value.trim()) {
            alert("Vui lòng nhập họ và tên");
            fullNameInput.focus();
            return;
        }
        if (!phoneInput.value.trim()) {
            alert("Vui lòng nhập số điện thoại");
            phoneInput.focus();
            return;
        }
        if (!provinceInput.value.trim()) {
            alert("Vui lòng chọn tỉnh/thành phố");
            provinceInput.focus();
            return;
        }
        if (!districtInput.value.trim()) {
            alert("Vui lòng chọn quận/huyện");
            districtInput.focus();
            return;
        }
        if (!wardInput.value.trim()) {
            alert("Vui lòng chọn phường/xã");
            wardInput.focus();
            return;
        }
        if (!addressDetailInput.value.trim()) {
            alert("Vui lòng nhập địa chỉ cụ thể");
            addressDetailInput.focus();
            return;
        }
        if (!addressTypeInput) {
            alert("Vui lòng chọn loại địa chỉ");
            return;
        }

        // Prepare data
        const rawAddressType = addressTypeInput?.value || '';
        let addressTypeCode = '';
        if (rawAddressType === 'Nhà riêng') {
            addressTypeCode = 'HOME';
        } else if (rawAddressType === 'Văn phòng') {
            addressTypeCode = 'OFFICE';
        }
        const data = {
            id: parseInt(document.querySelector('input[name="id"]').value) || null,
            fullName: fullNameInput.value,
            phone: phoneInput.value,
            province: provinceInput.value,
            district: districtInput.value,
            ward: wardInput.value,
            addressDetail: addressDetailInput.value,
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

});

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
        alert('Không thể xóa địa chỉ mặc định.');
        return;
    }

    fetch(`${contextPath}/delete-address`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ addressId: addressId })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status) {
                // Xóa địa chỉ khỏi giao diện
                const addressElement = document.querySelector(`[data-address-id="${addressId}"]`);
                if (addressElement) {
                    addressElement.remove();
                }
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
        body: JSON.stringify({ addressId: addressId })
    })
        .then(res => res.json())
        .then(data => {
            console.log("📦 JSON response từ server:", data);
            console.log("📮 addressDefault từ server:", data.addressDefault);
            if (data.status) {
                // Cập nhật lại phần address-details nếu có addressDefault mới
                if (data.addressDefault) {
                    updateAddressDetails(data.addressDefault);
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

    // Xóa thông báo lỗi màu đỏ "Vui lòng cập nhật địa chỉ đơn hàng"
    const shippingInfo = document.querySelector('.shipping-info');
    if (shippingInfo) {
        const redNotice = shippingInfo.querySelector('div[style*="red"]');
        if (redNotice) {
            redNotice.remove();
        }

        // Thêm shipping-methods div nếu chưa có
        let shippingMethodsDiv = document.getElementById('shipping-methods');
        if (!shippingMethodsDiv) {
            shippingMethodsDiv = document.createElement('div');
            shippingMethodsDiv.id = 'shipping-methods';
            shippingMethodsDiv.innerHTML = '<div class="loading"></div>';
            shippingInfo.appendChild(shippingMethodsDiv);
        }
    }
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
    showAddressFormOnly();
}
