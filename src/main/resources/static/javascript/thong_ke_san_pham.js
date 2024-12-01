// Hàm chuyển đổi mảng ngày tháng sang đối tượng Date
function arrayToDate(dateArray) {
    if (Array.isArray(dateArray) && dateArray.length >= 3) {
        return new Date(dateArray[0], dateArray[1] - 1, dateArray[2],
            dateArray[3] || 0, dateArray[4] || 0);
    }
    return null;
}

// Hàm tính tổng sản phẩm bán được theo SPCT
function tinhTongSanPhamBanDuoc(startDate = null, endDate = null, selectedProductIds = null) {
    console.log('Calculating product quantities:', {startDate, endDate, selectedProductIds});

    const productQuantities = {};
    const productNames = {};

    hoaDons.forEach(hoaDon => {
        const hoaDonDate = arrayToDate(hoaDon.ngayTao);

        // Kiểm tra điều kiện ngày
        const isDateInRange = !startDate || !endDate ||
            (hoaDonDate && hoaDonDate >= startDate && hoaDonDate <= endDate);

        if (isDateInRange) {
            const chiTietHoaDons = hoaDonChiTiets.filter(ct => ct.idHoaDon === hoaDon.id);
            chiTietHoaDons.forEach(chiTiet => {
                const sanPhamChiTiet = sanPhamChiTiets.find(spct => spct.id === chiTiet.idSpct);
                if (sanPhamChiTiet) {
                    const sanPham = sanPhams.find(sp => sp.tenSanPham === sanPhamChiTiet.tenSanPham);
                    if (sanPham) {
                        // Nếu không có sản phẩm được chọn hoặc sản phẩm nằm trong danh sách được chọn
                        if (!selectedProductIds || selectedProductIds.length === 0 ||
                            selectedProductIds.includes(sanPham.id.toString())) {
                            const productKey = sanPham.id;
                            productQuantities[productKey] = (productQuantities[productKey] || 0) + chiTiet.soLuong;
                            productNames[productKey] = sanPham.tenSanPham;
                        }
                    }
                }
            });
        }
    });

    console.log('Product Quantities:', productQuantities);
    return {productQuantities, productNames};
}

// Hàm tính tổng doanh thu cho một sản phẩm
function calculateTotalRevenue(productId, startDate = null, endDate = null) {
    let totalRevenue = 0;

    hoaDons.forEach(hoaDon => {
        const hoaDonDate = arrayToDate(hoaDon.ngayTao);

        // Kiểm tra điều kiện ngày
        const isDateInRange = !startDate || !endDate ||
            (hoaDonDate && hoaDonDate >= startDate && hoaDonDate <= endDate);

        if (isDateInRange) {
            const chiTietHoaDons = hoaDonChiTiets.filter(ct => ct.idHoaDon === hoaDon.id);
            chiTietHoaDons.forEach(chiTiet => {
                const sanPhamChiTiet = sanPhamChiTiets.find(spct => spct.id === chiTiet.idSpct);
                const sanPham = sanPhams.find(sp => sp.id.toString() === productId && sp.tenSanPham === sanPhamChiTiet.tenSanPham);

                if (sanPham) {
                    totalRevenue += chiTiet.soLuong * chiTiet.gia;
                }
            });
        }
    });

    return totalRevenue;
}

// Hàm hiển thị các sản phẩm bán chạy nhất
function displayTopSellingProducts(limit = 5, startDate = null, endDate = null, selectedProductIds = null) {
    const {productQuantities, productNames} = tinhTongSanPhamBanDuoc(startDate, endDate, selectedProductIds);

    console.log("LLL : ", tinhTongSanPhamBanDuoc(startDate, endDate, selectedProductIds))

    // Sắp xếp các sản phẩm theo số lượng bán giảm dần
    const sortedProducts = Object.keys(productQuantities)
        .map(key => ({
            id: key,
            name: productNames[key],
            quantity: productQuantities[key],
            totalRevenue: calculateTotalRevenue(key, startDate, endDate)
        }))
        .sort((a, b) => b.quantity - a.quantity)
        .slice(0, limit);

    // Lấy phần tử danh sách sản phẩm bán chạy
    const topSellingProductsList = document.getElementById('topSellingProducts');

    // Xóa các mục hiện tại
    topSellingProductsList.innerHTML = '';

    // Thêm các sản phẩm bán chạy vào danh sách
    sortedProducts.forEach(product => {
        const listItem = document.createElement('li');
        listItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');

        listItem.innerHTML = `
            <div>
                <strong>${product.name}</strong><br>
                <small class="text-muted">Tổng tiền: 
                    <span>${product.totalRevenue.toLocaleString()} VND</span>
                </small>
            </div>
            <span class="badge bg-success rounded-pill">Số lượng : ${product.quantity}</span>
        `;

        topSellingProductsList.appendChild(listItem);
    });
}

