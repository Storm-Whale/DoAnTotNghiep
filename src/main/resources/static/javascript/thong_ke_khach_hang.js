// Khởi tạo biểu đồ
let monthlyCustomerChart;

// Hàm chuyển đổi ngày từ mảng sang đối tượng Date
function arrayToDate(dateArray) {
    return new Date(dateArray[0], dateArray[1] - 1, dateArray[2]);
}

// Hàm lọc khách hàng theo khoảng thời gian
function filterCustomersByDateRange(startDate, endDate) {
    return khachHangs.filter(customer => {
        const customerDate = arrayToDate(customer.ngayTao);
        return customerDate >= startDate && customerDate <= endDate;
    });
}

// Hàm tạo labels và data cho biểu đồ theo ngày
function generateDailyData(customers, startDate, endDate) {
    const labels = [];
    const data = [];
    const currentDate = new Date(startDate);

    while (currentDate <= endDate) {
        labels.push(currentDate.toLocaleDateString('vi-VN'));
        const count = customers.filter(customer => {
            const customerDate = arrayToDate(customer.ngayTao);
            return customerDate.toDateString() === currentDate.toDateString();
        }).length;
        data.push(count);
        currentDate.setDate(currentDate.getDate() + 1);
    }

    return { labels, data };
}

// Hàm tạo labels và data cho biểu đồ theo tháng
function generateMonthlyData(customers, year, month) {
    const daysInMonth = new Date(year, month, 0).getDate();
    const labels = Array.from({length: daysInMonth}, (_, i) => `Ngày ${i + 1}`);
    const data = Array(daysInMonth).fill(0);

    customers.forEach(customer => {
        if (customer.ngayTao[0] === year && customer.ngayTao[1] === month) {
            const day = customer.ngayTao[2] - 1;
            data[day]++;
        }
    });

    return { labels, data };
}

// Hàm cập nhật biểu đồ
function updateChart(labels, data, title) {
    if (monthlyCustomerChart) {
        monthlyCustomerChart.destroy();
    }

    const ctx = document.getElementById('myChart').getContext('2d');
    monthlyCustomerChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: title,
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.5)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                title: {
                    display: true,
                    text: title
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Thời gian'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng khách hàng'
                    },
                    ticks: {
                        callback: function(value) {
                            if (Math.floor(value) === value) {
                                return value;
                            }
                        }
                    },
                    min: 0,
                    suggestedMax: function(context) {
                        const max = Math.max(...context.chart.data.datasets[0].data);
                        return max + Math.ceil(max * 0.2);
                    }
                }
            },
            animation: {
                duration: 1000,
                easing: 'easeInOutQuart'
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 10,
                    bottom: 10
                }
            }
        }
    });
}

// Hàm render danh sách khách hàng
function renderCustomerList(customers) {
    const customerListEl = document.querySelector('.list-group');

    // Xóa danh sách hiện tại
    customerListEl.innerHTML = '';

    // Nếu không có khách hàng
    if (customers.length === 0) {
        customerListEl.innerHTML = '<li class="list-group-item text-muted">Không có khách hàng mới</li>';
        return;
    }

    // Sắp xếp khách hàng theo ngày tham gia mới nhất
    const sortedCustomers = customers.sort((a, b) => {
        const dateA = new Date(a.ngayTao[0], a.ngayTao[1] - 1, a.ngayTao[2]);
        const dateB = new Date(b.ngayTao[0], b.ngayTao[1] - 1, b.ngayTao[2]);
        return dateB - dateA;
    });

    // Render từng khách hàng
    sortedCustomers.forEach(customer => {
        const listItem = document.createElement('li');
        listItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');

        // Format ngày
        const formattedDate = `${customer.ngayTao[2]}/${customer.ngayTao[1]}/${customer.ngayTao[0]}`;

        listItem.innerHTML = `
            <div>
                <strong>${customer.ten || 'Khách hàng'}</strong>
                <small class="d-block text-muted">${customer.email || 'Không có email'}</small>
            </div>
            <span class="badge bg-primary rounded-pill">${formattedDate}</span>
        `;

        customerListEl.appendChild(listItem);
    });
}

