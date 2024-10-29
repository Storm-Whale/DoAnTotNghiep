var dialogModal = document.getElementById("dialogModal");
var openDialogBtn = document.getElementById("openDialogBtn");
var closeDialogBtn = document.getElementById("closeDialogBtn");

// Khi nhấn vào nút "Mở Dialog", hiển thị hộp thoại (modal)
openDialogBtn.onclick = function() {
    dialogModal.style.display = "block";
}

// Khi nhấn vào nút "Đóng" (x), ẩn hộp thoại (modal)
closeDialogBtn.onclick = function() {
    dialogModal.style.display = "none";
}

// Khi nhấn ra ngoài vùng hộp thoại, cũng ẩn modal
window.onclick = function(event) {
    if (event.target === dialogModal) {
        dialogModal.style.display = "none";
    }
}