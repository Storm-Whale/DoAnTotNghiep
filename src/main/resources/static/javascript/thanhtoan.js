let timeoutId; // Biến lưu timeout để quản lý độ trễ

function tinhTienThua() {
    // Hủy bỏ timeout cũ (nếu có)
    if (timeoutId) {
        clearTimeout(timeoutId);
    }

    // Thiết lập timeout mới
    timeoutId = setTimeout(() => {
        // Lấy giá trị tongTien từ HTML (loại bỏ ký tự không phải số)
        var tongTien = parseFloat(document.getElementById("tongTien").innerText.replace(/\D/g, ''));

        // Lấy giá trị số tiền khách đưa từ input
        var soTienKhachDuaInput = document.getElementById("soTienKhachDua");
        var soTienKhachDua = soTienKhachDuaInput.value.trim();

        // Kiểm tra nếu ô nhập trống
        if (soTienKhachDua === "") {
            document.getElementById("tienThua").innerText = ""; // Xóa tiền thừa
            return; // Không xử lý gì thêm
        }

        // Chuyển giá trị sang kiểu số
        soTienKhachDua = parseFloat(soTienKhachDua);

        // Kiểm tra nếu giá trị không phải số hoặc nhỏ hơn 0
        if (isNaN(soTienKhachDua) || soTienKhachDua < 0) {
            // Hiển thị lỗi nếu giá trị không hợp lệ
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Số tiền không hợp lệ! Vui lòng nhập số dương.'
            });
            soTienKhachDuaInput.value = ''; // Xóa giá trị nhập không hợp lệ
            document.getElementById("tienThua").innerText = ""; // Xóa tiền thừa
            return;
        }

        // Kiểm tra nếu số tiền khách đưa nhỏ hơn tổng tiền
        if (soTienKhachDua < tongTien) {
            // Hiển thị lỗi nếu giá trị nhỏ hơn tổng tiền
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Số tiền khách đưa phải lớn hơn hoặc bằng tổng tiền.'
            });
            soTienKhachDuaInput.value = ''; // Xóa giá trị nhập không hợp lệ
            document.getElementById("tienThua").innerText = ""; // Xóa tiền thừa
            return;
        }

        // Tính tiền thừa
        var tienThua = soTienKhachDua - tongTien;

        // Hiển thị tiền thừa
        document.getElementById("tienThua").innerText = tienThua.toLocaleString('vi-VN') + " đ";
    }, 1000); // Độ trễ 1 giây
}
