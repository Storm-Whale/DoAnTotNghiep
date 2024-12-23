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

function confirmDelete(element) {
    const id = element.getAttribute('data-id');

    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn có chắc chắn muốn xóa địa chỉ này không?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Có, xóa nó!',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            // Gọi API xóa
            fetch(`/dia-chi/delete-address/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
                .then(response => {
                    if(response.ok) {
                        showSuccessAlert()
                    } else {
                       showErrorAlert()
                    }
                    window.location.reload()
                })
                .catch(error => {
                    console.error('Error:', error);
                    showErrorAlert()
                });
        }
    });
}

function showAddAddressForm() {
    const formContainer = document.getElementById("addAddressContainer");
    const overlay = document.getElementById("overlay");

    formContainer.classList.add('show');
    overlay.style.display = "block";
}

function hideAddAddressForm() {
    const formContainer = document.getElementById("addAddressContainer");
    const overlay = document.getElementById("overlay");

    formContainer.classList.remove('show');
    overlay.style.display = "none";
}

function submitAddressForm() {
    if (!validateAddressForm()) {
        return; // Exit if validation fails
    }

    const form = document.getElementById('addressForm');
    const formData = new FormData(form);

    fetch('/dia-chi/add-new-address', {
        method: 'POST',
        body: formData,
    })
        .then(response => {
            if (response.ok) {
                console.log('Thêm địa chỉ thành công');
                window.location.reload();
            } else {
                console.error('Error:', response);
                showErrorAlert();
            }
        })
        .catch(error => {
            console.error("Đã xảy ra lỗi khi gửi dữ liệu: " + error);
            showErrorAlert();
        });
}

function showSuccessAlert() {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 1000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
    Toast.fire({
        icon: "success",
        title: "Xóa địa chỉ thành công"
    });
}

// Hàm hiển thị lỗi mua hàng thất bại
function showErrorAlert() {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 1500,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });

    Toast.fire({
        icon: "error",
        title: "Có lỗi khi xóa"
    });
}

function showUpdateAddressForm(element) {
    const formContainer = document.getElementById("addAddressContainer");
    const overlay = document.getElementById("overlay");

    formContainer.classList.add('show');
    overlay.style.display = "block";

    const id = element.getAttribute('data-id');
    let diaChi = null;

    for (let i = 0; i < diaChiList.length; i++) {
        if (diaChiList[i].id === parseInt(id)) {
            diaChi = diaChiList[i];
            break;
        }
    }

    const form = document.getElementById('addressForm');
    form.setAttribute('data-id-dia-chi', id);

    const saveButton = document.getElementById('saveAddressButton');
    saveButton.textContent = 'Cập nhật';
    saveButton.onclick = updateAddress;

    document.getElementById('title').innerText = 'Cập Nhật Địa Chỉ'

    if (diaChi) {
        document.getElementById('name').value = diaChi.tenKhachHang;
        document.getElementById('phone').value = diaChi.soDienThoai;
        document.getElementById('city').value = diaChi.thanhPho;
        document.getElementById('district').value = diaChi.huyen;
        document.getElementById('ward').value = diaChi.xa;
        document.getElementById('detailAddress').value = diaChi.diaChiChiTiet;
    }
}

async function updateAddress() {
    if (!validateAddressForm()) {
        return;
    }

    const form = document.getElementById('addressForm');
    const formData = new FormData(form);
    const idDiaChi = form.getAttribute('data-id-dia-chi');

    try {
        const response = await fetch(`/dia-chi/update-address/${idDiaChi}`, {
            method: 'POST',
            body: formData
        });
        if (response.ok) {
            window.location.reload();
        } else {
            console.log('Có lỗi xảy ra: ' + response.statusText);
        }
    } catch (error) {
        console.log('Có lỗi xảy ra: ' + error.message);
    }
}

function validateAddressForm() {
    let isValid = true;

    // Reset all error messages
    document.querySelectorAll('.text-danger').forEach(el => el.classList.add('d-none'));

    // Regex kiểm tra ký tự đặc biệt
    const specialCharRegex = /[!#$%^&*()+=\[\]{};':"\\|,<>/?]/;

    // Validate name: cannot start with a number, contain special characters, or start with a space
    const name = document.getElementById('name').value.trim();
    if (name === '' || /^\d/.test(name) || name.length > 20 || specialCharRegex.test(name) || name.startsWith(' ')) {
        document.getElementById('nameError').classList.remove('d-none');
        isValid = false;
    }

    // Validate phone number: should be a valid phone number with 10-11 digits
    const phone = document.getElementById('phone').value.trim();
    const phoneRegex = /^0[0-9]{9,10}$/; // Starts with '0' and followed by 9-10 digits
    if (!phoneRegex.test(phone)) {
        document.getElementById('phoneError').classList.remove('d-none');
        isValid = false;
    }

    // Validate city: cannot start with a number, contain special characters, or start with a space
    const city = document.getElementById('city').value.trim();
    if (city === '' || /^\d/.test(city) || city.length > 20 || specialCharRegex.test(city) || city.startsWith(' ')) {
        document.getElementById('cityError').classList.remove('d-none');
        isValid = false;
    }

    // Validate district: cannot start with a number, contain special characters, or start with a space
    const district = document.getElementById('district').value.trim();
    if (district === '' || /^\d/.test(district) || district.length > 20 || specialCharRegex.test(district) || district.startsWith(' ')) {
        document.getElementById('districtError').classList.remove('d-none');
        isValid = false;
    }

    // Validate ward: cannot start with a number, contain special characters, or start with a space
    const ward = document.getElementById('ward').value.trim();
    if (ward === '' || /^\d/.test(ward) || ward.length > 20 || specialCharRegex.test(ward) || ward.startsWith(' ')) {
        document.getElementById('wardError').classList.remove('d-none');
        isValid = false;
    }

    // Validate detail address: cannot contain special characters or start with a space, max length 200
    const detailAddress = document.getElementById('detailAddress').value.trim();
    if (detailAddress === '' || detailAddress.length > 200 || specialCharRegex.test(detailAddress) || detailAddress.startsWith(' ')) {
        document.getElementById('detailAddressError').classList.remove('d-none');
        isValid = false;
    }

    return isValid;
}


