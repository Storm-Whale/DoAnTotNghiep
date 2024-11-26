    const trangThaiLabels = {
        1: 'Chờ xác nhận',
        2: 'Đơn hàng đã xác nhận',
        3: 'Chuẩn bị đơn hàng',
        4: 'Giao hàng',
        5: 'Đơn hàng hoàn thành'
    };

// Khởi tạo biểu đồ
const ctx = document.getElementById('orderStatusChart').getContext('2d');
let chart;

// Hiển thị biểu đồ theo khoảng ngày
const startDateInput = document.getElementById('startDate');
const endDateInput = document.getElementById('endDate');

startDateInput.addEventListener('input', () => {
    updateChartByDateRange();
    clearMonthAndDayInputs();
});

endDateInput.addEventListener('input', () => {
    updateChartByDateRange();
    clearMonthAndDayInputs();
});

// Hiển thị biểu đồ theo tháng
const monthInput = document.getElementById('monthInput');
monthInput.addEventListener('input', () => {
    updateChartByMonth();
    clearStartEndDateAndDayInputs();
});

// Hiển thị biểu đồ theo ngày
const dayInput = document.getElementById('dayInput');
dayInput.addEventListener('input', () => {
    updateChartByDay();
    clearStartEndDateAndMonthInputs();
});

function updateChartByDateRange() {
    const startDate = startDateInput.value ? new Date(startDateInput.value) : new Date(new Date().getFullYear(), 0, 1);
    const endDate = endDateInput.value ? new Date(endDateInput.value) : new Date(new Date().getFullYear(), 11, 31);

    const filteredHoaDons = hoa_dons.filter(hoa_don => {
        const hoaDonDate = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]);
        return hoaDonDate >= startDate && hoaDonDate <= endDate;
    });

    updateChart(filteredHoaDons);
}

function updateChartByMonth() {
    const selectedMonth = monthInput.value;
    const filteredHoaDons = hoa_dons.filter(hoa_don => {
        const hoaDonMonth = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]).getMonth();
        const selectedMonthNum = new Date(selectedMonth).getMonth();
        return hoaDonMonth === selectedMonthNum;
    });

    updateChart(filteredHoaDons);
}

function updateChartByDay() {
    const selectedDay = new Date(dayInput.value);
    const filteredHoaDons = hoa_dons.filter(hoa_don => {
        const hoaDonDate = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]);
        return hoaDonDate.getDate() === selectedDay.getDate()
            && hoaDonDate.getMonth() === selectedDay.getMonth()
            && hoaDonDate.getFullYear() === selectedDay.getFullYear();
    });

    updateChart(filteredHoaDons);
}

function updateChart(filteredHoaDons) {
    if (filteredHoaDons.length === 0) {
        const config = {
            type: 'pie',
            data: {
                labels: ['Không có dữ liệu'],
                datasets: [{
                    data: [1],
                    backgroundColor: ['#e0e0e0'],
                    borderColor: ['#9e9e9e'],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        enabled: false
                    }
                }
            }
        };

        if (chart) {
            chart.destroy();
        }
        chart = new Chart(ctx, config);
    } else {
        const trangThaiCounts = filteredHoaDons.reduce((counts, hoa_don) => {
            counts[hoa_don.trangThai] = (counts[hoa_don.trangThai] || 0) + 1;
            return counts;
        }, {});

        const labels = Object.keys(trangThaiCounts).map(key => trangThaiLabels[key]);
        const data = Object.values(trangThaiCounts);

        const config = {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Trạng thái đơn hàng',
                    data: data,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.7)',
                        'rgba(54, 162, 235, 0.7)',
                        'rgba(255, 206, 86, 0.7)',
                        'rgba(75, 192, 192, 0.7)',
                        'rgba(153, 102, 255, 0.7)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                return `${tooltipItem.label}: ${tooltipItem.raw} đơn hàng`;
                            }
                        }
                    }
                }
            }
        };

        if (chart) {
            chart.destroy();
        }
        chart = new Chart(ctx, config);
    }
}

function clearStartEndDateAndMonthInputs() {
    startDateInput.value = '';
    endDateInput.value = '';
    monthInput.value = '';
}

function clearStartEndDateAndDayInputs() {
    startDateInput.value = '';
    endDateInput.value = '';
    dayInput.value = '';
}

function clearMonthAndDayInputs() {
    monthInput.value = '';
    dayInput.value = '';
}

// Hiển thị biểu đồ trạng thái đơn hàng ngay khi tải trang
updateChart(hoa_dons);