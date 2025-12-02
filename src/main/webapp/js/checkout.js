function showPayment(method) {
    // Ẩn tất cả các phương thức thanh toán
    document.querySelectorAll('.payment-content').forEach((content) => {
        content.classList.add('hidden');
    });

    // Hiển thị phương thức thanh toán được chọn
    document.getElementById(method).classList.remove('hidden');

    // Đổi trạng thái active của các tab
    document.querySelectorAll('.tab').forEach((tab) => {
        tab.classList.remove('active');
    });
    document.querySelector(`.tab[onclick="showPayment('${method}')"]`).classList.add('active');
}

function loadShippingMethods() {
    const addressData = document.getElementById('address-data').value;
    const shippingMethodsDiv = document.getElementById('shipping-methods');
    if (!addressData) {
        shippingMethodsDiv.innerHTML = 'Yêu cầu cập nhật địa chỉ nhận hàng!';
        return;
    }

    // Kiểm tra contextPath
    if (typeof contextPath === 'undefined') {
        console.error('contextPath is not defined');
        shippingMethodsDiv.innerHTML = '<div class="error">Lỗi hệ thống: contextPath không được định nghĩa</div>';
        return;
    }

    shippingMethodsDiv.innerHTML = '<div class="loading">Đang tải phí vận chuyển...</div>';

    // Lấy danh sách sản phẩm từ DOM
    let products = [];
    document.querySelectorAll('.product-item').forEach((item) => {
        let productId = parseInt(item.querySelector('.product-id').innerText);
        let quantity = parseInt(item.querySelector('.quantity-info').innerText);
        products.push({
            id: productId,
            quantity: quantity
        });
    });

    // Lấy tổng giá trị đơn hàng từ hàng tổng cộng
    let totalOrderValue = 0;
    const totalOrderValueElem = document.getElementById('total-order-value');
    if (totalOrderValueElem) {
        totalOrderValue = parseInt(totalOrderValueElem.textContent.replace(/[^\d]/g, ''));
    }

    // Gửi request lên API phí ship
    fetch(`${contextPath}/shipfee`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            products: products,
            totalOrderValue: totalOrderValue
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.error) {
                shippingMethodsDiv.innerHTML = `<div class="error">${data.error}</div>`;
                // Cập nhật phí ship và tổng thanh toán về 0
                updateOrderSummaryShipping(0);
                return;
            }
            // Hiển thị phí ship
            shippingMethodsDiv.innerHTML = `
            <div class="shipping-method">
                <input type="radio" name="shipping_method" checked disabled>
                <label>
                    GHN tiêu chuẩn
                    <span class="shipping-fee">₫${data.total.toLocaleString()}</span>
                </label>
                <div class="guarantee">Phí giao hàng tự động từ GHN</div>
            </div>
        `;
            // Cập nhật hidden input nếu cần
            const shippingFeeInput = document.getElementById('shipping_fee');
            if (shippingFeeInput) shippingFeeInput.value = data.total;
            // Cập nhật phí ship và tổng thanh toán trong order-summary
            updateOrderSummaryShipping(data.total);
        })
        .catch(error => {
            console.error('Error:', error);
            shippingMethodsDiv.innerHTML = '<div class="error">Có lỗi xảy ra khi tính phí vận chuyển: ' + error.message + '</div>';
            updateOrderSummaryShipping(0);
        });
}

// Hàm cập nhật phí ship và tổng thanh toán trong order-summary
function updateOrderSummaryShipping(shippingFee) {
    // Cập nhật phí vận chuyển hiển thị
    const shippingFeeElem = document.querySelector('.order-summary .shipping-fee');
    if (shippingFeeElem) {
        shippingFeeElem.textContent = `${shippingFee.toLocaleString()} VND`;
    }

    // Lấy tổng tiền hàng hiện tại từ session hoặc DOM
    let totalOrderValue = 0;
    const totalOrderValueElem = document.getElementById('total-order-value');
    if (totalOrderValueElem) {
        const match = totalOrderValueElem.innerText.match(/(\d[\d.]*)/g);
        if (match) {
            totalOrderValue = parseInt(match.join('').replace(/\./g, ''));
        }
    }

    // Tính tổng thanh toán cuối cùng
    const finalTotal = totalOrderValue + shippingFee;

    // Cập nhật tổng thanh toán
    const finalTotalElem = document.querySelector('.order-summary strong span');
    if (finalTotalElem) {
        finalTotalElem.textContent = `₫${finalTotal.toLocaleString()} VND`;
    }
}


