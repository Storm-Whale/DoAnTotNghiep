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

document.addEventListener('DOMContentLoaded', function () {
    // Lấy tất cả các phần tử có id hoặc class phù hợp
    const dateElements = document.querySelectorAll('#ngayTaoLSHD');

    // Định dạng ngày mới
    dateElements.forEach(element => {
        const rawDate = element.textContent.trim(); // Lấy giá trị ngày hiện tại
        if (rawDate) {
            const formattedDate = formatLocalDateTime(rawDate);
            element.textContent = formattedDate; // Gán lại giá trị đã format
        }
    });

    // Hàm định dạng LocalDateTime
    function formatLocalDateTime(dateTimeString) {
        const date = new Date(dateTimeString);
        if (!isNaN(date)) {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit', second: '2-digit' };
            return date.toLocaleString('vi-VN', options);
        }
        return dateTimeString; // Trả về giá trị cũ nếu không hợp lệ
    }
});