/// Tạo hoa đơn
$(document).ready(function () {
    let invoiceCount = $('#invoiceList ul li').length;

    $('#btnCreateInvoice').click(function (event) {
        event.preventDefault();
        if (invoiceCount < 5) {
            $.post("/admin/taiquay/taohoadon", function (data) {
                console.log("Tạo hóa đơn với ID:", data.id);
                if (data && data.id) {
                    invoiceCount++;
                    $('#invoiceList ul').append(
                        '<li class="invoice-item" data-id="' + data.id + '">' +
                        '<a href="/admin/taiquay/detail/' + data.id + '" class="invoice-link">Hóa đơn ' + data.id + '</a>' +
                        '<span class="invoice-close-btn">&times;</span>' +
                        '</li>'
                    );
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: 'Không thể tạo hóa đơn mới. Vui lòng kiểm tra lại!'
                    });
                }
            }).fail(function (xhr) {
                if (xhr.status === 403) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Cảnh báo',
                        text: xhr.responseText
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: 'Có lỗi xảy ra, vui lòng thử lại!'
                    });
                }
            });
        } else {
            Swal.fire({
                icon: 'warning',
                title: 'Giới Hạn Hóa Đơn',
                text: 'Bạn chỉ được tạo tối đa 5 hóa đơn. Vui lòng hoàn tất hoặc hủy bớt hóa đơn!',
                confirmButtonText: 'Đã hiểu',
                confirmButtonColor: '#3085d6'
            });
        }
    });

    $('#invoiceList').on('click', '.invoice-close-btn', function () {
        const $invoiceItem = $(this).closest('.invoice-item');
        const invoiceId = $invoiceItem.data('id');

        if (invoiceId) {
            Swal.fire({
                title: 'Xác Nhận Hủy Hóa Đơn',
                text: 'Bạn có chắc chắn muốn hủy hóa đơn này?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Có, hủy ngay!',
                cancelButtonText: 'Không, quay lại'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: '/admin/taiquay/huyhoadon/' + invoiceId,
                        type: 'POST',
                        success: function () {
                            Swal.fire({
                                title: 'Đã Hủy!',
                                text: 'Hóa đơn đã được hủy thành công.',
                                icon: 'success',
                                confirmButtonText: 'OK'
                            }).then(() => {
                                $invoiceItem.remove();
                                // window.location.href = "/admin/taiquay";
                            });
                        },
                        error: function (xhr) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: xhr.responseText || "Không thể hủy hóa đơn. Vui lòng kiểm tra lại!"
                            });
                        }
                    });
                }
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'ID hóa đơn không hợp lệ.'
            });
        }
    });
});


// lọc sản phẩm

// JavaScript
// $(document).ready(function () {
//     let invoiceCount = $('#invoiceList ul li').length;
//
//     $('#btnCreateInvoice').click(function (event) {
//         event.preventDefault();
//         if (invoiceCount < 5) {
//             $.post("/admin/taiquay/taohoadon", function (data) {
//                 console.log("Tạo hóa đơn với ID:", data.id);
//                 if (data && data.id) {
//                     invoiceCount++;
//                     $('#invoiceList ul').append(
//                         '<li class="invoice-item" data-id="' + data.id + '">' +
//                         '<a href="/admin/taiquay/detail/' + data.id + '" class="invoice-link">Hóa đơn ' + data.id + '</a>' +
//                         '<span class="invoice-close-btn">&times;</span>' +
//                         '</li>'
//                     );
//                 } else {
//                     alert("Không thể tạo hóa đơn mới. Vui lòng kiểm tra lại!");
//                 }
//             }).fail(function (xhr) {
//                 if (xhr.status === 403) { // Nhân viên chưa đăng nhập
//                     alert(xhr.responseText); // Hiển thị thông báo lỗi từ server
//                 } else {
//                     alert("Có lỗi xảy ra, vui lòng thử lại!");
//                 }
//             });
//         } else {
//             alert("Bạn đã tạo tối đa 5 hóa đơn. Không thể tạo thêm.");
//         }
//     });
//
//     $('#invoiceList').on('click', '.invoice-close-btn', function () {
//         const $invoiceItem = $(this).closest('.invoice-item');
//         const invoiceId = $invoiceItem.data('id');
//         if (invoiceId !== undefined) {
//             $.ajax({
//                 url: '/admin/taiquay/huyhoadon/' + invoiceId,
//                 type: 'POST',
//                 // data: { _method: 'DELETE' },
//                 success: function () {
//                     alert("Hóa đơn đã được hủy thành công.");
//                     // Chuyển hướng về trang admin/taiquay
//                     window.location.href = "/admin/taiquay";
//                 },
//                 error: function (xhr) {
//                     alert(xhr.responseText || "Không thể hủy hóa đơn. Vui lòng kiểm tra lại!");
//                 }
//             });
//         } else {
//             alert("ID hóa đơn không hợp lệ.");
//         }
//     });
// });


