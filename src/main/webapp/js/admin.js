window.contextPath = "${pageContext.request.contextPath}";

function openModal(type, id = null) {
    if (type === 'add') {
        const modal = document.getElementById('addProductModal');
        if (modal) {
            modal.style.display = 'block';
        } else {
            console.error("Không tìm thấy modal có id 'addProductModal'");
        }
    } else if (type === 'edit') {
        const modal = document.getElementById('editProductModal');
        if (modal) {
            modal.style.display = 'block';
            console.log("Đang mở modal sửa với id sản phẩm:", id);

            fetch(`${contextPath}/getProduct?id=${id}`)
                .then(res => res.json())
                .then(data => {
                    document.getElementById('editProductId').value = id;
                    document.getElementById('editProductName').value = data.name || '';
                    document.getElementById('editPrice').value = data.price || '';
                    document.getElementById('editCategory').value = String(data.catalog_id) || '';
                    document.getElementById('editDescription').value = data.description || '';
                    document.getElementById('editOldImage').value = data.img || '';

                    // Hiển thị ảnh hiện tại nếu có
                    const imgPreview = document.getElementById('editPreviewImage');
                    if (data.img) {
                        imgPreview.src = `${contextPath}/${data.img}`;
                    } else {
                        imgPreview.src = "";
                    }

                    // Reset tất cả checkbox chất liệu trước
                    const checkboxes = document.querySelectorAll('#editMaterialGroup input[type="checkbox"]');
                    checkboxes.forEach(cb => cb.checked = false);

                    // Tích lại các chất liệu nếu có
                    if (Array.isArray(data.materials)) {
                        data.materials.forEach(m => {
                            const checkbox = document.querySelector(`#editMaterialGroup input[value="${m.id}"]`);
                            if (checkbox) checkbox.checked = true;
                        });
                    }

                    // Đặt lại action nếu cần (nếu bạn dùng submit truyền thống)
                    document.getElementById('editProductForm').action = `${contextPath}/adminEdit`;
                })
                .catch(error => {
                    console.error("Lỗi khi tải sản phẩm:", error);
                    closeModal('editProductModal');
                });
        } else {
            console.error("Không tìm thấy modal có id 'editProductModal'");
        }
    }
}


function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}


document.addEventListener("DOMContentLoaded", function () {
    let currentUrl = window.location.href; // Lấy URL hiện tại

    // Bỏ class "active" khỏi tất cả các link
    document.querySelectorAll(".category-list a").forEach(link => {
        link.classList.remove("active");

        // Nếu href của link khớp với URL hiện tại, thêm class "active"
        if (link.href === currentUrl) {
            link.classList.add("active");
        }
    });
});


// Hàm mở và đóng modal quản lý đơn hàng
function openOrderModal() {
    document.getElementById("orderModal").style.display = "block";  // Mở modal đơn hàng
}

function closeOrderModal() {
    document.getElementById("orderModal").style.display = "none";  // Đóng modal đơn hàng
}

// Hàm mở và đóng modal quản lý khách hàng
function openCustomerModal() {
    document.getElementById("customerModal").style.display = "block";  // Mở modal khách hàng
}

function closeCustomerModal() {
    document.getElementById("customerModal").style.display = "none";  // Đóng modal khách hàng
}

function openImportModal(productId, productName) {
    document.getElementById('importProductId').value = productId;
    document.getElementById('importProductName').value = productName;
    document.getElementById('importModal').style.display = "block";
}

function closeImportModal() {
    document.getElementById('importModal').style.display = "none";
}
function openExportModal(productId, productName) {
    document.getElementById("exportProductId").value = productId;
    document.getElementById("exportProductName").value = productName;
    document.getElementById("exportModal").style.display = "block";
}

function closeExportModal() {
    document.getElementById("exportModal").style.display = "none";
}




