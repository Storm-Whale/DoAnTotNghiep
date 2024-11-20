// What For : Hiện model phiếu giảm giá
// * Hiển thị form phiếu giảm giá
document.getElementById('openModalButton').addEventListener('click', function () {
    const voucherList = document.getElementById('voucherList');
    voucherList.innerHTML = '';
    const ngayHomNay = new Date();
    var tongTienBanDau = tongTien

    listPhieuGiamGia.forEach((voucher) => {
        const ngayKetThuc = new Date(voucher.ngayKetThuc);
        const ngayConLai = Math.ceil((ngayKetThuc - ngayHomNay) / (1000 * 60 * 60 * 24));
        const voucherContainer = document.createElement('div');
        voucherContainer.className = 'voucher-container';

        const isConditionMet = tongTienBanDau >= voucher.dieuKien;

        voucherContainer.innerHTML = `
            <div class="p-2">
                <img src="/37.jpg" alt="Voucher Image" width="80px">
            </div>
            <div class="voucher-details">
                <div>Giảm tới đa ${new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(voucher.giaTriMax)}</div>
                <div>Đơn Tối Thiểu ${new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(voucher.dieuKien)}</div>
                <div>Sắp hết hạn: Còn ${ngayConLai} ngày</div>
            </div>
            <div class="radio-container p-2">
                <input type="radio" name="voucherSelection" value="${voucher.maPhieuGiamGia}" class="custom-radio" ${isConditionMet ? '' : 'disabled'}>
            </div>
        `;

        // * Nếu không thỏa mãn điều kiện, làm mờ voucher
        if (!isConditionMet) {
            voucherContainer.style.opacity = '0.5';
            voucherContainer.style.pointerEvents = 'none';
        }

        voucherList.appendChild(voucherContainer);
    });

    $('#exampleModal').modal('show');
});

let selectedVoucherMa = null
// * Lấy mã voucher được chọn
document.getElementById('voucherList').addEventListener('change', function (event) {
    if (event.target.name === 'voucherSelection') {
        selectedVoucherMa = event.target.value
    }
})

// * Hiện voucher được chọn
var tongTienSauKhiTruMa
document.getElementById('select-voucher').addEventListener('click', function () {
    if (selectedVoucherMa !== null) {
        const  selectedVoucher = listPhieuGiamGia.find(voucher => voucher.maPhieuGiamGia === selectedVoucherMa)
        const nameVoucher = document.querySelector('.nameVoucherCode');
        document.getElementById('voucherCode').value = `${selectedVoucher.maPhieuGiamGia}`
        nameVoucher.innerHTML = `Tên ưu đãi : ${selectedVoucher.tenPhieuGiamGia}`

        let kieuGiamGia = selectedVoucher.kieuGiamGia
        tongTienSauKhiTruMa = kieuGiamGia === 1 ? tongTien - (tongTien * (selectedVoucher.giaTriGiam / 100)) - tienShip : tongTien - selectedVoucher.giaTriGiam - tienShip

        document.getElementById('tongTien').innerHTML = tongTienSauKhiTruMa.toLocaleString('vi-VN') + " đ"
        document.getElementById('tienGiamTuMa').innerHTML = (tongTien * (selectedVoucher.giaTriGiam / 100)).toLocaleString('vi-VN') + " đ"
    }
    $('#exampleModal').modal('hide');
})

// What For : Hiện model địa chỉ
// * Hiển thị địa chỉ ban đầu
const thongTinDiaChi = document.getElementById('thongTinDiaChi');
let selectedDiaChiID = null;
if (listDiaChi.length > 0) {
    const { id, tenKhachHang, soDienThoai, diaChiChiTiet } = listDiaChi[0];
    selectedDiaChiID = id
    thongTinDiaChi.innerHTML = `
        <input id="diaChi" type="hidden" value="${id}">
        Họ và tên: ${tenKhachHang}<br>
        Số điện thoại: ${soDienThoai}<br>
        Địa chỉ chi tiết: ${diaChiChiTiet}
    `;
} else {
    thongTinDiaChi.innerHTML = 'Bạn chưa có tạo thông tin địa chỉ, vui lòng tạo';
}

// * Hiển thị form chọn địa chỉ
document.getElementById('showFormButton').addEventListener('click', () => {
    document.getElementById('overlay').style.display = 'block';
    document.getElementById('addressContainer').style.display = 'block';
    const diaChiList = document.getElementById('diaChiList');

    if (diaChiList && listDiaChi) {
        diaChiList.innerHTML = listDiaChi.map((diaChi) => `
            <div class="address-item">
                <input type="radio" name="selectedAddress" value="${diaChi.id}" ${diaChi.id === parseInt(selectedDiaChiID) ? 'checked' : ''}>
                <div class="address-info">
                    <strong>${diaChi.tenKhachHang}</strong> ${diaChi.soDienThoai}<br>
                    ${diaChi.diaChiChiTiet}
                </div>
                <div class="address-update">
                    <button id="updateAddress" class="btn btn-warning update-btn" onclick="showUpdateForm(${diaChi.id})">Cập nhật</button>
                </div>
            </div>
        `).join('');
    }
});

// * Lấy mã id địa chỉ được chọn
document.getElementById('diaChiList').addEventListener('change', () => {
    // * Lấy tất cả các ô radio có tên là 'selectedAddress'
    const radioButtons = document.querySelectorAll('input[name="selectedAddress"]');

    // * Duyệt qua tất cả các ô radio để tìm ô đang được chọn
    radioButtons.forEach(radio => {
        if (radio.checked) {
            selectedDiaChiID = radio.value;
        }
    });
});

