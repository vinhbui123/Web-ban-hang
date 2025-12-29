/* ===========================
   LẤY contextPath CHUẨN
   (không dùng ${pageContext...} trong file .js)
=========================== */
const contextPath = document.body.dataset.contextPath || '';

/* ===========================
   TOGGLE CATEGORY MENU
=========================== */
function toggleCategoryMenu() {
    const menu = document.getElementById("category-list");
    const arrowIcon = document.getElementById("arrow-icon");

    if (!menu || !arrowIcon) return;

    menu.classList.toggle("hidden");
    arrowIcon.classList.toggle("rotate");
}

/* ===========================
   LOAD CATEGORY HTML
=========================== */
function addCategory() {
    fetch(`${contextPath}/category.html`)
        .then(res => res.text())
        .then(html => {
            const categoryDiv = document.querySelector('.category');
            if (categoryDiv) categoryDiv.innerHTML = html;
        })
        .catch(err => console.error("Lỗi load category:", err));
}

/* ===========================
   PAGINATION PRODUCT LIST
=========================== */
document.addEventListener("DOMContentLoaded", function () {
    const itemsPerPage = 10;
    const productBoxes = document.querySelectorAll(".product-box");
    const pagination = document.querySelector(".pagination");

    if (!productBoxes.length || !pagination) return;

    let currentPage = 1;

    function showPage(page) {
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;

        productBoxes.forEach((box, index) => {
            box.style.display =
                index >= start && index < end ? "block" : "none";
        });
    }

    function setupPagination() {
        const totalPages = Math.ceil(productBoxes.length / itemsPerPage);
        pagination.innerHTML = "";

        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.className = "page-btn";
            if (i === currentPage) btn.classList.add("active");

            btn.addEventListener("click", () => {
                currentPage = i;
                showPage(currentPage);
                updatePagination();
            });

            pagination.appendChild(btn);
        }
    }

    function updatePagination() {
        pagination.querySelectorAll("button").forEach((btn, index) => {
            btn.classList.toggle("active", index + 1 === currentPage);
        });
    }

    showPage(currentPage);
    setupPagination();
});

/* ===========================
   LOAD PRODUCT (EDIT PAGE)
=========================== */
document.addEventListener("DOMContentLoaded", function () {

    // Nếu không có productId thì thoát (trang list)
    if (typeof productId === "undefined" || !productId) return;

    fetch(`${contextPath}/getProduct?id=${productId}`)
        .then(response => {
            // Kiểm tra HTTP status trước
            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }
            // Lấy text trước để kiểm tra xem là JSON hay HTML
            return response.text().then(text => {
                try {
                    // Cố gắng parse JSON
                    return JSON.parse(text);
                } catch (e) {
                    // Nếu lỗi parse, in nội dung HTML ra console để debug
                    console.error("Server trả về HTML thay vì JSON:", text);
                    throw new Error("Phản hồi từ server không phải là JSON hợp lệ (xem console).");
                }
            });
        })
        .then(data => {
            if (!data) throw new Error("Dữ liệu rỗng");

            // ... (Code gán dữ liệu vào input giữ nguyên như cũ)
            productNameInput.value = data.name ?? '';
            priceInput.value = data.price ?? '';
            // ...
        })
        .catch(err => {
            console.error("LỖI LOAD PRODUCT:", err);
            // Hiển thị thông báo lỗi rõ ràng hơn
            alert("Lỗi tải dữ liệu: " + err.message);
        });
});
