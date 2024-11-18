// --------------------------------------------------- Start Tăng Giảm Giá Trị Bằng Button ---------------------------------------------------
document.getElementById('button-minus').addEventListener('click', function () {
    var input = document.getElementById('input-number');
    var value = parseInt(input.value);
    if (value > parseInt(input.min)) {
        input.value = value - 1;
    }
});

document.getElementById('button-plus').addEventListener('click', function () {
    var input = document.getElementById('input-number');
    var value = parseInt(input.value);
    if (value < parseInt(input.max)) {
        input.value = value + 1;
    }
});
// --------------------------------------------------- End Tăng Giảm Giá Trị Bằng Button ---------------------------------------------------
// --------------------------------------------------- Start Thay Đổi Khung Ảnh Ở Dưới Main Img  ---------------------------------------------------
// Hàm thay đổi ảnh chính khi người dùng chọn một thumbnail
function changeMainImage(src) {
    // Thay đổi hình ảnh chính
    const mainImage = document.querySelector('.main-image');
    mainImage.src = src;

    // Lấy tất cả các thumbnail
    const thumbnails = document.querySelectorAll('.thumbnail-container img');

    // Xóa lớp 'selected' khỏi tất cả thumbnail
    thumbnails.forEach(thumbnail => {
        thumbnail.classList.remove('selected');
    });

    // Thêm lớp 'selected' cho thumbnail hiện tại
    const selectedThumbnail = Array.from(thumbnails).find(thumbnail => thumbnail.src === src);
    if (selectedThumbnail) {
        selectedThumbnail.classList.add('selected');
    }
}

// Nếu cần thêm chức năng chuyển trang ảnh thumbnail, có thể bổ sung như sau:

let currentThumbnailStartIndex = 0; // Biến lưu trữ chỉ số bắt đầu của thumbnail hiện tại
const thumbnailsPerPage = 4; // Số lượng thumbnail hiển thị trên mỗi trang
const thumbnails = document.querySelectorAll('.thumbnails img'); // Lấy tất cả các ảnh thumbnail

// Hàm cập nhật hiển thị thumbnail
function updateThumbnailDisplay() {
    // Ẩn tất cả thumbnail
    thumbnails.forEach((thumbnail, index) => {
        thumbnail.style.display = (index >= currentThumbnailStartIndex && index < currentThumbnailStartIndex + thumbnailsPerPage) ? 'inline-block' : 'none';
    });

    // Hiển thị hoặc ẩn nút "prev" và "next" dựa vào vị trí hiện tại
    document.querySelector('.prev-btn').style.display = currentThumbnailStartIndex === 0 ? 'none' : 'block';
    document.querySelector('.next-btn').style.display = currentThumbnailStartIndex + thumbnailsPerPage >= thumbnails.length ? 'none' : 'block';
}

// Hàm chuyển đến thumbnail tiếp theo
function nextThumbnail() {
    if (currentThumbnailStartIndex + thumbnailsPerPage < thumbnails.length) {
        currentThumbnailStartIndex += thumbnailsPerPage;
        updateThumbnailDisplay(); // Cập nhật lại hiển thị thumbnail
    }
}

// Hàm chuyển đến thumbnail trước đó
function prevThumbnail() {
    if (currentThumbnailStartIndex > 0) {
        currentThumbnailStartIndex -= thumbnailsPerPage;
        updateThumbnailDisplay(); // Cập nhật lại hiển thị thumbnail
    }
}

// Gọi hàm để khởi tạo hiển thị ban đầu
updateThumbnailDisplay();

// Ham De Chuyen Anh Tu Radio Mau Sang Main Img
function changeImgColor() {
    // Lấy ảnh của màu sắc được chọn
    var selectedColorImg = event.currentTarget.querySelector('img').src;

    // Thay đổi ảnh trong thẻ mainImage
    var mainImage = document.getElementById('mainImage');
    mainImage.src = selectedColorImg;
}

// --------------------------------------------------- End Thay Đổi Khung Ảnh Ở Dưới Main Img  ---------------------------------------------------
// -------------------------------------------------- Start Lấy Kích Cỡ Khi Ấn Radio Màu Sắc & Giá Tiền Khi Chọn Ảnh  ---------------------------------------------------
var mapMauSacKichCo = JSON.parse(mauSacToKichCoJson);
var mapScpt = JSON.parse(spcts)