// * Hiển thị địa chỉ được chọn
document.querySelector('.confirm').addEventListener('click', () => {
    const selectedDiaChi = listDiaChi.find(diachi => diachi.id === parseInt(selectedDiaChiID));

    if (selectedDiaChi) {
        const { id, tenKhachHang, soDienThoai, diaChiChiTiet } = selectedDiaChi;
        thongTinDiaChi.innerHTML = `
            <input id="diaChi" type="hidden" name="selectedAddress" value="${id}">
            Họ và tên: ${tenKhachHang}<br>
            Số điện thoại: ${soDienThoai}<br>
            Địa chỉ chi tiết: ${diaChiChiTiet}
        `;
        document.getElementById('overlay').style.display = 'none';
        document.getElementById('addressContainer').style.display = 'none';
        document.getElementById('addAddressContainer').style.display = 'none';
    } else {
        console.error('Không tìm thấy địa chỉ với ID:', selectedDiaChiID);
    }
});

// * Đóng form khi bấm "Hủy" hoặc "Xác nhận"
document.querySelectorAll('.cancel, .confirm').forEach(button => {
    button.addEventListener('click', () => {
        document.getElementById('overlay').style.display = 'none';
        document.getElementById('addressContainer').style.display = 'none';
        document.getElementById('addAddressContainer').style.display = 'none';
    });
});

// * Hiển thị form thêm địa chỉ mới
document.getElementById('addAddressButton').addEventListener('click', function () {
    document.getElementById('addressContainer').style.display = 'none';
    document.getElementById('addAddressContainer').style.display = 'block';
});

// * Xử lý sự kiện quay lại từ form thêm địa chỉ
document.getElementById('backButton').addEventListener('click', function () {
    document.getElementById('addressContainer').style.display = 'block';
    document.getElementById('addAddressContainer').style.display = 'none';
});

// What For : Gửi dữ liệu về trang địa chỉ để thêm mới
async function submitForm() {
    const form = document.getElementById('addressForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/dia-chi/add-new-address', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            localStorage.setItem('shouldTriggerShow', 'true');
            window.location.reload();
        } else {
            console.log('Có lỗi xảy ra: ' + response.statusText);
        }
    } catch (error) {
        console.log('Có lỗi xảy ra: ' + error.message);
    }
}

// * Hàm hiển thị form cập nhật và lưu idDiaChi
function showUpdateForm(idDiaChi) {
    document.getElementById('addressContainer').style.display = 'none';
    document.getElementById('addAddressContainer').style.display = 'block';

    // Lưu idDiaChi vào data attribute của form
    const form = document.getElementById('addressForm');
    form.setAttribute('data-id-dia-chi', idDiaChi);

    // Đổi nút lưu thành nút cập nhật
    const saveButton = document.getElementById('saveAddressButton');
    saveButton.textContent = 'Cập nhật';
    saveButton.onclick = updateAddress;

    // Điền thông tin hiện tại vào form
    const diaChi = listDiaChi.find(d => d.id === idDiaChi);
    if (diaChi) {
        document.getElementById('name').value = diaChi.tenKhachHang;
        document.getElementById('phone').value = diaChi.soDienThoai;
        document.getElementById('city').value = diaChi.thanhPho;
        document.getElementById('district').value = diaChi.huyen;
        document.getElementById('ward').value = diaChi.xa;
        document.getElementById('detailAddress').value = diaChi.diaChiChiTiet;
    }
}

// * Hàm xử lý cập nhật địa chỉ
async function updateAddress() {
    const form = document.getElementById('addressForm');
    const formData = new FormData(form);
    const idDiaChi = form.getAttribute('data-id-dia-chi');

    try {
        const response = await fetch(`/dia-chi/update-address/${idDiaChi}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            localStorage.setItem('shouldTriggerShow', 'true');
            window.location.reload();
        } else {
            console.log('Có lỗi xảy ra: ' + response.statusText);
        }
    } catch (error) {
        console.log('Có lỗi xảy ra: ' + error.message);
    }
}

// Kiểm tra và trigger click khi trang load xong
document.addEventListener('DOMContentLoaded', function() {
    // Kiểm tra flag trong localStorage
    if (localStorage.getItem('shouldTriggerShow') === 'true') {
        // Xóa flag
        localStorage.removeItem('shouldTriggerShow');
        // Tự động click nút
        document.getElementById('showFormButton').click();
    }
});

// What For : Gửi Data Thanh Toán
function guiDataThanhToan() {
    const paymentMethods = document.getElementsByName('paymentMethod');
    let selectedMethod;
    const idDiaChi = document.getElementById('diaChi').value
    const maPGG = document.getElementById('voucherCode').value
    const ghiChu = document.getElementById('ghiChu').value

    // Duyệt qua từng nút radio để tìm nút nào được chọn
    for (let i = 0; i < paymentMethods.length; i++) {
        if (paymentMethods[i].checked) {
            selectedMethod = paymentMethods[i].value; // Lấy giá trị của nút được chọn
            break;
        }
    }

    const formData = new FormData();
    formData.append('listIDSPGH', JSON.stringify(listIDSPGH));
    formData.append('tongTien', tongTienSauKhiTruMa);
    formData.append('pttt', selectedMethod);
    formData.append('idDiaChi', idDiaChi);
    formData.append('maPGG', maPGG);
    formData.append('ghiChu', ghiChu)

    fetch('/client/thanh-toan', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(() => {
            // Lưu trạng thái vào localStorage
            localStorage.setItem('paymentSuccess', 'true');
            // Chuyển hướng sang trang chính
            window.location.href = 'http://localhost:8080/client';
        })
        .catch(error => console.log(error));
}


