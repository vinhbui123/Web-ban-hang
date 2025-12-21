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

    // console.log(orderData);
    // Gửi dữ liệu đến controller
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