// Hàm cập nhật biểu đồ
function updateChart(startDate = null, endDate = null, selectedProductIds = null) {
    const {productQuantities, productNames} = tinhTongSanPhamBanDuoc(startDate, endDate, selectedProductIds);

    // Chuẩn bị dữ liệu cho biểu đồ
    const labels = Object.keys(productQuantities).map(key => productNames[key]);
    const data = Object.values(productQuantities);

    // Tạo hoặc cập nhật biểu đồ
    const ctx = document.getElementById('myChart').getContext('2d');
    if (window.myChart instanceof Chart) {
        window.myChart.destroy();
    }

    window.myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Số lượng sản phẩm bán ra',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.5)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Sản phẩm'
                    }
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: 'Thống kê số lượng sản phẩm bán ra'
                }
            }
        }
    });
}

// Hàm reset tất cả các input date ngoại trừ input được chỉ định
function resetOtherInputs(excludeInputs) {
    const inputs = ['startDate', 'endDate', 'monthInput', 'dayInput'];
    inputs.forEach(inputId => {
        if (!excludeInputs.includes(inputId)) {
            document.getElementById(inputId).value = '';
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    // Khởi tạo Select2
    $('#tenSanPham').select2({
        placeholder: "Chọn sản phẩm",
        allowClear: true,
    });

    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const monthInput = document.getElementById('monthInput');
    const dayInput = document.getElementById('dayInput');
    const productSelect = document.getElementById('tenSanPham');

    // Khởi tạo hiển thị dữ liệu năm hiện tại
    const currentYear = new Date().getFullYear();
    const startOfYear = new Date(currentYear, 0, 1, 0, 0, 0);
    const endOfYear = new Date(currentYear, 11, 31, 23, 59, 59);

    function initialUpdateChart() {
        console.log('Initial chart update');
        updateChart(startOfYear, endOfYear);
        displayTopSellingProducts(
            parseInt(document.getElementById('topProducts').value),
            startOfYear,
            endOfYear
        );
    }

    initialUpdateChart();

    // Xử lý chọn sản phẩm
    $('#tenSanPham').on('change', function () {
        // Lấy ID các sản phẩm đã chọn
        const selectedProductIds = $(this).val();
        console.log('Selected Product IDs:', selectedProductIds);

        // Lấy khoảng thời gian hiện tại
        const {startDate, endDate} = getCurrentDateRange();
        console.log('Current Date Range:', startDate, endDate);

        // Update chart ngay lập tức
        updateChart(startDate, endDate, selectedProductIds);
        displayTopSellingProducts(
            parseInt(document.getElementById('topProducts').value),
            startDate,
            endDate,
            selectedProductIds
        );
    });

    // Hàm helper để lấy các ngày hiện tại đang được chọn
    function getCurrentDateRange() {
        if (startDateInput.value && endDateInput.value) {
            const startDate = new Date(startDateInput.value);
            const endDate = new Date(endDateInput.value);
            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);
            return {startDate, endDate};
        } else if (monthInput.value) {
            const [year, month] = monthInput.value.split('-');
            return {
                startDate: new Date(year, month - 1, 1, 0, 0, 0),
                endDate: new Date(year, month, 0, 23, 59, 59)
            };
        } else if (dayInput.value) {
            const selectedDate = new Date(dayInput.value);
            selectedDate.setHours(0, 0, 0, 0);
            const nextDate = new Date(selectedDate);
            nextDate.setDate(selectedDate.getDate() + 1);
            nextDate.setHours(0, 0, 0, 0);
            return {startDate: selectedDate, endDate: nextDate};
        }
        return {startDate: startOfYear, endDate: endOfYear};
    }

    // Xử lý khoảng thời gian
    let dateRangeTimeout = null;

    function handleDateRangeChange() {
        const startDate = startDateInput.value ? new Date(startDateInput.value) : null;
        const endDate = endDateInput.value ? new Date(endDateInput.value) : null;

        if (dateRangeTimeout) {
            clearTimeout(dateRangeTimeout);
        }

        if (startDate && endDate) {
            if (startDate > endDate) {
                alert('Ngày bắt đầu không được lớn hơn ngày kết thúc!');
                startDateInput.value = '';
                endDateInput.value = '';
                updateChart(startOfYear, endOfYear, $('#tenSanPham').val());
                displayTopSellingProducts(
                    parseInt(document.getElementById('topProducts').value),
                    startOfYear,
                    endOfYear,
                    $('#tenSanPham').val()
                );
                return;
            }

            startDate.setHours(0, 0, 0, 0);
            endDate.setHours(23, 59, 59, 999);

            dateRangeTimeout = setTimeout(() => {
                updateChart(startDate, endDate, $('#tenSanPham').val());
                displayTopSellingProducts(
                    parseInt(document.getElementById('topProducts').value),
                    startDate,
                    endDate,
                    $('#tenSanPham').val()
                );
                resetOtherInputs(['startDate', 'endDate']);
            }, 300);
        } else if (!startDate && !endDate) {
            updateChart(startOfYear, endOfYear, $('#tenSanPham').val());
            displayTopSellingProducts(
                parseInt(document.getElementById('topProducts').value),
                startOfYear,
                endOfYear,
                $('#tenSanPham').val()
            );
        }
    }

    startDateInput.addEventListener('change', handleDateRangeChange);
    endDateInput.addEventListener('change', handleDateRangeChange);

    // Xử lý chọn tháng
    monthInput.addEventListener('change', function () {
        if (this.value) {
            const [year, month] = this.value.split('-');
            const startDate = new Date(year, month - 1, 1, 0, 0, 0);
            const endDate = new Date(year, month, 0, 23, 59, 59);
            updateChart(startDate, endDate, $('#tenSanPham').val());
            displayTopSellingProducts(
                parseInt(document.getElementById('topProducts').value),
                startDate,
                endDate,
                $('#tenSanPham').val()
            );
            resetOtherInputs(['monthInput']);
        } else {
            updateChart(startOfYear, endOfYear, $('#tenSanPham').val());
            displayTopSellingProducts(
                parseInt(document.getElementById('topProducts').value),
                startOfYear,
                endOfYear,
                $('#tenSanPham').val()
            );
        }
    });

    // Xử lý chọn ngày
    dayInput.addEventListener('change', function () {
        if (this.value) {
            const selectedDate = new Date(this.value);
            selectedDate.setHours(0, 0, 0, 0);
            const nextDate = new Date(selectedDate);
            nextDate.setDate(selectedDate.getDate() + 1);
            nextDate.setHours(0, 0, 0, 0);
            updateChart(selectedDate, nextDate, $('#tenSanPham').val());
            displayTopSellingProducts(
                parseInt(document.getElementById('topProducts').value),
                selectedDate,
                nextDate,
                $('#tenSanPham').val()
            );
            resetOtherInputs(['dayInput']);
        } else {
            updateChart(startOfYear, endOfYear, $('#tenSanPham').val());
            displayTopSellingProducts(
                parseInt(document.getElementById('topProducts').value),
                startOfYear,
                endOfYear,
                $('#tenSanPham').val()
            );
        }
    });

    // Xử lý thay đổi số lượng sản phẩm top
    // Xử lý thay đổi số lượng sản phẩm top
    const topProductsSelect = document.getElementById('topProducts');
    topProductsSelect.addEventListener('change', function () {
        const {startDate, endDate} = getCurrentDateRange();
        const selectedProductIds = $('#tenSanPham').val();

        updateChart(startDate, endDate, selectedProductIds);
        displayTopSellingProducts(
            parseInt(this.value),
            startDate,
            endDate,
            selectedProductIds
        );
    });
});

// Hàm phụ trợ để in ra các thông tin debug (có thể bỏ qua trong production)
function debugProductInfo() {
    console.log('Debug Product Information:');
    console.log('Hóa Đơn:', hoaDons);
    console.log('Sản Phẩm:', sanPhams);
    console.log('Hóa Đơn Chi Tiết:', hoaDonChiTiets);
    console.log('Sản Phẩm Chi Tiết:', sanPhamChiTiets);
}

// Hàm tính biểu đồ tổng quan về doanh thu
function createRevenueOverviewChart(startDate = null, endDate = null, selectedProductIds = null) {
    // Tính tổng doanh thu theo từng sản phẩm
    const revenueData = {};

    hoaDons.forEach(hoaDon => {
        const hoaDonDate = arrayToDate(hoaDon.ngayTao);

        // Kiểm tra điều kiện ngày
        const isDateInRange = !startDate || !endDate ||
            (hoaDonDate && hoaDonDate >= startDate && hoaDonDate <= endDate);

        if (isDateInRange) {
            const chiTietHoaDons = hoaDonChiTiets.filter(ct => ct.idHoaDon === hoaDon.id);

            chiTietHoaDons.forEach(chiTiet => {
                const sanPhamChiTiet = sanPhamChiTiets.find(spct => spct.id === chiTiet.idSpct);
                if (sanPhamChiTiet) {
                    const sanPham = sanPhams.find(sp => sp.tenSanPham === sanPhamChiTiet.tenSanPham);

                    if (sanPham) {
                        // Kiểm tra điều kiện sản phẩm được chọn
                        if (!selectedProductIds || selectedProductIds.length === 0 ||
                            selectedProductIds.includes(sanPham.id.toString())) {

                            const productId = sanPham.id.toString();
                            const revenue = chiTiet.soLuong * chiTiet.donGia;

                            if (!revenueData[productId]) {
                                revenueData[productId] = {
                                    name: sanPham.tenSanPham,
                                    totalRevenue: 0,
                                    quantity: 0
                                };
                            }

                            revenueData[productId].totalRevenue += revenue;
                            revenueData[productId].quantity += chiTiet.soLuong;
                        }
                    }
                }
            });
        }
    });

    // Chuyển đổi dữ liệu cho biểu đồ
    const labels = Object.values(revenueData).map(item => item.name);
    const revenueValues = Object.values(revenueData).map(item => item.totalRevenue);
    const quantityValues = Object.values(revenueData).map(item => item.quantity);

    // Tạo biểu đồ kết hợp
    const ctx = document.getElementById('myChart').getContext('2d');

    // Hủy biểu đồ cũ nếu tồn tại
    if (window.myChart instanceof Chart) {
        window.myChart.destroy();
    }

    window.myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Doanh Thu (VND)',
                    data: revenueValues,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    yAxisID: 'y-revenue'
                },
                {
                    label: 'Số Lượng Bán',
                    data: quantityValues,
                    backgroundColor: 'rgba(255, 99, 132, 0.6)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1,
                    yAxisID: 'y-quantity'
                }
            ]
        },
        options: {
            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false
            },
            scales: {
                'y-revenue': {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    title: {
                        display: true,
                        text: 'Doanh Thu (VND)'
                    }
                },
                'y-quantity': {
                    type: 'linear',
                    display: true,
                    position: 'right',
                    title: {
                        display: true,
                        text: 'Số Lượng Bán'
                    },
                    grid: {
                        drawOnChartArea: false
                    }
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: 'Tổng Quan Doanh Thu và Số Lượng Sản Phẩm'
                }
            }
        }
    });
}

