// Chức năng hiển thị/ẩn sidebar bộ lọc
function toggleFilter() {
    const sidebar = document.getElementById('filter-sidebar');
    if (sidebar.classList.contains('active')) {
        // Ẩn sidebar
        sidebar.classList.remove('active');
        sidebar.classList.add('hidden');
    } else {
        // Hiện sidebar
        sidebar.classList.remove('hidden');
        sidebar.classList.add('active');
    }
}

// Khởi tạo các accordion trong bộ lọc
document.addEventListener('DOMContentLoaded', function () {
    const accordions = document.querySelectorAll('.accordion');

    accordions.forEach(acc => {
        acc.addEventListener('click', function () {
            // Chuyển đổi trạng thái active của accordion
            this.classList.toggle('active');

            // Lấy phần panel tiếp theo
            const panel = this.nextElementSibling;

            // Điều chỉnh chiều cao của panel
            if (panel.style.maxHeight) {
                // Thu gọn panel
                panel.style.maxHeight = null;
                panel.style.padding = '0 10px';
            } else {
                // Mở rộng panel
                panel.style.maxHeight = panel.scrollHeight + "px";
                panel.style.padding = '10px';
            }
        });
    });
});
// What For : Chức năng hiển thị thêm sản phẩm
// Chức năng "Xem thêm" sản phẩm
document.addEventListener("DOMContentLoaded", function () {
    const initialDisplayCount = 19; // Số sản phẩm hiển thị ban đầu
    const products = document.querySelectorAll('.product-card');
    const loadMoreBtn = document.getElementById("loadMoreBtn");
    let isExpanded = false;

    // Ẩn các sản phẩm vượt quá số lượng ban đầu
    for (let i = initialDisplayCount; i < products.length; i++) {
        products[i].style.display = "none";
    }

    // Hàm chuyển đổi hiển thị sản phẩm
    window.toggleProducts = function () {
        if (!isExpanded) {
            // Hiển thị tất cả sản phẩm
            for (let i = initialDisplayCount; i < products.length; i++) {
                products[i].style.display = "block";
            }
            loadMoreBtn.innerHTML = 'Thu gọn <i class="fas fa-chevron-up"></i>';
        } else {
            // Ẩn bớt sản phẩm
            for (let i = initialDisplayCount; i < products.length; i++) {
                products[i].style.display = "none";
            }
            loadMoreBtn.innerHTML = 'Xem thêm <i class="fas fa-chevron-down"></i>';
        }
        isExpanded = !isExpanded;
    };
});

// What For :  Hiển thị thông báo khi mua xong
document.addEventListener('DOMContentLoaded', () => {
    if (localStorage.getItem('paymentSuccess') === 'true') {
        showSuccessAlert();
        // Xóa trạng thái để không hiển thị lại khi tải lại trang
        localStorage.removeItem('paymentSuccess');
    }
});

function showSuccessAlert() {
    Swal.fire({
        position: "bottom-end",
        icon: "success",
        title: "Thanh Toán Thành Công",
        showConfirmButton: false,
        timer: 1750,
        backdrop: false,
        customClass: {
            popup: 'sizeAlert'
        }
    });
}