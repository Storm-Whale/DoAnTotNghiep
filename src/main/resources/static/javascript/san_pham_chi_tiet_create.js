$(document).ready(function () {
    $('.js-select2').select2({
        placeholder: "Chọn một tùy chọn",
        allowClear: true
    });
});

function updateColorDetails() {
    const colorSelect = document.getElementById("idMauSac");
    const colorDetailsContainer = document.getElementById("colorDetailsContainer");
    colorDetailsContainer.innerHTML = "";

    Array.from(colorSelect.selectedOptions).forEach((option, index) => {
        const colorId = option.value;
        const colorName = option.text;

        const detailContainer = document.createElement("div");
        detailContainer.classList.add("border", "rounded", "p-3", "mb-3");

        detailContainer.innerHTML = `
                <div class="color-detail" data-index="${index}">
                    <h5 class="mb-3">Chi tiết cho ${colorName}</h5>
                    <input type="hidden" name="colorDetails[${index}].idMauSac" value="${colorId}">
        
                    <!-- Sử dụng Flexbox để chia bố cục -->
                    <div class="d-flex">
                        <!-- Phần ảnh (ảnh sản phẩm) ở bên trái -->
                        <div class="me-4" style="flex: 0 0 400px; text-align: center;">
                            <label class="form-label">Ảnh sản phẩm</label>
                            <input type="file"
                                   class="form-control images-input"
                                   name="colorDetails[${index}].images"
                                   multiple
                                   accept="image/*"
                                   required
                                   onchange="previewImage(event)">
                            <div class="text-danger warning-text images-warning mt-1" style="display: none;">
                                Vui lòng tải lên ít nhất một ảnh sản phẩm!
                            </div>
                            
                            <!-- Vùng hiển thị ảnh tải lên -->
                            <div id="imagePreviewContainer" style="margin-top: 10px;">
                                <img id="imagePreview" src="#" alt="Ảnh tải lên" style="width: 390px; height: 300px;display: none; object-fit: cover" />
                            </div>
                        </div>
    
                        <!-- Các thông tin còn lại ở bên phải -->
                        <div style="flex: 1;">
                            <!-- Giá (Price) -->
                            <div class="mb-3">
                                <label class="form-label">Giá</label>
                                <input type="number"
                                       class="form-control gia-input"
                                       name="colorDetails[${index}].gia"
                                       step="any"
                                       min="0"
                                       required
                                       placeholder="Nhập giá sản phẩm"
                                       oninput="removeWarning(this)">
                                <div class="text-danger warning-text gia-warning mt-1" style="display: none;">
                                    Vui lòng nhập giá hợp lệ!
                                </div>
                            </div>
                        
                            <!-- Kích thước (Size) -->
                            <div class="mb-3">
                                <label class="form-label">Kích thước</label>
                                <div class="d-flex align-items-center">
                                    <select class="form-control js-select2 kichCo-select"
                                            name="colorDetails[${index}].idKichCos"
                                            multiple required
                                            onchange="removeWarning(this)">
                                        ${kichCoList.map(size => `
                                            <option value="${size.id}">${size.tenKichCo}</option>
                                        `).join('')}
                                    </select>
                                    <button type="button" class="btn btn-primary btn-btnQuickAddKichCo ms-2" onclick="hienForm('/admin/kichco/quick-add')">Thêm</button>
                                </div>
                                <div class="text-danger warning-text kichCo-warning mt-2" style="display: none;">
                                    Vui lòng chọn ít nhất một kích thước!
                                </div>
                            </div>     
                
                            <!-- Số lượng (Quantity) -->
                            <div class="mb-3">
                                <label class="form-label">Số lượng</label>
                                <input type="number"
                                       class="form-control soLuong-input"
                                       name="colorDetails[${index}].soLuong"
                                       min="1"
                                       required
                                       placeholder="Nhập số lượng"
                                       oninput="removeWarning(this)">
                                <div class="text-danger warning-text soLuong-warning mt-1" style="display: none;">
                                    Vui lòng nhập số lượng ít nhất là 1!
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;

        colorDetailsContainer.appendChild(detailContainer);

        // Khởi tạo Select2 cho select mới thêm vào
        $(detailContainer).find('.js-select2').select2({
            placeholder: "Chọn kích thước",
            allowClear: true
        });
    });
}

// Function to validate the entire form
function validateForm() {
    let isValid = true;

    // Validate main color selection
    const colorSelect = document.getElementById("idMauSac");
    const colorWarning = document.getElementById("colorWarning");

    if (colorSelect.selectedOptions.length === 0) {
        colorWarning.style.display = "block";
        isValid = false;
    } else {
        colorWarning.style.display = "none";
    }

    // Validate each color detail section
    const colorDetails = document.querySelectorAll('.color-detail');
    colorDetails.forEach(function (detail) {
        // Validate Giá (Price)
        const giaInput = detail.querySelector('.gia-input');
        const giaWarning = detail.querySelector('.gia-warning');
        if (!giaInput.value || giaInput.value < 0) {
            giaWarning.style.display = "block";
            giaWarning.textContent = "Giá phải lớn hơn 0!";
            isValid = false;
        } else if (giaInput.value.startsWith(" ")) {
            giaWarning.style.display = "block";
            giaWarning.textContent = "Giá không được bắt đầu bằng dấu cách!";
            isValid = false;
        } else {
            giaWarning.style.display = "none";
        }

        // Validate Kích thước (Size)
        const kichCoSelect = detail.querySelector('.kichCo-select');
        const kichCoWarning = detail.querySelector('.kichCo-warning');
        if (kichCoSelect.selectedOptions.length === 0) {
            kichCoWarning.style.display = "block";
            isValid = false;
        } else {
            kichCoWarning.style.display = "none";
        }

        // Validate Số lượng (Quantity)
        const soLuongInput = detail.querySelector('.soLuong-input');
        const soLuongWarning = detail.querySelector('.soLuong-warning');
        if (!soLuongInput.value || soLuongInput.value < 1) {
            soLuongWarning.style.display = "block";
            soLuongWarning.textContent = "Số lượng phải lớn hơn hoặc bằng 1!";
            isValid = false;
        } else if (soLuongInput.value.startsWith(" ")) {
            soLuongWarning.style.display = "block";
            soLuongWarning.textContent = "Số lượng không được bắt đầu bằng dấu cách!";
            isValid = false;
        } else {
            soLuongWarning.style.display = "none";
        }

        // Validate Ảnh sản phẩm (Images)
        const imagesInput = detail.querySelector('.images-input');
        const imagesWarning = detail.querySelector('.images-warning');
        if (!imagesInput.files || imagesInput.files.length === 0) {
            imagesWarning.style.display = "block";
            isValid = false;
        } else {
            imagesWarning.style.display = "none";
        }
    });

    return isValid;
}

// Real-time validation for main color selection
document.getElementById("idMauSac").addEventListener("change", function () {
    const colorSelect = document.getElementById("idMauSac");
    const colorWarning = document.getElementById("colorWarning");
    if (colorSelect.selectedOptions.length > 0) {
        colorWarning.style.display = "none";
    }
});

// Real-time validation for dynamically added color detail sections
document.addEventListener('input', function (e) {
    // Handle Giá (Price) inputs
    if (e.target.classList.contains('gia-input')) {
        const giaWarning = e.target.parentElement.querySelector('.gia-warning');
        if (e.target.value && e.target.value >= 0) {
            giaWarning.style.display = "none";
        }
    }

    // Handle Số lượng (Quantity) inputs
    if (e.target.classList.contains('soLuong-input')) {
        const soLuongWarning = e.target.parentElement.querySelector('.soLuong-warning');
        if (e.target.value && e.target.value >= 1) {
            soLuongWarning.style.display = "none";
        }
    }
});

document.addEventListener('change', function (e) {
    // Handle Kích thước (Size) selects
    if (e.target.classList.contains('kichCo-select')) {
        const kichCoWarning = e.target.parentElement.querySelector('.kichCo-warning');
        if (e.target.selectedOptions.length > 0) {
            kichCoWarning.style.display = "none";
        }
    }

    // Handle Ảnh sản phẩm (Images) inputs
    if (e.target.classList.contains('images-input')) {
        const imagesWarning = e.target.parentElement.querySelector('.images-warning');
        if (e.target.files && e.target.files.length > 0) {
            imagesWarning.style.display = "none";
        }
    }
});

function removeWarning(element) {
    const parent = element.closest('.mb-3'); // Tìm phần tử cha chứa input
    const warning = parent.querySelector('.warning-text'); // Tìm phần tử thông báo
    if (warning) {
        warning.style.display = "none"; // Chỉ ẩn đi thay vì xóa
    }
}

document.getElementById('form-spct').addEventListener('submit', function(e) {
    e.preventDefault(); // Ngăn form submit mặc định

    if (validateForm()) {
        // Lấy id từ input hidden
        const productId = document.querySelector('input[name="idSP"]').value;

        // Submit form bằng AJAX để có thể xử lý chuyển hướng sau khi submit
        fetch(this.action, {
            method: 'POST',
            body: new FormData(this)
        })
            .then(response => {
                if (response.ok) {
                    // Nếu submit thành công, chuyển hướng đến trang mới
                    window.location.href = `/admin/products/find_by_id/${productId}`;
                }
            })
            .catch(error => {
                console.error(error);
                window.location.href = `admin/product-details/create/${productId}`;
            });
    }
});

function previewImage(event) {
    // Lấy phần tử img để hiển thị ảnh tải lên
    var imagePreview = document.getElementById('imagePreview');

    // Kiểm tra nếu có ảnh được chọn
    if (event.target.files && event.target.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            // Cập nhật thuộc tính src của img với ảnh vừa tải lên
            imagePreview.src = e.target.result;
            imagePreview.style.display = "block";  // Hiển thị ảnh preview
        }

        // Đọc ảnh dưới dạng URL
        reader.readAsDataURL(event.target.files[0]);
    }
}