document.querySelectorAll('.color-item').forEach(item => {
    item.addEventListener('click', function () {
        // Xóa selected class từ tất cả các color items
        document.querySelectorAll('.color-item').forEach(i => {
            i.classList.remove('selected');
        });

        // Thêm selected class cho item được chọn
        this.classList.add('selected');

        // Lấy radio input trong color item này và kích hoạt nó
        const radioInput = this.querySelector('input[type="radio"]');
        if (radioInput) {
            radioInput.checked = true;
            // Gọi hàm filter sizes với ID màu sắc
            filterSizes(radioInput.value);
        }

        // Thực hiện thay đổi ảnh
        changeImgColor();
    });
});

// Cập nhật hàm filterSizes
// Reset và cập nhật trạng thái của tất cả các size
function filterSizes(mausacId) {
    // Reset tất cả các size về trạng thái disabled
    document.querySelectorAll('.size-label').forEach(function (label) {
        const labelElement = label.querySelector('label');
        const radioInput = label.querySelector('input[type="radio"]');

        if (labelElement) {
            labelElement.classList.add('disabled');
            if (radioInput) {
                radioInput.disabled = true; // Vô hiệu hóa input radio
                radioInput.checked = false;
            }
        }
    });

    // Bật các size phù hợp với màu sắc đã chọn
    if (mapMauSacKichCo[mausacId]) {
        mapMauSacKichCo[mausacId].forEach(function (kichCo) {
            const sizeLabel = document.getElementById(kichCo.tenKichCo);
            if (sizeLabel) {
                const labelElement = sizeLabel.closest('.size-label').querySelector('label');
                if (labelElement) {
                    labelElement.classList.remove('disabled');
                    sizeLabel.disabled = false; // Bật lại input radio cho size hợp lệ
                }
            }
        });
    }

    // Cập nhật giá khi chọn màu
    updatePrice(mausacId);
}

// Thêm event listener cho các size radio buttons
document.querySelectorAll('.size-radio').forEach(function(radio) {
    radio.addEventListener('click', function(event) {
        if (this.disabled) {
            event.preventDefault(); // Ngăn chặn việc chọn nếu size bị disabled
            return false;
        }
    });
});

// Cập nhật CSS để hiển thị rõ ràng các size không khả dụng
document.head.insertAdjacentHTML('beforeend', `
    <style>
        .size-label input[type="radio"]:disabled + label {
            opacity: 0.5;
            cursor: not-allowed;
            background-color: #f5f5f5;
        }
        
        .size-label label.disabled {
            opacity: 0.5;
            cursor: not-allowed;
            background-color: #f5f5f5;
            pointer-events: none;
        }
    </style>
`);

// Hàm cập nhật giá
function updatePrice(mausacId) {
    const priceElement = document.querySelector('.price');
    const currentPrice = parseFloat(priceElement.textContent.replace(/\D/g, ''));
    let newPrice = currentPrice;

    mapScpt.forEach(function (item) {
        if (item.idMauSac === parseInt(mausacId)) {
            newPrice = item.gia;
        }
    });

    priceElement.innerHTML = newPrice.toLocaleString('vi-VN') + ' đ';
}

// -------------------------------------------------- End Lấy Kích Cỡ Khi Ấn Radio Màu Sắc & Giá Tiền Khi Chọn Ảnh  ---------------------------------------------------
// -------------------------------------------------- Start Thêm sản phẩm mới vào giỏ hàng  ---------------------------------------------------
document.addEventListener('DOMContentLoaded', function () {

    const addToCartBtn = document.getElementById('add-to-cart-btn');
    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', function () {
            const formData = new FormData(document.getElementById('cart-form'));
            const idSP = document.querySelector('input[name=idSP]').value;

            fetch(`/client/add_sp_vao_gio_hang/${idSP}`, {
                method: 'POST',
                body: formData,
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    }
                    throw new Error('Có lỗi xảy ra khi thêm sản phẩm');
                })
                .then(() => {
                    showSuccessAlert();
                })
                .catch(error => {
                    console.error(error);
                    showErrorAlert();
                });
        });
    }
})
// -------------------------------------------------- End Thêm sản phẩm mới vào giỏ hàng  ---------------------------------------------------
// -------------------------------------------------- Start Alert  ---------------------------------------------------
// Hàm hiển mua hàng thành công
function showSuccessAlert() {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
    Toast.fire({
        icon: "success",
        title: "Mua sản phẩm thành công"
    });
}
// Hàm hiển thị lỗi mua hàng thất bại
function showErrorAlert() {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });

    Toast.fire({
        icon: "error",
        title: "Có lỗi xảy ra khi thêm sản phẩm"
    });
}

function logout() {
    window.location.href = '/login/logout';
    window.location.reload();
}
// -------------------------------------------------- End Alert  ---------------------------------------------------

// Tìm kiếm sản phẩm ở header
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
