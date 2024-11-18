// * Chức năng hiển thị/ẩn sidebar bộ lọc
function toggleFilter() {
    const sidebar = document.getElementById('filter-sidebar');
    sidebar.classList.toggle('active');
    sidebar.classList.toggle('hidden');
}

// * Khởi tạo các accordion trong bộ lọc
document.addEventListener('DOMContentLoaded', function () {
    const accordions = document.querySelectorAll('.accordion');

    accordions.forEach(acc => {
        acc.addEventListener('click', function () {
            // Chuyển đổi trạng thái active của accordion
            this.classList.toggle('active');

            // Lấy phần tử panel tiếp theo
            const panel = this.nextElementSibling;

            // Điều chỉnh chiều cao của panel
            if (panel.style.maxHeight) {
                // Nếu panel đang mở, thu gọn lại
                panel.style.maxHeight = null;
                panel.style.padding = '0 10px'; // Tùy chỉnh padding khi thu gọn
            } else {
                // Nếu panel đang đóng, mở rộng ra
                panel.style.maxHeight = panel.scrollHeight + "px";
                panel.style.padding = '10px'; // Tùy chỉnh padding khi mở rộng
            }
        });

        // Đặt trạng thái ban đầu cho panel nếu accordion có class 'active'
        const panel = acc.nextElementSibling;
        if (acc.classList.contains('active')) {
            panel.style.maxHeight = panel.scrollHeight + "px";
            panel.style.padding = '10px';
        }
    });
});

// * Gửi bộ lọc và giữ nguyên trạng thái
function submitFilterForm() {
    const form = document.querySelector('form');
    const formData = new FormData(form);
    const queryParams = new URLSearchParams();

    // Lưu trạng thái các accordion đang mở
    const activeAccordions = document.querySelectorAll('.accordion.active');
    activeAccordions.forEach(acc => {
        queryParams.append('activeAccordion', acc.textContent.trim());
    });

    // Thêm các tham số lọc
    for (const [key, value] of formData.entries()) {
        if (value) queryParams.append(key, value);
    }

    // Điều hướng lại trang với tham số
    window.location.href = `${form.action}?${queryParams.toString()}`;
}

// * Khởi tạo bộ lọc khi tải lại trang
document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);

    if (isFilterOpen) {
        toggleFilter();
    }

    // Duy trì trạng thái cho các bộ lọc
    const filterElements = document.querySelectorAll('select[name], input[name]');
    filterElements.forEach(filter => {
        const paramValue = urlParams.get(filter.name);
        if (paramValue) {
            filter.value = paramValue;
        }
    });

    // Khởi tạo trạng thái accordion
    const accordions = document.querySelectorAll('.accordion');
    accordions.forEach(acc => {
        const panel = acc.nextElementSibling;

        // Kiểm tra nếu accordion có trong danh sách activeAccordions
        if (activeAccordions && activeAccordions.includes(acc.textContent.trim())) {
            acc.classList.add('active');
            panel.style.maxHeight = panel.scrollHeight + 'px';
            panel.style.padding = '10px';
        } else {
            // Kiểm tra nếu panel có giá trị lọc
            const hasFilterValue = panel.querySelectorAll('select, input').some(input => input.value);
            if (hasFilterValue) {
                acc.classList.add('active');
                panel.style.maxHeight = panel.scrollHeight + 'px';
                panel.style.padding = '10px';
            }
        }
    });
});

// What For : Chức năng hiển thị thêm sản phẩm
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

const searchInput = document.getElementById('searchInput');
const suggestionsBox = document.getElementById('suggestions');
const suggestionList = document.getElementById('suggestionList');

searchInput.addEventListener('input', function() {
    const query = this.value.toLowerCase();
    suggestionList.innerHTML = ''; // Xóa danh sách gợi ý cũ
    suggestionsBox.style.display = 'none'; // Ẩn khung gợi ý

    if (query) {
        const filteredSuggestions = san_phams.filter(item =>
            item.sanPhamResponse.tenSanPham.toLowerCase().includes(query)
        );

        if (filteredSuggestions.length > 0) {
            filteredSuggestions.forEach(item => {
                const li = document.createElement('li');

                // Tạo thẻ a với liên kết
                const link = document.createElement('a');
                link.href = `/client/san_pham_chi_tiet/${item.sanPhamResponse.id}`; // Thêm ID sản phẩm vào URL
                link.style.textDecoration = 'none'; // Bỏ gạch chân cho link

                // Tạo phần tử hình ảnh
                const img = document.createElement('img');
                img.src = item.sanPhamResponse.anhUrl; // Gán thuộc tính src
                img.alt = item.sanPhamResponse.tenSanPham; // Gán thuộc tính alt

                // Tạo phần tử thông tin sản phẩm
                const productInfo = document.createElement('div');
                productInfo.className = 'product-info';

                const name = document.createElement('span');
                name.textContent = item.sanPhamResponse.tenSanPham;

                const price = document.createElement('span');
                price.textContent = `${item.gia.toLocaleString()} VNĐ`; // Định dạng giá

                productInfo.appendChild(name);
                productInfo.appendChild(price);

                // Thêm hình ảnh và thông tin vào thẻ a
                link.appendChild(img);
                link.appendChild(productInfo);
                li.appendChild(link); // Thêm thẻ a vào thẻ li

                li.onclick = function() {
                    // Khi click vào li, sẽ chuyển hướng đến trang chi tiết sản phẩm
                    window.location.href = link.href;
                };

                suggestionList.appendChild(li);
            });
            suggestionsBox.style.display = 'block'; // Hiện khung gợi ý
        }
    }
});

// Ẩn khung gợi ý khi click ra ngoài
document.addEventListener('click', function(event) {
    if (!searchInput.contains(event.target)) {
        suggestionsBox.style.display = 'none';
    }
});

