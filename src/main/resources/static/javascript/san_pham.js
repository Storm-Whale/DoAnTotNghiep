$(document).ready(function () {
    $('.js-select2').select2({
        placeholder: "Chọn một tùy chọn",
        allowClear: true
    });
});

let targetUrl = ''

function hienForm(url) {
    targetUrl = url;
    document.getElementById('formThemNhanh').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
}

function anForm() {
    // Ẩn form và overlay
    document.getElementById('formThemNhanh').style.display = 'none';
    document.getElementById('overlay').style.display = 'none';
    document.getElementById('errorTen').style.display = 'none';

    // Reset form
    const form = document.getElementById('formQuick');
    if (form) {
        form.reset();
    }
}

function themNhanh() {
    const tenInput = document.getElementById('ten');
    const errorTen = document.getElementById('errorTen');

    // Reset lỗi trước khi kiểm tra
    errorTen.style.display = 'none';

    const tenValue = tenInput.value;

    // Kiểm tra giá trị nhập
    if (!tenValue.trim()) {
        errorTen.textContent = 'Tên không được để trống!';
        errorTen.style.display = 'block';
        tenInput.focus();
        return false;
    }

    if (tenValue.startsWith(' ')) {
        errorTen.textContent = 'Tên không được bắt đầu bằng dấu cách!';
        errorTen.style.display = 'block';
        tenInput.focus();
        return false;
    }

    if (tenValue.length < 3) {
        errorTen.textContent = 'Tên phải có ít nhất 3 ký tự!';
        errorTen.style.display = 'block';
        tenInput.focus();
        return false;
    }

    const specialCharRegex = /[^a-zA-Z0-9\s]/;
    if (specialCharRegex.test(tenValue)) {
        errorTen.textContent = 'Tên không được chứa ký tự đặc biệt!';
        errorTen.style.display = 'block';
        tenInput.focus();
        return false;
    }

    // Nếu kiểm tra thành công, tiếp tục xử lý dữ liệu
    const dataForm = document.querySelector('#formQuick');
    if (!dataForm) {
        console.error('Form không tồn tại!');
        return;
    }

    const formData = new FormData(dataForm);

    fetch(targetUrl, {
        method: 'POST',
        body: formData,
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then((data) => {
            if (data.success) {
                // Xác định select2 cần cập nhật
                let selectElement;
                if (targetUrl.includes('thuong-hieu')) {
                    selectElement = $('#idThuongHieu');
                } else if (targetUrl.includes('chatlieu')) {
                    selectElement = $('#idChatLieu');
                } else if (targetUrl.includes('co-ao')) {
                    selectElement = $('#idCoAo');
                } else if (targetUrl.includes('tay-ao')) {
                    selectElement = $('#idTayAo');
                }

                // Thêm option mới vào select2
                if (selectElement) {
                    const newOption = new Option(data.ten, data.id, true, true);
                    selectElement.append(newOption).trigger('change');
                }

                // Đóng form và thông báo thành công
                anForm();
            } else {
                console.error(data.message || 'Thêm mới thất bại!');
            }
        })
        .catch((error) => {
            console.error('Lỗi:', error);
        });
}

