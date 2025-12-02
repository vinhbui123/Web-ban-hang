document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.tab');
    const orderBlocks = document.querySelectorAll('.order-block');

    const buttonVisibility = {
        '0': ['cancel', 'connect'],
        '1': ['cancel', 'connect'],
        '2': ['connect'],
        '3': ['connect', 'ratting', 'reorder'],
        '4': ['reorder', 'connect'],
    };

    function updateVisibility(selectedStatus) {
        orderBlocks.forEach(orderBlock => {
            const status = orderBlock.getAttribute('data-index');
            const shouldShow = (selectedStatus === 'all' || status === selectedStatus);

            // Hiện/ẩn đơn hàng
            orderBlock.style.display = shouldShow ? 'block' : 'none';

            // Xử lý nút
            const buttons = orderBlock.querySelectorAll('.order-actions button');
            buttons.forEach(button => {
                const classList = Array.from(button.classList);
                const match = buttonVisibility[status]?.find(cls => classList.includes(cls));
                if (shouldShow && match) {
                    button.style.display = 'inline-block';
                } else {
                    button.style.display = 'none';
                }
            });
        });
    }

    // Bắt sự kiện click tab
    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            tabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
            const selectedStatus = this.getAttribute('data-index');
            updateVisibility(selectedStatus);
        });
    });

    // Gọi mặc định
    document.querySelector('.tab.active').click();
});


function cancelOrder(orderId) {
    if (confirm("Bạn chắc chắn muốn hủy đơn hàng #" + orderId + " ?")) {
        fetch(`${contextPath}/cancel-order-customer`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({orderId: orderId}) // ← gửi dạng JSON rõ ràng
        })
            .then(res => res.json())
            .then(data => {
                alert(data.message);
                if (data.success) {
                    location.reload(); // reload lại trang để cập nhật trạng thái đơn
                }
            })
            .catch(error => {
                console.error('Lỗi hủy đơn:', error);
                alert("Đã xảy ra lỗi khi hủy đơn hàng.");
            });
    }
}

function reorder(productId) {
    window.location.href = contextPath + '/product-detail?id=' + productId;
}