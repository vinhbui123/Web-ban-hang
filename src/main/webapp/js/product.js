const contextPath = "${pageContext.request.contextPath}";
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

// showPopup removed (handled by cart.js showCartPopup)

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
            button.innerText = i + "";
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
    // productContainer click listener for add-to-cart removed (handled by cart.js)

    showPage(currentPage);
    setupPagination();
});
fetch(`${contextPath}/getProduct?id=${productId}`)
    .then(response => {
        console.log('Response status:', response.status);
        return response.json();
    })
    .then(data => {
        // console.log('Product data:', data);
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

