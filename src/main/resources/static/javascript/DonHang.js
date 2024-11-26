document.addEventListener("DOMContentLoaded", function () {
    const filterButtons = document.querySelectorAll(".filter-btn");
    const orderItems = document.querySelectorAll(".order-item");

    // Lắng nghe sự kiện click trên các button lọc trạng thái
    filterButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            // Loại bỏ lớp active khỏi tất cả các button
            filterButtons.forEach(function (btn) {
                btn.classList.remove("active");
            });

            // Thêm lớp active vào button đang được chọn
            button.classList.add("active");

            // Lấy trạng thái của button được chọn
            const status = button.getAttribute("data-status");

            // Lọc các sản phẩm theo trạng thái
            orderItems.forEach(function (item) {
                if (status === "all") {
                    item.style.display = "block";  // Hiển thị tất cả
                } else {
                    if (item.getAttribute("data-status") === status) {
                        item.style.display = "block";  // Hiển thị sản phẩm theo trạng thái
                    } else {
                        item.style.display = "none";  // Ẩn sản phẩm không khớp trạng thái
                    }
                }
            });
        });
    });
});
