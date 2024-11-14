const monthNames = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];

const tong_so_hoa_don_theo_thang = (status, dateRange, selectedMonth, selectedDay) => {
    const months = Array(12).fill(0);
    hoa_dons.forEach(hoa_don => {
        const [year, month_str, day] = hoa_don.ngaySua;
        const month_index = parseInt(month_str) - 1;
        const date = new Date(`${year}-${month_str}-${day}`);

        if (
            hoa_don.trangThai === status &&
            date >= dateRange.start &&
            date <= dateRange.end &&
            (!selectedMonth || month_index === selectedMonth - 1) &&
            (!selectedDay || date.toISOString().split('T')[0] === selectedDay)
        ) {
            months[month_index]++;
        }
    });
    return months;
};

const doanh_thu_hoa_don_theo_thang = (status, dateRange, selectedMonth, selectedDay) => {
    const tongTien = Array(12).fill(0);
    hoa_dons.forEach(hoa_don => {
        const [year, month_str, day] = hoa_don.ngaySua;
        const month_index = parseInt(month_str) - 1;
        const date = new Date(`${year}-${month_str}-${day}`);

        if (
            hoa_don.trangThai === status &&
            date >= dateRange.start &&
            date <= dateRange.end &&
            (!selectedMonth || month_index === selectedMonth - 1) &&
            (!selectedDay || date.toISOString().split('T')[0] === selectedDay)
        ) {
            tongTien[month_index] += hoa_don.tongTien;
        }
    });
    return tongTien;
};

let myChart;

function updateChart() {
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const monthInput = document.getElementById('monthInput');
    const dayInput = document.getElementById('dayInput');

    // Xóa lựa chọn khác khi chọn tháng
    monthInput.addEventListener('change', () => {
        startDateInput.value = '';
        endDateInput.value = '';
        dayInput.value = '';
    });

    // Xóa lựa chọn khác khi chọn ngày
    dayInput.addEventListener('change', () => {
        startDateInput.value = '';
        endDateInput.value = '';
        monthInput.value = '';
    });

    // Xóa lựa chọn khác khi chọn khoảng thời gian
    startDateInput.addEventListener('change', () => {
        monthInput.value = '';
        dayInput.value = '';
    });

    endDateInput.addEventListener('change', () => {
        monthInput.value = '';
        dayInput.value = '';
    });

    const startDate = startDateInput.value ? new Date(startDateInput.value) : new Date(new Date().getFullYear(), 0, 1);
    const endDate = endDateInput.value ? new Date(endDateInput.value) : new Date(new Date().getFullYear(), 11, 31);
    const dateRange = { start: startDate, end: endDate };

    const selectedMonth = monthInput.value ? parseInt(monthInput.value.split("-")[1]) : null;
    const selectedDay = dayInput.value || null;

    const tong_so_hoa_don = tong_so_hoa_don_theo_thang(4, dateRange, selectedMonth, selectedDay);
    const doanh_thu = doanh_thu_hoa_don_theo_thang(4, dateRange, selectedMonth, selectedDay);

    if (myChart) {
        myChart.data.labels = monthNames;
        myChart.data.datasets[0].data = tong_so_hoa_don;
        myChart.data.datasets[1].data = doanh_thu;
        myChart.update();
    } else {
        const ctx = document.getElementById('myChart').getContext('2d');
        myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: monthNames,
                datasets: [
                    {
                        label: 'Tổng Hóa Đơn (Trạng Thái 4)',
                        data: tong_so_hoa_don,
                        backgroundColor: 'rgba(75, 192, 192, 0.5)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                        yAxisID: 'y1'
                    },
                    {
                        label: 'Doanh Thu (Trạng Thái 4)',
                        data: doanh_thu,
                        backgroundColor: 'rgba(255, 99, 132, 0.5)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 1,
                        yAxisID: 'y2'
                    }
                ]
            },
            options: {
                scales: {
                    x: {
                        stacked: false
                    },
                    y1: {
                        beginAtZero: true,
                        position: 'left',
                        title: {
                            display: true,
                            text: 'Tổng Hóa Đơn'
                        }
                    },
                    y2: {
                        beginAtZero: true,
                        position: 'right',
                        title: {
                            display: true,
                            text: 'Doanh Thu'
                        },
                        grid: {
                            drawOnChartArea: false
                        }
                    }
                },
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top'
                    },
                    title: {
                        display: true,
                        text: 'Tổng Hóa Đơn và Doanh Thu theo Ngày, Tháng, hoặc Khoảng Thời Gian'
                    }
                }
            }
        });
    }
}

// Gọi hàm cập nhật đồ thị ban đầu
updateChart();
