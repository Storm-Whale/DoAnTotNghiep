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

    // Reset form
    const form = document.getElementById('formQuick');
    if (form) {
        form.reset();
    }
}

function themNhanh() {
    const dataForm = document.querySelector('#formQuick');
    if (!dataForm) {
        console.error('Form không tồn tại!');
        return;
    }

    const formData = new FormData(dataForm);

    fetch(targetUrl, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                // Xác định select2 cần cập nhật
                let selectElement;
                if (targetUrl.includes('mausac')) {
                    selectElement = $('#idMauSac');
                } else if (targetUrl.includes('kichco')) {
                    selectElement = $('#idKichCo');
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