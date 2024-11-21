const searchInput = document.getElementById('searchInput');
const suggestionsBox = document.getElementById('suggestions');
const suggestionList = document.getElementById('suggestionList');

searchInput.addEventListener('input', function () {
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
                img.src = '/' + item.sanPhamResponse.anhUrl; // Gán thuộc tính src
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

                li.onclick = function () {
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
document.addEventListener('click', function (event) {
    if (!searchInput.contains(event.target)) {
        suggestionsBox.style.display = 'none';
    }
});


// Lấy tất cả các phần tử có class "ngayTaoElement"
const ngayTaoElements = document.querySelectorAll('.ngayTaoElement');

// Duyệt qua tất cả các phần tử và chuyển đổi ngày tháng
ngayTaoElements.forEach(element => {
    const rawDate = element.innerText;

    // Tạo đối tượng Date từ chuỗi
    const date = new Date(rawDate);

    // Định dạng ngày tháng (ví dụ: dd/MM/yyyy)
    const formattedDate = date.toLocaleDateString("vi-VN", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });

    // Gán giá trị đã định dạng lại cho phần tử
    element.innerText = formattedDate;
});

document.addEventListener('DOMContentLoaded', function () {
    // Lấy URL hiện tại
    const currentUrl = window.location.href;

    // Lấy tất cả các tab
    const tabs = document.querySelectorAll('.nav-link');

    let activeTabFound = false;

    // Kiểm tra URL để thêm 'active' vào tab tương ứng
    tabs.forEach(tab => {
        // Lấy href đầy đủ của tab
        const tabHref = tab.href; // Lấy URL đầy đủ từ thuộc tính href

        // So sánh URL hiện tại với URL của tab (phải chính xác)
        if (currentUrl === tabHref) {
            tab.classList.add('active'); // Thêm class 'active' nếu trùng
            activeTabFound = true;
        } else {
            tab.classList.remove('active'); // Loại bỏ 'active' ở các tab khác
        }
    });

    // Nếu không có tab nào được chọn, thêm 'active' vào tab đầu tiên
    if (!activeTabFound) {
        tabs[0].classList.add('active');
    }
});

function confirmDelete(event, orderId) {
    event.preventDefault();

    Swal.fire({
        title: 'Bạn có chắc chắn?',
        text: 'Bạn có muốn hủy đơn hàng này không? Hành động này không thể hoàn tác!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        if (result.isConfirmed) {
            // User confirmed the action
            $.ajax({
                url: '/admin/hoadon/delete_client/' + orderId,
                type: 'GET',
                success: function(response) {
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Đơn hàng đã được hủy',
                        icon: 'success'
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function(xhr) {
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Không thể xóa đơn hàng',
                        icon: 'error'
                    });
                }
            });
        }
    });
}


