// --------------------------------------------------- Tăng Giảm Giá Trị Bằng Button ---------------------------------------------------
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
// --------------------------------------------------- Tăng Giảm Giá Trị Bằng Button ---------------------------------------------------

// document.getElementById('add-to-cart-btn').addEventListener('click', function () {
//     var notification = document.getElementById('notification');
//     notification.style.display = 'block';
//     setTimeout(function () {
//         notification.style.display = 'none';
//     }, 3000);
// });

// --------------------------------------------------- Start Thay Đổi Khung Ảnh Ở Dưới Main Img  ---------------------------------------------------
// Hàm thay đổi ảnh chính khi người dùng chọn một thumbnail
function changeMainImage(src) {
    // Thay đổi thuộc tính `src` của ảnh chính với đường dẫn mới
    document.getElementById('mainImage').src = src;
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
function filterSizes(mausacId) {
    // Làm mờ tất cả các size
    document.querySelectorAll('.size-label').forEach(function (label) {
        const labelElement = label.querySelector('label');
        if (labelElement) {
            labelElement.style.border = '2px solid black'; // Viền mặc định
            labelElement.style.color = 'black'; // Màu chữ mặc định
            labelElement.style.opacity = '0.5'; // Làm mờ
            labelElement.style.pointerEvents = 'none'; // Ngăn không cho tương tác
        }
    });

    // Hiển thị các size tương ứng với màu đã chọn
    if (mapMauSacKichCo[mausacId]) {
        mapMauSacKichCo[mausacId].forEach(function (kichCo) {
            const sizeLabel = document.getElementById(kichCo.tenKichCo);
            if (sizeLabel) {
                const labelElement = sizeLabel.closest('.size-label').querySelector('label');
                if (labelElement) {
                    labelElement.style.opacity = '1'; // Hiển thị rõ ràng
                    labelElement.style.pointerEvents = 'auto'; // Cho phép tương tác
                    labelElement.style.color = 'rgba(0, 0, 0, 0.5)'; // Màu chữ mờ
                }
            }
        });
        const oldPriceElement = document.querySelector('.price');
        const oldPrice = parseFloat(oldPriceElement.textContent)
        let newPrice = oldPrice
        mapScpt.forEach(function (item) {
            if (item.idMauSac === mausacId) {
                newPrice = item.gia
            }
        })
        oldPriceElement.innerHTML = newPrice.toLocaleString('vi-VN') + ' đ'
    }
}
// -------------------------------------------------- End Lấy Kích Cỡ Khi Ấn Radio Màu Sắc & Giá Tiền Khi Chọn Ảnh  ---------------------------------------------------
