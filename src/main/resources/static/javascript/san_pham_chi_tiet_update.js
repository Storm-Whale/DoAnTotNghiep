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

document.getElementById("updateForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Ngăn chặn form submit mặc định

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
                // Chuyển hướng đến trang mới sau khi cập nhật thành công
                window.location.href = "/admin"; // Thay đổi đường dẫn trang mới ở đây
            } else {
                // Xử lý phản hồi lỗi
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Đã xảy ra lỗi khi cập nhật ảnh.',
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true
                });
            }
        })
        .catch(error => {
            // Xử lý lỗi khi gửi yêu cầu
            console.error('Lỗi:', error);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Đã xảy ra lỗi khi cập nhật ảnh.',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1000,
                timerProgressBar: true
            });
        });
});