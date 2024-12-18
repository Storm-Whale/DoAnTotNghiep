document.addEventListener("DOMContentLoaded", function () {
    const productCheckboxes = document.querySelectorAll(".form-check-input:not(#select-all)");
    const loadingOverlay = document.getElementById("loading-overlay");
    const loadingSpinner = document.getElementById("loading-spinner");

    const selectAllCheckbox = document.getElementById("select-all");

    // Nếu addedProductId không null, đánh dấu checkbox tương ứng
    if (addedProductId) {
        const checkboxes = document.querySelectorAll('.product-checkbox');
        checkboxes.forEach(checkbox => {
            const productId = checkbox.getAttribute('data-product-id');
            if (productId === addedProductId.toString()) {
                checkbox.checked = true; // Đánh dấu checkbox
            }
        });

        updateTotalAmountAndCount()

    }

    document.querySelectorAll('.cart-item').forEach(item => {
        const btnMinus = item.querySelector('.btn-minus');
        const btnPlus = item.querySelector('.btn-plus');
        const quantityInput = item.querySelector('.quantity-input');
        const totalPriceElement = item.querySelector('.cart-item-total-price');
        const basePriceElement = item.querySelector('.cart-item-price');
        const checkbox = item.querySelector('.form-check-input');
        const idspghElement = item.querySelector('.idspgh');

        if (!idspghElement) return;
        const idspgh = idspghElement.value;

        const basePrice = parseFloat(basePriceElement?.value || '0');

        btnMinus.addEventListener('click', async () => {
            const currentQuantity = parseInt(quantityInput.value || '0');
            if (currentQuantity > 1) {
                loadingOverlay.style.display = 'block'; // Show overlay
                loadingSpinner.style.display = 'block'; // Show spinner
                try {
                    const response = await fetch(`/san-pham-gio-hang/minus-spgh/${idspgh}`, {
                        method: 'POST'
                    });

                    if (response.ok) {
                        setTimeout(() => {
                            quantityInput.value = String(currentQuantity - 1);
                            updatePrice(currentQuantity - 1, totalPriceElement, basePrice);
                            if (checkbox && checkbox.checked) {
                                updateTotalAmountAndCount();
                            }
                            loadingOverlay.style.display = 'none'; // Hide overlay
                            loadingSpinner.style.display = 'none'; // Hide spinner
                        }, 500);
                    } else {
                        loadingOverlay.style.display = 'none';
                        loadingSpinner.style.display = 'none';
                    }
                } catch (error) {
                    console.error('Error:', error);
                    loadingOverlay.style.display = 'none';
                    loadingSpinner.style.display = 'none';
                }
            }
        });

        btnPlus.addEventListener('click', async () => {
            loadingOverlay.style.display = 'block'; // Show overlay
            loadingSpinner.style.display = 'block'; // Show spinner
            try {
                const response = await fetch(`/san-pham-gio-hang/plus-spgh/${idspgh}`, {
                    method: 'POST'
                });

                if (response.ok) {
                    const currentQuantity = parseInt(quantityInput.value || '0');
                    setTimeout(() => {
                        quantityInput.value = String(currentQuantity + 1);
                        updatePrice(currentQuantity + 1, totalPriceElement, basePrice);
                        if (checkbox && checkbox.checked) {
                            updateTotalAmountAndCount();
                        }
                        loadingOverlay.style.display = 'none'; // Hide overlay
                        loadingSpinner.style.display = 'none'; // Hide spinner
                    }, 500);
                } else {
                    loadingOverlay.style.display = 'none';
                    loadingSpinner.style.display = 'none';
                }
            } catch (error) {
                console.error('Error:', error);
                loadingOverlay.style.display = 'none';
                loadingSpinner.style.display = 'none';
            }
        });

        quantityInput.addEventListener('change', async () => {
            let newQuantity = parseInt(quantityInput.value || '0');
            if (newQuantity < 1) {
                newQuantity = 1;
                quantityInput.value = String(newQuantity);
            }

            // Show loading spinner
            loadingOverlay.style.display = 'block'; // Show overlay
            loadingSpinner.style.display = 'block'; // Show spinner

            // Send updated quantity to the server
            try {
                const response = await fetch(`/san-pham-gio-hang/update-number-of-product/${idspgh}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ quantity: newQuantity }) // Send new quantity as JSON
                });

                if (response.ok) {
                    updatePrice(newQuantity, totalPriceElement, basePrice);
                    if (checkbox && checkbox.checked) {
                        updateTotalAmountAndCount();
                    }
                }
            } catch (error) {
                console.error('Error:', error);
            } finally {
                // Hide loading spinner after 1 second
                setTimeout(() => {
                    loadingOverlay.style.display = 'none'; // Hide overlay
                    loadingSpinner.style.display = 'none'; // Hide spinner
                }, 500);
            }
        });
    });

    document.addEventListener('click', async function(event) {
        const deleteButton = event.target.closest('.text-danger');

        if (deleteButton) {
            event.preventDefault();

            // Sử dụng SweetAlert để xác nhận
            const result = await Swal.fire({
                title: 'Xác nhận xóa sản phẩm',
                text: 'Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Có, xóa ngay!',
                cancelButtonText: 'Hủy bỏ'
            });

            // Kiểm tra xác nhận
            if (result.isConfirmed) {
                // Lấy ID sản phẩm
                const idspgh = deleteButton.getAttribute('href').split('/').pop();
                const cartItem = deleteButton.closest('.cart-item');
                const brandCard = cartItem.closest('.card');

                // Hiển thị loading
                const loadingOverlay = cartItem.querySelector('.loading-overlay');
                const loadingSpinner = cartItem.querySelector('.loading-spinner');
                loadingOverlay.style.display = 'block';
                loadingSpinner.style.display = 'block';

                try {
                    const response = await fetch(`/san-pham-gio-hang/delete-spgh/${idspgh}`, {
                        method: 'POST'
                    });

                    if (response.ok) {
                        setTimeout(() => {
                            // Xóa phần tử cart-item khỏi DOM
                            cartItem.remove();

                            // Kiểm tra xem thương hiệu còn sản phẩm không
                            const remainingItemsInBrand = brandCard.querySelectorAll('.cart-item');

                            if (remainingItemsInBrand.length === 0) {
                                // Nếu không còn sản phẩm, xóa luôn thẻ thương hiệu
                                brandCard.remove();
                            }

                            // Cập nhật lại tổng số lượng và tổng tiền
                            updateTotalAmountAndCount();

                            // Ẩn loading
                            loadingOverlay.style.display = 'none';
                            loadingSpinner.style.display = 'none';

                            // Thông báo xóa thành công
                            Swal.fire({
                                icon: 'success',
                                title: 'Đã xóa!',
                                text: 'Sản phẩm đã được xóa khỏi giỏ hàng.',
                                timer: 2000,
                                showConfirmButton: false
                            });
                        }, 500);
                    } else {
                        // Xử lý lỗi
                        loadingOverlay.style.display = 'none';
                        loadingSpinner.style.display = 'none';

                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: 'Không thể xóa sản phẩm. Vui lòng thử lại.'
                        });
                    }
                } catch (error) {
                    console.error('Lỗi:', error);

                    loadingOverlay.style.display = 'none';
                    loadingSpinner.style.display = 'none';

                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: 'Đã xảy ra lỗi. Vui lòng thử lại.'
                    });
                }
            }
        }
    });

    // Select all checkbox functionality
    selectAllCheckbox.addEventListener("change", function () {
        const isChecked = selectAllCheckbox.checked;
        productCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });

        // Update total amount and count if the select all checkbox is checked
        if (isChecked) {
            updateTotalAmountAndCount();
        } else {
            resetTotalAmountAndCount();
        }
    });

    // Add event listeners for each brand's select all checkbox
    document.querySelectorAll('.select-all').forEach(selectAllBrandCheckbox => {
        selectAllBrandCheckbox.addEventListener("change", function () {
            const isChecked = this.checked;
            const brandCartItems = this.closest('.card').querySelectorAll('.product-checkbox');

            brandCartItems.forEach(checkbox => {
                checkbox.checked = isChecked;
            });

            // Update total amount and count if the select all checkbox for the brand is checked
            if (isChecked) {
                updateTotalAmountAndCount();
            } else {
                resetTotalAmountAndCount();
            }
        });
    });

    // Event listener for individual product checkboxes
    productCheckboxes.forEach(checkbox => {
        checkbox.addEventListener("change", updateTotalAmountAndCount);
    });

    function resetTotalAmountAndCount() {
        const selectedCountElement = document.getElementById("selected-count");
        const totalAmountElement = document.getElementById("total-product");

        if (selectedCountElement) {
            selectedCountElement.textContent = "0 sản phẩm"; // Reset to 0 sản phẩm
        }
        if (totalAmountElement) {
            totalAmountElement.textContent = "0 đ"; // Reset total to 0
        }
    }

    function updatePrice(quantity, totalPriceElement, basePrice) {
        if (!totalPriceElement) return;
        const newTotalPrice = basePrice * quantity;
        totalPriceElement.textContent = new Intl.NumberFormat('vi-VN', {
            maximumFractionDigits: 0
        }).format(newTotalPrice) + ' đ';
    }

    function updateTotalAmountAndCount() {
        let totalAmount = 0;
        let selectedCount = 0;

        productCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const cartItem = checkbox.closest(".cart-item");
                if (!cartItem) return;

                const quantityInput = cartItem.querySelector(".quantity-input");
                const basePriceInput = cartItem.querySelector(".cart-item-price");

                if (!quantityInput || !basePriceInput) return;

                const quantity = parseInt(quantityInput.value || '0');
                const basePrice = parseFloat(basePriceInput.value || '0');
                const itemTotal = quantity * basePrice;

                totalAmount += itemTotal;
                selectedCount++;
            }
        });

        const selectedCountElement = document.getElementById("selected-count");
        const totalAmountElement = document.getElementById("total-product");

        if (selectedCountElement) {
            selectedCountElement.textContent = `${selectedCount}`; // Cập nhật số lượng sản phẩm
        }

        if (totalAmountElement) {
            totalAmountElement.textContent = new Intl.NumberFormat('vi-VN', {
                maximumFractionDigits: 0
            }).format(totalAmount) + ' đ'; // Cập nhật tổng tiền
        }
    }
});

// Add new checkout button functionality
const checkoutButton = document.querySelector('.btn-buy');
checkoutButton.addEventListener('click', function() {
    // Get all checked product checkboxes
    const selectedProducts = document.querySelectorAll('.product-checkbox:checked');

    if (selectedProducts.length === 0) {
        alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán');
        return;
    }

    // Tạo form ẩn
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/client/check-out';

    // Thêm input cho mỗi sản phẩm được chọn
    selectedProducts.forEach(checkbox => {
        const cartItem = checkbox.closest('.cart-item');
        const idspghInput = cartItem.querySelector('.idspgh');

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'productIds';  // Spring sẽ tự động convert thành List
        input.value = idspghInput.value;

        form.appendChild(input);
    });

    document.body.appendChild(form);
    form.submit();
});
