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

    // Tự động ẩn sau 1 giây
    const timeoutId = setTimeout(() => {
        hideCartPopup();
    }, 1000);

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
    const productContainer = document.querySelector(".product-list");
    if (productContainer) {
        productContainer.addEventListener("click", function (event) {
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

    // Delegation cho product-detail page
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

function updateSelection(productId, isSelected) {
    const contextPath = document.body.dataset.contextPath || '';

    fetch(`${contextPath}/api/cart`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            action: "updateSelection",
            id: parseInt(productId),
            selected: isSelected
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === true) {
                // Update total display (Price)
                const totalPriceDisplay = document.getElementById("cart-total-price");
                if (totalPriceDisplay) {
                    totalPriceDisplay.innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data.total);
                    console.log('[updateSelection] Updated price to:', data.total);
                }

                // Update total quantity display
                const totalQuantityDisplay = document.getElementById("cart-total-quantity");
                if (totalQuantityDisplay && data.selectedQuantity !== undefined) {
                    totalQuantityDisplay.innerText = data.selectedQuantity;
                    console.log('[updateSelection] Updated quantity to:', data.selectedQuantity);
                }

                // Update "Select All" checkbox state based on current state of all checkboxes
                // This runs AFTER the checkbox state has been updated
                setTimeout(() => {
                    const allCheckboxes = document.querySelectorAll('.product-checkbox');
                    const selectAllCheckbox = document.getElementById('select-all');
                    if (selectAllCheckbox && allCheckboxes.length > 0) {
                        const allChecked = Array.from(allCheckboxes).every(cb => cb.checked);
                        selectAllCheckbox.checked = allChecked;
                        console.log('[updateSelection] Select all checkbox updated to:', allChecked);
                    }
                }, 0);
            } else {
                showCartPopup(data.message, false);
            }
        })
        .catch(error => {
            console.error("Lỗi update selection:", error);
        });
}

function selectAll(isSelected) {
    const contextPath = document.body.dataset.contextPath || '';

    fetch(`${contextPath}/api/cart`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            action: "selectAll",
            selected: isSelected
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === true) {
                // Update specific checkboxes to match 'selectAll' state
                document.querySelectorAll('.product-checkbox').forEach(cb => cb.checked = isSelected);

                const totalPriceDisplay = document.getElementById("cart-total-price");
                if (totalPriceDisplay) {
                    totalPriceDisplay.innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data.total);
                }

                // Update total quantity display
                const totalQuantityDisplay = document.getElementById("cart-total-quantity");
                if (totalQuantityDisplay && data.selectedQuantity !== undefined) {
                    totalQuantityDisplay.innerText = data.selectedQuantity;
                }
            } else {
                showCartPopup(data.message, false);
            }
        })
        .catch(error => {
            console.error("Lỗi select all:", error);
        });
}
