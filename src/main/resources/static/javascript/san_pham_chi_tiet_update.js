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

// Lắng nghe sự kiện thay đổi file
document.getElementById("fileUpload").addEventListener("change", function () {
    const files = this.files;

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

    // Validate Số Lượng (soLuong) - required and minimum value
    const soLuong = parseInt(document.getElementById("soLuong").value, 10);
    if (isNaN(soLuong) || soLuong < 1) {
        isValid = false;
        showErrorMessage("error-soLuong", "Số Lượng phải lớn hơn 0.");
    }

    // Validate Giá (gia) - required and minimum value
    const gia = parseFloat(document.getElementById("gia").value);
    if (isNaN(gia) || gia < 0) {
        isValid = false;
        showErrorMessage("error-gia", "Giá phải lớn hơn hoặc bằng 0.");
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
