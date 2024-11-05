/// Tạo hoa đơn
// $(document).ready(function () {
//     // Biến để lưu số lượng hóa đơn đã tạo
//     let invoiceCount = $('#invoiceList ul li').length; // Lấy số lượng hóa đơn chờ hiện tại
//
//     $('#btnCreateInvoice').click(function (event) {
//         event.preventDefault();
//         // Kiểm tra số lượng hóa đơn đã tạo
//         if (invoiceCount < 5) {
//             $.post("/admin/taiquay/taohoadon", function (data) {
//                 // Kiểm tra xem hóa đơn mới có được tạo thành công không
//                 if (data) {
//                     // Tăng số lượng hóa đơn đã tạo
//                     invoiceCount++;
//                     // Thêm hóa đơn mới vào danh sách với CSS
//                     $('#invoiceList ul').append(
//                         '<li class="invoice-item">' +
//                         '<a href="/admin/taiquay/detail/' + data.id + '" class="invoice-link">Hóa đơn ' + data.id + '</a>' +
//                         '<span class="invoice-close-btn">&times;</span>' +
//                         '</li>'
//                     );
//                 } else {
//                     alert("Không thể tạo hóa đơn mới. Vui lòng kiểm tra lại!");
//                 }
//             });
//         } else {
//             alert("Bạn đã tạo tối đa 5 hóa đơn. Không thể tạo thêm.");
//         }
//     });
// });


// lọc sản phẩm

// JavaScript
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
                    alert("Không thể tạo hóa đơn mới. Vui lòng kiểm tra lại!");
                }
            });
        } else {
            alert("Bạn đã tạo tối đa 5 hóa đơn. Không thể tạo thêm.");
        }
    });

    // Xử lý khi bấm vào nút X để hủy hóa đơn
    $('#invoiceList').on('click', '.invoice-close-btn', function () {
        const $invoiceItem = $(this).closest('.invoice-item');
        const invoiceId = $invoiceItem.data('id');
        console.log("Invoice ID:", invoiceId); // Kiểm tra ID được lấy từ HTML
        if (invoiceId !== undefined) {
            $.ajax({
                url: '/admin/taiquay/huyhoadon/' + invoiceId,
                type: 'DELETE',
                success: function (response) {
                    // Xóa hóa đơn khỏi giao diện nếu hủy thành công
                    $invoiceItem.remove();
                    invoiceCount--;
                    alert(response); // Hiển thị thông báo từ server
                },
                error: function (xhr) {
                    alert(xhr.responseText || "Không thể hủy hóa đơn. Vui lòng kiểm tra lại!");
                }
            });
        } else {
            alert("ID hóa đơn không hợp lệ.");
        }
    });
});


