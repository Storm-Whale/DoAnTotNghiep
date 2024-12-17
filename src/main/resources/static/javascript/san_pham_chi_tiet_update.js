$(document).ready(function () {
    $('.js-select2').select2({
        placeholder: "Chọn một tùy chọn",
        allowClear: true
    });
});

let selectedImages = 0;  // Số lượng ảnh đã chọn
// Danh sách các ID ảnh đã chọn
let selectedImageIds = [];  // Giả sử các ID ảnh sẽ là số nguyên

// Hàm để chọn hoặc bỏ chọn ảnh
function toggleBorder(imgElement, imageId) {
    if (imgElement.classList.contains("selected")) {
        imgElement.classList.remove("selected");
        selectedImages--;
        const index = selectedImageIds.indexOf(imageId);
        if (index > -1) {
            selectedImageIds.splice(index, 1);
        }
    } else {
        imgElement.classList.add("selected");
        selectedImageIds.push(imageId);
        selectedImages++;
    }
}

// Xử lý sự kiện khi tải ảnh lên
document.getElementById('fileUpload').addEventListener('change', function(event) {
    // Lấy danh sách các tệp đã chọn
    const files = event.target.files;

    // Kiểm tra xem có ảnh được chọn không
    if (files.length === 0) {
        document.getElementById('error-fileUpload').style.display = 'block';
        return;
    }

    // Kiểm tra số lượng file được tải lên với số lượng ảnh đã chọn
    if (files.length > selectedImages) {
        // Hiển thị cảnh báo bằng SweetAlert nếu tải lên quá số ảnh đã chọn
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: `Bạn chỉ có thể tải lên tối đa ${selectedImages} ảnh.`,
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            customClass: {
                popup: 'small-toast' // Thêm lớp CSS tùy chỉnh
            }
        });

        // Xóa các file đã chọn để người dùng chọn lại
        this.value = "";
        return;
    }

    // Ẩn thông báo lỗi nếu có ảnh được chọn
    document.getElementById('error-fileUpload').style.display = 'none';

    // Lấy container chứa danh sách ảnh
    const imageContainer = document.querySelector('.d-flex.flex-wrap');

    // Xóa các ảnh cũ
    imageContainer.innerHTML = '';

    // Reset trạng thái chọn ảnh
    selectedImages = 0;
    selectedImageIds = [];

    // Duyệt qua từng tệp và tạo preview
    for (let i = 0; i < files.length; i++) {
        const file = files[i];

        // Kiểm tra xem tệp có phải là ảnh không
        if (!file.type.startsWith('image/')) {
            continue;
        }

        // Tạo đối tượng FileReader để đọc ảnh
        const reader = new FileReader();

        reader.onload = function(e) {
            // Tạo phần tử div chứa ảnh
            const imageWrapper = document.createElement('div');
            imageWrapper.className = 'text-center me-3 mb-3';

            // Tạo phần tử ảnh
            const img = document.createElement('img');
            img.src = e.target.result;
            img.alt = file.name;
            img.className = 'image-hover';
            img.style.width = '100px';
            img.style.height = 'auto';

            // Gán sự kiện click để chọn/bỏ chọn ảnh
            // Sử dụng một ID tạm thời cho ảnh mới tải lên
            const temporaryId = `new-image-${i}`;
            img.setAttribute('data-id', temporaryId);
            img.onclick = function() {
                toggleBorder(this, temporaryId);
            };

            // Thêm ảnh vào wrapper
            imageWrapper.appendChild(img);

            // Thêm wrapper vào container
            imageContainer.appendChild(imageWrapper);
        };

        // Đọc tệp ảnh
        reader.readAsDataURL(file);
    }
});

// Function to show error message
function showErrorMessage(elementId, message) {
    const errorMessageElement = document.getElementById(elementId);
    if (errorMessageElement) {
        errorMessageElement.style.display = 'block'; // Show the error message
        errorMessageElement.textContent = message;  // Set the error message
    }
}

// Function to hide all error messages
function hideAllErrorMessages() {
    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(function(message) {
        message.style.display = 'none'; // Hide the error messages
    });
}