// Hàm reset tất cả các input
function resetAllInputs() {
    document.getElementById('startDate').value = '';
    document.getElementById('endDate').value = '';
    document.getElementById('monthInput').value = '';
    document.getElementById('dayInput').value = '';
}

// Xử lý sự kiện cho khoảng ngày
function handleDateRangeChange() {
    const startDate = new Date(document.getElementById('startDate').value);
    const endDate = new Date(document.getElementById('endDate').value);

    if (startDate && endDate && startDate <= endDate) {
        // Reset other inputs
        document.getElementById('monthInput').value = '';
        document.getElementById('dayInput').value = '';

        const filteredCustomers = filterCustomersByDateRange(startDate, endDate);
        const { labels, data } = generateDailyData(filteredCustomers, startDate, endDate);
        updateChart(labels, data, `Số lượng khách hàng từ ${startDate.toLocaleDateString('vi-VN')} đến ${endDate.toLocaleDateString('vi-VN')}`);

        // Cập nhật danh sách khách hàng
        renderCustomerList(filteredCustomers);
    }
}

// Thêm sự kiện cho startDate và endDate
document.getElementById('startDate').addEventListener('change', handleDateRangeChange);
document.getElementById('endDate').addEventListener('change', handleDateRangeChange);

// Xử lý sự kiện cho tháng
document.getElementById('monthInput').addEventListener('change', function(e) {
    if (e.target.value) {
        // Reset other inputs
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';
        document.getElementById('dayInput').value = '';

        const [year, month] = e.target.value.split('-').map(Number);
        const filteredCustomers = khachHangs.filter(customer =>
            customer.ngayTao[0] === year && customer.ngayTao[1] === parseInt(month)
        );
        const { labels, data } = generateMonthlyData(filteredCustomers, year, parseInt(month));
        updateChart(labels, data, `Số lượng khách hàng trong tháng ${month}/${year}`);

        // Cập nhật danh sách khách hàng
        renderCustomerList(filteredCustomers);
    }
});

// Xử lý sự kiện cho ngày cụ thể
document.getElementById('dayInput').addEventListener('change', function(e) {
    if (e.target.value) {
        // Reset other inputs
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';
        document.getElementById('monthInput').value = '';

        const selectedDate = new Date(e.target.value);
        const year = selectedDate.getFullYear();
        const month = selectedDate.getMonth() + 1;
        const day = selectedDate.getDate();

        const filteredCustomers = khachHangs.filter(customer =>
            customer.ngayTao[0] === year &&
            customer.ngayTao[1] === month &&
            customer.ngayTao[2] === day
        );

        // Hiển thị dữ liệu với labels rõ ràng hơn
        updateChart(
            [`${day}/${month}/${year}`],
            [filteredCustomers.length],
            `Số lượng khách hàng ngày ${day}/${month}/${year}`
        );

        // Cập nhật danh sách khách hàng
        renderCustomerList(filteredCustomers);
    }
});

// Khởi tạo biểu đồ mặc định với dữ liệu của năm hiện tại
const initializeChart = () => {
    const today = new Date();
    const currentYear = today.getFullYear();
    const filteredCustomers = khachHangs.filter(customer => customer.ngayTao[0] === currentYear);

    const monthCounts = Array(12).fill(0);
    filteredCustomers.forEach(customer => {
        const month = customer.ngayTao[1] - 1;
        monthCounts[month]++;
    });

    const labels = [
        'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4',
        'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8',
        'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
    ];

    updateChart(labels, monthCounts, `Số lượng khách hàng trong năm ${currentYear}`);

    // Render danh sách khách hàng ban đầu
    renderCustomerList(filteredCustomers);
};

// Gọi hàm khởi tạo khi trang được tải
document.addEventListener('DOMContentLoaded', initializeChart);