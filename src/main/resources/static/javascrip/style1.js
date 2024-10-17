function toggleFilter() {
    const sidebar = document.getElementById('filter-sidebar');
    if (sidebar.classList.contains('active')) {
        sidebar.classList.remove('active');
        sidebar.classList.add('hidden');
    } else {
        sidebar.classList.remove('hidden');
        sidebar.classList.add('active');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const accordions = document.querySelectorAll('.accordion');

    accordions.forEach(acc => {
        acc.addEventListener('click', function () {
            // Toggle the "active" class for the accordion
            this.classList.toggle('active');

            // Get the next sibling panel
            const panel = this.nextElementSibling;

            // Toggle display of the panel
            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
                panel.style.padding = '0 10px'; // Reset padding when collapsed
            } else {
                panel.style.maxHeight = panel.scrollHeight + "px";
                panel.style.padding = '10px'; // Add padding when expanded
            }
        });
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const banner = document.querySelector('.banner');

    // Thêm lớp fade-in sau khi trang đã tải xong
    banner.classList.add('fade-in');
});
document.addEventListener('DOMContentLoaded', function () {
    const productItems = document.querySelectorAll('.product-item');
    const productCountElement = document.getElementById('product-count');

    // Cập nhật số lượng sản phẩm
    const productCount = productItems.length;
    productCountElement.textContent = `${productCount} sản phẩm`;

    // Hiệu ứng chuyển tiếp cho banner
    const banner = document.querySelector('.banner');
    const img = banner.querySelector('img');

    img.addEventListener('load', function () {
        banner.classList.add('loaded');
    });

    if (img.complete) {
        banner.classList.add('loaded');
    }
});
document.addEventListener("DOMContentLoaded", function () {
    const products = document.querySelectorAll(".product-card");
    const loadMoreBtn = document.getElementById("loadMoreBtn");

    // Ẩn các sản phẩm sau 8 sản phẩm đầu tiên
    const initialDisplayCount = 8;
    for (let i = initialDisplayCount; i < products.length; i++) {
        products[i].style.display = "none";
    }

    // Hàm để hiển thị thêm sản phẩm
    window.showMoreProducts = function () {
        for (let i = initialDisplayCount; i < products.length; i++) {
            products[i].style.display = "block";
        }
        loadMoreBtn.style.display = "none"; // Ẩn nút sau khi hiển thị tất cả sản phẩm
    };
});
