//Ẩn, hiện danh sách Menu
function toggleCategoryMenu() {
    const menu = document.getElementById("category-list");
    const arrowIcon = document.getElementById("arrow-icon");

    menu.classList.toggle("hidden"); // Ẩn/hiện ul khi nhấp vào span
    arrowIcon.classList.toggle("rotate"); // Xoay mũi tên
}

// Hàm để tải nội dung từ category.html và chèn vào div.category
function addCategory() {
    fetch('category.html')
        .then(response => response.text())
        .then(html => {
            document.querySelector('.category').innerHTML = html;
        })
        .catch(error => console.log('Lỗi tải file category:', error));
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

document.addEventListener("DOMContentLoaded", function () {
    const itemsPerPage = 10; // Số lượng sản phẩm mỗi trang (2 dòng x 5 sản phẩm)
    const productBoxes = document.querySelectorAll(".product-box");
    const pagination = document.querySelector(".pagination");
    let currentPage = 1;

    function showPage(page) {
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;

        productBoxes.forEach((box, index) => {
            box.style.display = (index >= start && index < end) ? "block" : "none";
        });
    }

    function setupPagination() {
        const totalPages = Math.ceil(productBoxes.length / itemsPerPage);
        pagination.innerHTML = "";

        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement("button");
            button.innerText = i+"";
            button.classList.add("page-btn");
            button.classList.toggle("active", i === currentPage);
            button.addEventListener("click", () => {
                currentPage = i;
                showPage(currentPage);
                updatePagination();
            });
            pagination.appendChild(button);
        }
    }

    function updatePagination() {
        const buttons = pagination.querySelectorAll("button");
        buttons.forEach((button, index) => {
            button.classList.toggle("active", index + 1 === currentPage);
        });
    }

    const productContainer = document.querySelector(".product-list");
    productContainer.addEventListener("click", function (event) {
        const button = event.target.closest(".add-to-cart");
        if (!button) return;

        event.preventDefault();
        event.stopPropagation();

        const productBox = button.closest(".product-box");
        const productIdElement = productBox.querySelector(".product-id");
        const productId = productIdElement ? productIdElement.innerText.trim() : null;

        if (!productId) return;

        fetch(`${contextPath}/add-cart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ productId : productId})
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === true) {
                    const cartCountElement = document.querySelector(".cart-count");
                    if (cartCountElement) cartCountElement.innerText = data.cartSize;
                    showPopup("Sản phẩm đã được thêm vào giỏ hàng thành công!");
                } else {
                    if (data.status === false)
                    showPopup(data.message);
                }
            })
            .catch(error => {
                console.error("Lỗi:", error.message);
                alert("Có lỗi xảy ra khi thêm vào giỏ hàng: " + error.message);
            });
    });

    showPage(currentPage);
    setupPagination();
});
fetch(`${contextPath}/getProduct?id=${productId}`)
    .then(response => {
        console.log('Response status:', response.status);
        return response.json();
    })
    .then(data => {
        console.log('Product data:', data);
        productNameInput.value = data.name || '';
        priceInput.value = data.price || '';
        quantityInput.value = data.quantity || '';
        categoryInput.value = data.catalog_id || '';
        descriptionInput.value = data.description || '';

        // Đặt action của form sang chế độ cập nhật
        form.action = `${contextPath}/adminEdit?productId=${productId}`;
    })
    .catch(error => {
        console.error('Lỗi:', error);
    });

