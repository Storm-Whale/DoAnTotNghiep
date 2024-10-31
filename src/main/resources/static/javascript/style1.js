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

document.addEventListener("DOMContentLoaded", function() {
    const initialDisplayCount = 8;
    const products = document.querySelectorAll('.product-card');
    const loadMoreBtn = document.getElementById("loadMoreBtn");
    let isExpanded = false;

    for (let i = initialDisplayCount; i < products.length; i++) {
        products[i].style.display = "none";
    }

    window.toggleProducts = function () {
        if (!isExpanded) {
            for (let i = initialDisplayCount; i < products.length; i++) {
                products[i].style.display = "block";
            }
            loadMoreBtn.innerHTML = 'Thu gọn <i class="fas fa-chevron-up"></i>';
        } else {
            for (let i = initialDisplayCount; i < products.length; i++) {
                products[i].style.display = "none";
            }
            loadMoreBtn.innerHTML = 'Xem thêm <i class="fas fa-chevron-down"></i>';
        }
        isExpanded = !isExpanded;
    };
});

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

document.addEventListener('DOMContentLoaded', function () {
    const imgUser = document.querySelector('.imguser');
    const accountDropdown = document.querySelector('.account-dropdown');

    imgUser.addEventListener('click', function (event) {
        event.stopPropagation(); // Ngăn chặn sự kiện click lan truyền
        const isVisible = accountDropdown.style.display === 'block';
        accountDropdown.style.display = isVisible ? 'none' : 'block';
    });


    document.addEventListener('click', function () {
        accountDropdown.style.display = 'none';
    });


    accountDropdown.addEventListener('mouseleave', function () {
        accountDropdown.style.display = 'none'; // Ẩn menu khi di chuột ra ngoài
    });


    accountDropdown.addEventListener('click', function (event) {
        event.stopPropagation();
    });
});
