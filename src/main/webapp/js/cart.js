// Hiển thị popup thông báo
function showCartPopup(message, isSuccess = true) {
    const popup = document.getElementById("cart-popup");
    if (!popup) return;

    const popupContent = popup.querySelector(".popup-content p");
    if (popupContent) {
        popupContent.textContent = message;
        popupContent.style.color = isSuccess ? "#333" : "#d9534f";
    }

    popup.classList.remove("hidden");

    // Tự động ẩn sau 2 giây
    const timeoutId = setTimeout(() => {
        hideCartPopup();
    }, 2000);

    // Click để ẩn popup
    function onClickAnywhere() {
        hideCartPopup();
    }
    popup.addEventListener("click", onClickAnywhere);

    function hideCartPopup() {
        popup.classList.add("hidden");
        popup.removeEventListener("click", onClickAnywhere);
        clearTimeout(timeoutId);
    }
}

// Cập nhật số lượng hiển thị trên icon giỏ hàng
function updateCartCount(count) {
    const cartCountElement = document.querySelector(".cart-count");
    if (cartCountElement) {
        cartCountElement.textContent = count;
        // Hiệu ứng animation khi cập nhật
        cartCountElement.classList.add("cart-updated");
        setTimeout(() => {
            cartCountElement.classList.remove("cart-updated");
        }, 300);
    }
}

// Xử lý Add to Cart
function addToCart(productId, quantity = 1) {
    const contextPath = document.body.dataset.contextPath || '';

    fetch(`${contextPath}/api/cart`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            productId: parseInt(productId),
            quantity: quantity
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === true) {
                updateCartCount(data.cartSize);
                showCartPopup(data.message, true);
            } else {
                if (data.redirect) {
                    window.location.href = data.redirect;
                    return;
                }
                showCartPopup(data.message, false);
            }
        })
        .catch(error => {
            console.error("Lỗi add-to-cart:", error);
            showCartPopup("Có lỗi xảy ra, vui lòng thử lại!", false);
        });
}

// Event Delegation cho tất cả button add-to-cart
document.addEventListener("DOMContentLoaded", function () {
    // Delegation trên product-list
    const productContainer = document.querySelector(".product-list");
    if (productContainer) {
        productContainer.addEventListener("click", function (event) {
            // Match cả 2 class: .add-to-cart (button) và .add-to-cart-btn (link)
            const button = event.target.closest(".add-to-cart, .add-to-cart-btn");
            if (!button) return;

            event.preventDefault();
            event.stopPropagation();

            const productBox = button.closest(".product-box");
            const productIdElement = productBox.querySelector(".product-id");
            const productId = productIdElement ? productIdElement.textContent.trim() : null;

            if (productId) {
                addToCart(productId);
            }
        });
    }

    // Delegation cho product-detail page (nếu có)
    const detailAddBtn = document.querySelector(".detail-add-to-cart");
    if (detailAddBtn) {
        detailAddBtn.addEventListener("click", function (event) {
            event.preventDefault();
            const productId = this.dataset.productId;
            const quantityInput = document.querySelector(".quantity-input");
            const quantity = quantityInput ? parseInt(quantityInput.value) || 1 : 1;

            if (productId) {
                addToCart(productId, quantity);
            }
        });
    }
});
