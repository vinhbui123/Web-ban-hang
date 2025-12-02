// Hàm chọn tất cả các checkbox
function selectAll(source) {
    // Lấy tất cả các checkbox trong giỏ hàng
    const checkboxes = document.querySelectorAll('.product-checkbox');
    // Nếu checkbox "Chọn tất cả" được chọn, thì tích tất cả các checkbox
    checkboxes.forEach(checkbox => {
        checkbox.checked = source.checked;
        updateProductSelection(checkbox.closest('.cart-item').dataset.productId, source.checked);
    });

    // Cập nhật tổng tiền và số lượng
    calculateTotal();
}

// Hàm cập nhật trạng thái chọn của sản phẩm
function updateProductSelection(productId, selected) {
    fetch(`${contextPath}/update-cart-selection`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({productId: productId, selected: selected})
    })
        .then(response => response.json())
        .then(data => {
            if (!data.status) {
                console.error('Lỗi khi cập nhật trạng thái chọn sản phẩm');
            }
        })
        .catch(error => console.error('Lỗi:', error));
}

// Hàm tính toán tổng tiền và tổng số lượng
function calculateTotal() {
    let totalAmount = 0;
    let totalQuantity = 0;
    let allChecked = true;
    const selectAllCheckbox = document.getElementById('select-all');

    // Duyệt qua tất cả các sản phẩm để tính tổng tiền và tổng số lượng
    document.querySelectorAll('.cart-item').forEach(item => {
        const checkbox = item.querySelector('.product-checkbox');

        // Kiểm tra xem checkbox có được chọn hay không trước khi tính tiền
        if (checkbox.checked) {
            const quantity = parseInt(item.querySelector('.quantity-input').value);
            const price = parseFloat(item.querySelector('.price-info h4').textContent.replace('.', '').replace(',', ''));
            const total = quantity * price;
            totalAmount += total;
            totalQuantity += quantity;
        }

        // Kiểm tra trạng thái checkbox, nếu có sản phẩm chưa được chọn thì bỏ tích "Chọn tất cả"
        if (!checkbox.checked) {
            allChecked = false;
        }
    });

    // Cập nhật tổng tiền và số lượng
    document.getElementById('total-amount').textContent = totalAmount.toLocaleString('vi-VN') + " VND";
    document.getElementById('total-quantity').textContent = totalQuantity;

    // Cập nhật trạng thái "Chọn tất cả"
    if (selectAllCheckbox) {
        selectAllCheckbox.checked = allChecked;
    }
}

// Hàm cập nhật số lượng khi người dùng thay đổi
function updateQuantity(change, button) {
    const cartItem = button.closest('.cart-item');
    const quantityInput = cartItem.querySelector('.quantity-input');
    const productId = cartItem.dataset.productId;
    let currentQuantity = parseInt(quantityInput.value);
    const newQuantity = currentQuantity + change;

    // Nếu số lượng mới < 1 → hiện thông báo
    if (newQuantity < 1) {
        showPopupError(cartItem, "Số lượng không thể nhỏ hơn 1");
        return;
    }
    const stock = getStockQuantity(cartItem);

    if (newQuantity > stock) {
        showPopupError(cartItem, "Vượt quá số lượng tồn kho");
        quantityInput.value = currentQuantity;
        return;
    }

    // Gửi request AJAX đến servlet cập nhật giỏ hàng
    fetch(`${contextPath}/update-cart`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({productId: productId, newQuantity: String(newQuantity)})
    })
        .then(response => response.json())
        .then(data => {
            if (data.status) {
                quantityInput.value = newQuantity;
                // Cập nhật lại phần hiển thị tổng giá của sản phẩm
                const price = parseInt(cartItem.querySelector('.price-info h4').dataset.price);
                console.log(price);
                const total = newQuantity * price;
                cartItem.querySelector('.total-info h4').textContent = total.toLocaleString('vi-VN') + " VND";
                if (cartItem.querySelector('.product-checkbox').checked) {
                    calculateTotal();
                }
            }
        })
        .catch(error => console.error('Lỗi khi cập nhật:', error));
}

// Thêm sự kiện xoá sản phẩm khỏi giỏ hàng
document.querySelectorAll('.remove-btn').forEach(button => {
    button.addEventListener('click', function (event) {
        event.preventDefault();
        const cartItem = button.closest('.cart-item');
        cartItem.remove();
        calculateTotal();

        const productId = cartItem.dataset.productId;

        // Gửi request AJAX đến servlet cập nhật giỏ hàng
        fetch(`${contextPath}/del-cart`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({productId: productId})
        })
            .then(response => response.json())
            .then(data => {
                if (data.status) {
                    const cartCountElement = document.querySelector(".cart-count");
                    if (cartCountElement) cartCountElement.innerText = data.cartSize;
                }
            })
            .catch(error => console.error('Lỗi khi xóa cart:', error));

    });
})

// Thêm sự kiện khi checkbox của sản phẩm thay đổi
document.querySelectorAll('.product-checkbox').forEach(checkbox => {
    checkbox.addEventListener('change', function () {
        const cartItem = this.closest('.cart-item');
        updateProductSelection(cartItem.dataset.productId, this.checked);
        calculateTotal();
    });
});

// Thêm sự kiện khi thay đổi số lượng sản phẩm
document.querySelectorAll('.quantity-input').forEach(input => {
    input.addEventListener('input', function() {
        if (this.closest('.cart-item').querySelector('.product-checkbox').checked) {
            calculateTotal();
        }
    });
});

// Thêm sự kiện cho checkbox "Chọn tất cả"
document.getElementById("select-all").addEventListener("change", function () {
    selectAll(this);
});

document.getElementById("remove-selected-btn").addEventListener("click", function () {
    const items = document.querySelectorAll(".cart-item");
    items.forEach(item => {
        const checkbox = item.querySelector(".product-checkbox");
        if (checkbox && checkbox.checked) {
            item.remove(); // hoặc gọi API xóa từ server
        }
    });
});

function showPopupError(inputElement, message) {
    const popup = document.createElement('div');
    popup.className = 'input-popup-error';
    popup.textContent = message;

    document.body.appendChild(popup);

    // Đợi popup được render xong để lấy chiều cao
    requestAnimationFrame(() => {
        const rect = inputElement.getBoundingClientRect();
        const popupRect = popup.getBoundingClientRect();

        // Căn giữa theo chiều ngang và nằm phía trên input
        popup.style.top = `${rect.top + window.scrollY - 35}px`;
        popup.style.left = `${rect.left + window.scrollX + rect.width / 2 - popupRect.width / 2}px`;

        popup.style.opacity = '1';

        setTimeout(() => {
            popup.style.opacity = '0';
            setTimeout(() => popup.remove(), 300);
        }, 1000);
    });
}

function getStockQuantity(cartItem) {
    const stockSpan = cartItem.querySelector('.stock-quantity');

    const stockText = stockSpan.textContent.trim(); // "Còn lại: xx"
    const match = stockText.match(/Còn lại:\s*(\d+)/);
    if (match) {
        return parseInt(match[1]);
    }
    return 0;
}

// Thêm sự kiện cho nút thanh toán
document.querySelector('.checkout-btn').addEventListener('click', function (e) {
    e.preventDefault();

    // Kiểm tra xem có sản phẩm nào được chọn không
    const hasSelectedItems = Array.from(document.querySelectorAll('.product-checkbox')).some(cb => cb.checked);

    if (!hasSelectedItems) {
        alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán');
        return;
    }

    // Chuyển hướng đến trang checkout
    window.location.href = `${contextPath}/checkout`;
});