function tinhTienThua() {
    // Lấy giá trị tongTien từ HTML

    var tongTien = parseFloat(document.getElementById("tongTien").innerText.replace(/\D/g, ''));

    // Lấy giá trị số tiền khách đưa
    var soTienKhachDua = parseFloat(document.getElementById("soTienKhachDua").value).toFixed(2);

    if (!isNaN(soTienKhachDua) && !isNaN(tongTien)) {
        var tienThua = soTienKhachDua - tongTien;

        // Hiển thị tiền thừa
        document.getElementById("tienThua").innerText = tienThua.toLocaleString('vi-VN') + " đ";
    }
}