// let bigImg = document.querySelector('.big-img img');

function showImg(pic) {
    var bigImg = document.getElementById("main-img");
    bigImg.src = pic;
}

// Hiện thông báo khi thêm vào giỏ hàng thành công
function showPopup(message) {
    const popup = document.getElementById("cart-popup");
    if (popup) {
        popup.querySelector(".popup-content p").textContent = message;

        popup.classList.remove("hidden");

        // Tự động ẩn sau x000 = x giây
        const timeoutId = setTimeout(() => {
            hidePopup();
        }, 1000);

        // Thêm sự kiện click để ẩn popup nếu người dùng click
        function onClickAnywhere() {
            hidePopup();
        }

        popup.addEventListener("click", onClickAnywhere);

        function hidePopup() {
            popup.classList.add("hidden");
            popup.removeEventListener("click", onClickAnywhere);
            clearTimeout(timeoutId);
        }
    }
}


document.addEventListener('DOMContentLoaded', function () {
    // Lấy tất cả các phần tử .color-item
    document.querySelectorAll('.color-item').forEach(item => {
        // Thêm sự kiện click
        item.addEventListener('click', function () {
            // Loại bỏ lớp 'active' từ tất cả các phần tử
            document.querySelectorAll('.color-item').forEach(i => i.classList.remove('active'));
            // Thêm lớp 'active' vào phần tử được click
            this.classList.add('active');
        });
    });
    document.querySelector('.cart-btn').addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();


        const productIdInput = document.querySelector('#product-id');
        const productId = productIdInput ? productIdInput.value : "";
        var quantityInput = document.querySelector('#quantity-input');
        var quantity = quantityInput ? quantityInput.value : "1";

        fetch(`${contextPath}/add-cart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({productId: productId, quantity: quantity}) // Chuyển dữ liệu thành JSON để gửi
        })
            .then(response => response.json())
            .then(data => {
                if (data.status) { // Sử dụng key "status" theo phản hồi JSON của bạn
                    console.log("Thêm cart thành công!");
                    const cartCountElement = document.querySelector(".cart-count");
                    if (cartCountElement) {
                        cartCountElement.innerText = data.cartSize; // Cập nhật số lượng từ phản hồi
                    }
                    // Hiển thị popup
                    showPopup("Sản phẩm đã được thêm vào giỏ hàng thành công!");
                } else {
                    // Hiển thị popup
                    showPopup(data.message);
                }
            })
            .catch(error => {
                console.error("Lỗi:", error.message);
                alert("Có lỗi xảy ra khi thêm vào giỏ hàng:" + error.message);
            });
    });

    // Add event listener for "Mua Ngay" button
    document.querySelector('.buy-btn').addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();

        const productIdInput = document.querySelector('#product-id');
        const productId = productIdInput ? productIdInput.value : "";
        var quantityInput = document.querySelector('#quantity-input');
        var quantity = quantityInput ? quantityInput.value : "1";

        fetch(`${contextPath}/add-cart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({productId: productId, quantity: quantity})
        })
            .then(response => response.json())
            .then(data => {
                if (data.status) {
                    // Redirect to checkout page after successful addition to cart
                    window.location.href = `${contextPath}/checkout`;
                } else {
                    showPopup(data.message);
                }
            })
            .catch(error => {
                console.error("Lỗi:", error.message);
                alert("Có lỗi xảy ra khi thêm vào giỏ hàng:" + error.message);
            });
    });
});