// Function to validate file upload (image type and size)
function validateFileUpload() {
    const fileUpload = document.getElementById("fileUpload");
    const files = fileUpload.files;

    // Nếu không có file nào được chọn, không cần kiểm tra
    if (files.length === 0) {
        return { isValid: true, message: "" };  // Trả về true nếu không có file
    }

    const maxFileSize = 2 * 1024 * 1024; // 2MB
    const allowedTypes = ["image/jpeg", "image/png", "image/gif", "image/jpg"]; // Các loại ảnh hợp lệ

    // Kiểm tra từng file được chọn
    for (let i = 0; i < files.length; i++) {
        const file = files[i];

        // Kiểm tra loại file (file phải là ảnh)
        if (!allowedTypes.includes(file.type)) {
            return { isValid: false, message: "Chỉ chấp nhận ảnh với định dạng JPG, JPEG, PNG hoặc GIF." };
        }

        // Kiểm tra kích thước file (phải nhỏ hơn hoặc bằng 2MB)
        if (file.size > maxFileSize) {
            return { isValid: false, message: "Kích thước ảnh không được vượt quá 2MB." };
        }
    }

    return { isValid: true, message: "" };  // Nếu tất cả đều hợp lệ
}

// Function to validate the form
function validateForm() {
    let isValid = true;

    // Clear any previous error messages
    hideAllErrorMessages();

    // Validate Sản Phẩm (idSP) - not required but ensure value is set correctly
    const idSP = document.getElementById("idSP").value.trim();
    if (idSP === "") {
        isValid = false;
        showErrorMessage("error-idSP", "Sản Phẩm không được để trống.");
    }

    // Validate Màu Sắc (idMauSac) - required field
    const idMauSac = document.getElementById("idMauSac").value;
    if (!idMauSac) {
        isValid = false;
        showErrorMessage("error-idMauSac", "Màu Sắc không được để trống.");
    }

    // Validate Kích Cỡ (idChatLieu) - required field
    const idChatLieu = document.getElementById("idChatLieu").value;
    if (!idChatLieu) {
        isValid = false;
        showErrorMessage("error-idChatLieu", "Kích Cỡ không được để trống.");
    }

    const maxGia = 999999999.99;
    const maxSoLuong = 9999;

    // Kiểm tra giá
    const gia = parseFloat(document.getElementById("gia").value);
    if (isNaN(gia) || gia < 0) {
        isValid = false;
        showErrorMessage("error-gia", "Giá phải lớn hơn hoặc bằng 0.");
    } else if (gia > maxGia) {
        isValid = false;
        showErrorMessage("error-gia", `Giá không được vượt quá ${maxGia}.`);
    }

    // Kiểm tra số lượng
    const soLuong = parseInt(document.getElementById("soLuong").value, 10);
    if (isNaN(soLuong) || soLuong < 1) {
        isValid = false;
        showErrorMessage("error-soLuong", "Số Lượng phải lớn hơn 0.");
    } else if (soLuong > maxSoLuong) {
        isValid = false;
        showErrorMessage("error-soLuong", `Số Lượng không được vượt quá ${maxSoLuong}.`);
    }

    // Validate the file upload only if files are selected
    const fileValidation = validateFileUpload();
    if (!fileValidation.isValid) {
        isValid = false;
        showErrorMessage("error-fileUpload", fileValidation.message);  // Hiển thị thông báo lỗi nếu không hợp lệ
    }

    return isValid;  // Return the result of validation
}

// Event listener for form submit
document.getElementById("updateForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Ngăn chặn form submit mặc định

    // Validate the form
    if (!validateForm()) {
        return;  // Stop form submission if validation fails
    }

    const id_sp = document.querySelector('input[name="id_sp"]').value;

    // Lấy productId từ input hidden
    const productId = document.getElementById("idspct").value;

    // Tạo đối tượng FormData từ form
    const formData = new FormData(this);

    // Thêm các ID ảnh đã chọn vào FormData
    selectedImageIds.forEach(id => {
        formData.append("imageIds", id);
    });

    // Gửi yêu cầu POST lên server
    fetch(`/admin/product-details/update/${productId}`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                window.location.href = `/admin/products/find_by_id/${id_sp}`;
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Đã xảy ra lỗi khi cập nhật sản phẩm.',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1000,
                timerProgressBar: true
            });
        });
});
