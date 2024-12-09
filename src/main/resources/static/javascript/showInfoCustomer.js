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

function previewImage(event) {
    var file = event.target.files[0];
    var errorText = document.getElementById('errorText');

    if (file) {
        // Check file type (only .JPEG and .PNG)
        if (!['image/jpeg', 'image/png'].includes(file.type)) {
            errorText.classList.remove('text-muted'); // Remove text-muted
            errorText.classList.add('text-danger');  // Add text-danger class
            event.target.value = '';  // Clear the input
            return;
        }

        // Check file size (max 1MB)
        if (file.size > 1024 * 1024) {
            errorText.classList.remove('text-muted'); // Remove text-muted
            errorText.classList.add('text-danger');  // Add text-danger class
            event.target.value = '';  // Clear the input
            return;
        }

        // Hide error message if the file is valid
        errorText.classList.remove('text-danger');  // Remove text-danger class
        errorText.classList.add('text-muted');  // Add text-muted to reset styling

        // If file is valid, preview the image
        var reader = new FileReader();
        reader.onload = function (e) {
            // Set the image source to the selected file
            document.getElementById('profileImage').src = e.target.result;
        }
        reader.readAsDataURL(file);  // Convert file to data URL
    }
}

document.querySelector('form').addEventListener('submit', function (event) {
    let isValid = true;

    // Lấy các span hiển thị lỗi
    const errorName = document.getElementById('errorName');
    const errorEmail = document.getElementById('errorEmail');
    const errorPhone = document.getElementById('errorPhone');
    const errorDob = document.getElementById('errorDob');

    // Xóa lỗi trước đó
    errorName.textContent = '';
    errorEmail.textContent = '';
    errorPhone.textContent = '';
    errorDob.textContent = '';

    // Lấy giá trị từ input
    const name = document.getElementById('name').value.trim();
    const email = document.getElementById('email').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const dob = document.getElementById('dob').value;

    // Kiểm tra Tên
    if (!name || /^\s/.test(name)) {
        isValid = false;
        errorName.textContent = 'Tên không được để trống và không được bắt đầu bằng dấu cách.';
    } else if (/^\d/.test(name)) {
        isValid = false;
        errorName.textContent = 'Tên không được bắt đầu bằng số.';
    } else if (name.length > 50) {
        isValid = false;
        errorName.textContent = 'Tên không được dài quá 50 ký tự.';
    }

    // Kiểm tra Email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        isValid = false;
        errorEmail.textContent = 'Email không hợp lệ.';
    } else if (/^\d/.test(email)) {
        isValid = false;
        errorEmail.textContent = 'Email không được bắt đầu bằng số.';
    } else if (/^[A-Z]/.test(email)) {
        isValid = false;
        errorEmail.textContent = 'Email không được bắt đầu bằng chữ hoa.';
    } else if (email.length > 100) {
        isValid = false;
        errorEmail.textContent = 'Email không được dài quá 100 ký tự.';
    }

    // Kiểm tra Số điện thoại (bắt đầu bằng số 0 và độ dài 10-11 ký tự)
    const phoneRegex = /^0\d{9,10}$/;
    if (!phoneRegex.test(phone)) {
        isValid = false;
        errorPhone.textContent = 'Số điện thoại phải bắt đầu bằng số 0 và có độ dài 10-11 ký tự.';
    }

    // Kiểm tra Ngày sinh
    if (!dob) {
        isValid = false;
        errorDob.textContent = 'Vui lòng chọn ngày sinh.';
    } else if (new Date(dob) > new Date()) {
        isValid = false;
        errorDob.textContent = 'Ngày sinh không được là ngày trong tương lai.';
    } else {
        const age = new Date().getFullYear() - new Date(dob).getFullYear();
        if (age > 120) {
            isValid = false;
            errorDob.textContent = 'Tuổi không được quá 120.';
        }
    }

    // Ngăn form gửi đi nếu không hợp lệ
    if (!isValid) {
        event.preventDefault();
    }
});




