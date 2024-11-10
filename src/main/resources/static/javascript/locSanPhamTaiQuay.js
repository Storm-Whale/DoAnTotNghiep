// function submitForm() {
//     document.getElementById("filterForm").submit();
// }
//
// let debounceTimer;
//
// function submitForm() {
//     clearTimeout(debounceTimer);
//     debounceTimer = setTimeout(function () {
//         document.getElementById("filterForm").submit();
//     }, 500);  // Trì hoãn 500ms trước khi submit form
// }
//
// let timeout = null;
//
// function submitAjaxForm() {
//     clearTimeout(timeout);  // Xóa timeout nếu người dùng vẫn đang nhập
//
//     timeout = setTimeout(() => {  // Đợi 300ms trước khi gửi yêu cầu
//         const form = document.getElementById('filterForm');
//         const formData = new FormData(form);
//         const params = new URLSearchParams(formData);
//
//         fetch(`/admin/taiquay/sanpham?${params.toString()}`, {
//             method: 'GET',
//         })
//             .then(response => response.text())
//             .then(html => {
//                 // Cập nhật lại phần table sản phẩm
//                 document.getElementById('sanphamTable').innerHTML = html;
//             })
//             .catch(error => console.error('Error:', error));
//     }, 300);  // Chỉ gửi yêu cầu sau khi người dùng ngừng nhập trong 300ms
// }
//
// function changeItemsPerPage2() {
//     const selectedSize = document.getElementById("itemsPerPage2").value;
//     const currentPage = 0;
//     const url = `/admin/taiquay/sanpham?page=${currentPage}&size=${selectedSize}#sanpham`;
//     window.location.href = url;
// }