// Hàm tạo biểu đồ tròn thể hiện tỷ lệ doanh thu từng sản phẩm
function createRevenuePieChart(startDate = null, endDate = null, selectedProductIds = null) {
    const revenueData = {};

    hoaDons.forEach(hoaDon => {
        const hoaDonDate = arrayToDate(hoaDon.ngayTao);

        const isDateInRange = !startDate || !endDate ||
            (hoaDonDate && hoaDonDate >= startDate && hoaDonDate <= endDate);

        if (isDateInRange) {
            const chiTietHoaDons = hoaDonChiTiets.filter(ct => ct.idHoaDon === hoaDon.id);

            chiTietHoaDons.forEach(chiTiet => {
                const sanPhamChiTiet = sanPhamChiTiets.find(spct => spct.id === chiTiet.idSpct);
                if (sanPhamChiTiet) {
                    const sanPham = sanPhams.find(sp => sp.tenSanPham === sanPhamChiTiet.tenSanPham);

                    if (sanPham) {
                        if (!selectedProductIds || selectedProductIds.length === 0 ||
                            selectedProductIds.includes(sanPham.id.toString())) {

                            const productId = sanPham.id.toString();
                            const revenue = chiTiet.soLuong * chiTiet.donGia;

                            if (!revenueData[productId]) {
                                revenueData[productId] = {
                                    name: sanPham.tenSanPham,
                                    totalRevenue: 0
                                };
                            }

                            revenueData[productId].totalRevenue += revenue;
                        }
                    }
                }
            });
        }
    });

    const labels = Object.values(revenueData).map(item => item.name);
    const revenueValues = Object.values(revenueData).map(item => item.totalRevenue);

    const ctx = document.getElementById('myChart').getContext('2d');

    if (window.myChart instanceof Chart) {
        window.myChart.destroy();
    }

    window.myChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: revenueValues,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(153, 102, 255, 0.6)'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Tỷ Lệ Doanh Thu Theo Sản Phẩm'
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            const value = context.parsed;
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = ((value / total) * 100).toFixed(2);
                            return `${context.label}: ${value.toLocaleString()} VND (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
}