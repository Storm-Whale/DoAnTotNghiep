const trangThaiLabels = {
    1: 'Chờ xác nhận',
    2: 'Đơn hàng đã xác nhận',
    4: 'Giao hàng',
    5: 'Đơn hàng hoàn thành',
    6: 'Đơn hàng bị huỷ'
};

// Define status mapping for cards
const statusElementIds = {
    1: 'statusPending',     // Chờ xác nhận
    2: 'statusConfirmed',   // Đã xác nhận
    4: 'statusDelivered',   // Giao hàng
    5: 'statusCompleted',   // Hoàn thành
    6: 'statusCanceled'     // Đã huỷ
};

// Khởi tạo biểu đồ
const ctx = document.getElementById('orderStatusChart').getContext('2d');
let chart;

// Get all input elements
const startDateInput = document.getElementById('startDate');
const endDateInput = document.getElementById('endDate');
const monthInput = document.getElementById('monthInput');
const dayInput = document.getElementById('dayInput');
const displayTypeSelect = document.getElementById('displayTypeSelect');

// Calculate total invoices
function calculateTotalInvoices(filteredHoaDons) {
    return filteredHoaDons.length;
}

// Get currently filtered invoices
function getCurrentFilteredHoaDons() {
    // If a date range is selected
    if (startDateInput.value || endDateInput.value) {
        const startDate = startDateInput.value ? new Date(startDateInput.value) : new Date(new Date().getFullYear(), 0, 1);
        const endDate = endDateInput.value ? new Date(endDateInput.value) : new Date(new Date().getFullYear(), 11, 31);

        return hoa_dons.filter(hoa_don => {
            const hoaDonDate = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]);
            return hoaDonDate >= startDate && hoaDonDate <= endDate;
        });
    }

    // If a month is selected
    if (monthInput.value) {
        const selectedMonth = monthInput.value;
        return hoa_dons.filter(hoa_don => {
            const hoaDonMonth = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]).getMonth();
            const selectedMonthNum = new Date(selectedMonth).getMonth();
            return hoaDonMonth === selectedMonthNum;
        });
    }

    // If a day is selected
    if (dayInput.value) {
        const selectedDay = new Date(dayInput.value);
        return hoa_dons.filter(hoa_don => {
            const hoaDonDate = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]);
            return hoaDonDate.getDate() === selectedDay.getDate()
                && hoaDonDate.getMonth() === selectedDay.getMonth()
                && hoaDonDate.getFullYear() === selectedDay.getFullYear();
        });
    }

    // If no filter is applied, return all invoices
    return hoa_dons;
}

// Update status cards
function updateStatusCards(filteredHoaDons) {
    // Reset all status card values
    Object.values(statusElementIds).forEach(elementId => {
        document.getElementById(elementId).textContent = '0';
    });

    // Count status occurrences
    const statusCounts = filteredHoaDons.reduce((counts, hoa_don) => {
        counts[hoa_don.trangThai] = (counts[hoa_don.trangThai] || 0) + 1;
        return counts;
    }, {});

    // Get display type
    const displayType = displayTypeSelect.value;
    const totalInvoices = calculateTotalInvoices(filteredHoaDons);

    // Update status cards with counts or percentages
    Object.entries(statusCounts).forEach(([status, count]) => {
        const elementId = statusElementIds[status];
        if (elementId) {
            const element = document.getElementById(elementId);

            if (displayType === 'number') {
                element.textContent = count;
            } else {
                // Calculate percentage
                const percentage = totalInvoices > 0
                    ? ((count / totalInvoices) * 100).toFixed(1)
                    : 0;
                element.textContent = `${percentage}%`;
            }
        }
    });
}

// Update chart function
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
        const displayType = displayTypeSelect.value;
        const totalInvoices = calculateTotalInvoices(filteredHoaDons);

        const trangThaiCounts = filteredHoaDons.reduce((counts, hoa_don) => {
            counts[hoa_don.trangThai] = (counts[hoa_don.trangThai] || 0) + 1;
            return counts;
        }, {});

        const labels = Object.keys(trangThaiCounts).map(key => trangThaiLabels[key]);
        const data = displayType === 'number'
            ? Object.values(trangThaiCounts)
            : Object.values(trangThaiCounts).map(count => ((count / totalInvoices) * 100).toFixed(1));

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
                            label: function (tooltipItem) {
                                return displayType === 'number'
                                    ? `${tooltipItem.label}: ${tooltipItem.raw} đơn hàng`
                                    : `${tooltipItem.label}: ${tooltipItem.raw}%`;
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

// Update functions for different date selections
function updateChartByDateRange() {
    const startDate = startDateInput.value ? new Date(startDateInput.value) : new Date(new Date().getFullYear(), 0, 1);
    const endDate = endDateInput.value ? new Date(endDateInput.value) : new Date(new Date().getFullYear(), 11, 31);

    const filteredHoaDons = hoa_dons.filter(hoa_don => {
        const hoaDonDate = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]);
        return hoaDonDate >= startDate && hoaDonDate <= endDate;
    });

    updateChart(filteredHoaDons);
    updateStatusCards(filteredHoaDons);
}

function updateChartByMonth() {
    const selectedMonth = monthInput.value;
    const filteredHoaDons = hoa_dons.filter(hoa_don => {
        const hoaDonMonth = new Date(hoa_don.ngaySua[0], hoa_don.ngaySua[1] - 1, hoa_don.ngaySua[2]).getMonth();
        const selectedMonthNum = new Date(selectedMonth).getMonth();
        return hoaDonMonth === selectedMonthNum;
    });

    updateChart(filteredHoaDons);
    updateStatusCards(filteredHoaDons);
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
    updateStatusCards(filteredHoaDons);
}

// Clear input functions
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

// Event listeners
startDateInput.addEventListener('input', () => {
    updateChartByDateRange();
    clearMonthAndDayInputs();
});

endDateInput.addEventListener('input', () => {
    updateChartByDateRange();
    clearMonthAndDayInputs();
});

monthInput.addEventListener('input', () => {
    updateChartByMonth();
    clearStartEndDateAndDayInputs();
});

dayInput.addEventListener('input', () => {
    updateChartByDay();
    clearStartEndDateAndMonthInputs();
});

// Event listener for display type change
displayTypeSelect.addEventListener('change', () => {
    // Re-update the status cards and chart with the current data
    const currentFilteredHoaDons = getCurrentFilteredHoaDons();
    updateStatusCards(currentFilteredHoaDons);
    updateChart(currentFilteredHoaDons);
});

// Initial load
updateChart(hoa_dons);
updateStatusCards(hoa_dons);