// Gọi khi trang vừa tải xong
// Đảm bảo gọi sau khi DOM đã sẵn sàng và có địa chỉ mặc định

document.addEventListener('DOMContentLoaded', function () {
    loadShippingMethods();
});

// Khi cập nhật địa chỉ mặc định (sau khi lưu/chọn địa chỉ mới), hãy gọi:
// updateAddressDetails(response.addressDefault);
// loadShippingMethods();
// (Chèn đoạn này vào callback thành công của AJAX cập nhật địa chỉ)

// Reload shipping methods when address changes
document.querySelector('.change-shipping').addEventListener('click', function (e) {
    e.preventDefault();
    loadShippingMethods();
});

function placeOrder() {
    // Lấy ID của phương thức thanh toán đang hiển thị
    let activePayment = document.querySelector('.payment-content:not(.hidden)');
    let paymentMethod = activePayment ? activePayment.id : "unknown";
    // Status mặc định (chưa thanh toán)


    const status = 0;
    // FreeShipping
    let freeShipping = 0;
    let shippingFeeElem = document.querySelector('.shipping-fee');
    if (shippingFeeElem) {
        freeShipping = parseInt(shippingFeeElem.textContent.replace(/[^\d]/g, '')) || 0;
    }

    // Lấy danh sách sản phẩm từ JSP
    let details = [];
    document.querySelectorAll('.product-item').forEach((item) => {
        let productId = item.querySelector('.product-id').innerText;
        // let name = item.querySelector('.product-name').innerText;
        let price = item.querySelector('.price-info').innerText.replace("₫", "").trim();
        let quantity = item.querySelector('.quantity-info').innerText;
        details.push({
            productId: parseInt(productId),
            // name: name,
            price: parseInt(price),
            quantity: parseInt(quantity)
        });
    });

    // Tạo đối tượng đơn hàng
    let orderData = {
        userId: parseInt(userId),
        status: status,
        freeShipping: parseInt(freeShipping),
        paymentTypeId: paymentMethod === 'cod' ? 1 : 2,
        details: details
    };

    console.log(orderData);
    // Gửi dữ liệu đến controller bằng Fetch API
    fetch(`${contextPath}/checkout`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Đặt hàng thành công!");
                window.location.href = `${contextPath}/purchase`;
            } else {
                alert("Đặt hàng thất bại, vui lòng thử lại!" + data.message);
            }
        })
        .catch(error => console.error('Lỗi:', error));
}

function openCouponPopup() {
    document.getElementById("couponModalPopup").style.display = "flex";
}

function closeCouponPopup() {
    document.getElementById("couponModalPopup").style.display = "none";
}

function applyCoupon(code) {
    fetch(`${contextPath}/apply-coupon`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `code=${encodeURIComponent(code)}`
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                closeCouponPopup();
                alert(data.message);

                const discountAmount = data.discountAmount || 0;
                const totalText = document.querySelector('#total-order-value').textContent.replace(/[^\d]/g, '');
                const shippingText = document.querySelector('.shipping-fee').textContent.replace(/[^\d]/g, '');

                const total = parseInt(totalText || '0');
                const shipping = parseInt(shippingText || '0');
                const newTotal = total + shipping - discountAmount;

                // Format số tiền VND
                const formatter = new Intl.NumberFormat('vi-VN');
                const formattedNewTotal = formatter.format(newTotal);
                const formattedDiscount = formatter.format(discountAmount);

                // Cập nhật DOM
                document.querySelector('.order-summary strong span').textContent = `${formattedNewTotal} VND`;

                // Hiển thị mã đã áp dụng
                document.getElementById('applied-coupon-info').innerHTML = `
    <p style="margin: 0;">Mã đã áp dụng: <strong>${code}</strong></p>
    <p style="margin: 0;">Đã giảm: <strong>${formattedDiscount} VND</strong></p>
`;

                // Gán hidden input để gửi mã
                document.getElementById('selectedCouponCode').value = code;

            } else {
                alert(data.message);
            }
        });
}

