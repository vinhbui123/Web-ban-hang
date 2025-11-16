function openPurchaseDetailPopup() {
    const modal = document.getElementById("purchaseModal");
    modal.style.display = "block";
}

function closePurchaseDetailPopup() {
    const modal = document.getElementById("purchaseModal");
    modal.style.display = "none";
}

// Đóng popup nếu click ra ngoài phần nội dung
window.onclick = function (event) {
    const modal = document.getElementById("purchaseModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
};

