// Constants
const MONTH_NAMES = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
    'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];

class StatisticsManager {
    constructor() {
        this.chart = null;
        this.inputs = {
            startDate: document.getElementById('startDate'),
            endDate: document.getElementById('endDate'),
            month: document.getElementById('monthInput'),
            day: document.getElementById('dayInput')
        };
        this.totalInvoicesElement = document.getElementById('totalInvoices');
        this.totalRevenueElement = document.getElementById('totalRevenue');

        this.initializeEventListeners();
        this.debugStatistics(); // Added debug method
    }

    // Debug method to understand data structure
    debugStatistics() {
        console.log('Debugging Statistics:');
        console.log('Hóa Đơns:', hoa_dons);

        if (!Array.isArray(hoa_dons)) {
            console.error('hoa_dons is not an array');
            return;
        }

        const statuses = [...new Set(hoa_dons.map(hd => hd.trangThai))];
        console.log('Unique Statuses:', statuses);

        // Log first few entries to understand structure
        console.log('Sample Entries:', hoa_dons.slice(0, 5));
    }

    initializeEventListeners() {
        // Event handlers for different input types
        this.inputs.month.addEventListener('change', () => {
            this.clearOtherDateInputs(this.inputs.month);
            this.updateChart();
        });

        this.inputs.day.addEventListener('change', () => {
            this.clearOtherDateInputs(this.inputs.day);
            this.updateChart();
        });

        this.inputs.startDate.addEventListener('change', () => {
            this.clearOtherDateInputs(this.inputs.startDate);
            this.updateChart();
        });

        this.inputs.endDate.addEventListener('change', () => {
            this.clearOtherDateInputs(this.inputs.endDate);
            this.updateChart();
        });
    }

    // Helper method to clear other date inputs
    clearOtherDateInputs(currentInput) {
        const allInputs = [
            this.inputs.startDate,
            this.inputs.endDate,
            this.inputs.month,
            this.inputs.day
        ];

        allInputs.forEach(input => {
            if (input !== currentInput) {
                input.value = '';
            }
        });
    }

    calculateDateRange() {
        const currentYear = new Date().getFullYear();
        let startDate, endDate;

        if (this.inputs.startDate.value && this.inputs.endDate.value) {
            // If both start and end dates are provided
            startDate = new Date(this.inputs.startDate.value);
            endDate = new Date(this.inputs.endDate.value);
        } else if (this.inputs.month.value) {
            // If a specific month is selected
            const [yearMonth] = this.inputs.month.value.split('-');
            startDate = new Date(yearMonth, 0, 1);
            endDate = new Date(yearMonth, 11, 31);
        } else if (this.inputs.day.value) {
            // If a specific day is selected
            startDate = new Date(this.inputs.day.value);
            endDate = new Date(this.inputs.day.value);
        } else {
            // Default to current year if no specific date is selected
            startDate = new Date(currentYear, 0, 1);
            endDate = new Date(currentYear, 11, 31);
        }

        return { start: startDate, end: endDate };
    }

    parseSelectedFilters() {
        return {
            month: this.inputs.month.value ?
                new Date(this.inputs.month.value).getMonth() + 1 :
                null,
            day: this.inputs.day.value || null
        };
    }

    calculateMonthlyStatistics(dateRange, selectedMonth, selectedDay) {
        const months = Array(12).fill(0);
        const revenue = Array(12).fill(0);
        let totalInvoices = 0;
        let totalRevenue = 0;

        try {
            if (!Array.isArray(hoa_dons)) {
                console.error('Invalid hoa_dons data structure');
                return { orders: months, revenue: revenue, totalInvoices, totalRevenue };
            }

            hoa_dons.forEach(hoa_don => {
                // Skip entries without valid date
                if (!hoa_don.ngaySua || !Array.isArray(hoa_don.ngaySua)) {
                    console.log('Invalid ngaySua:', hoa_don);
                    return;
                }

                // Parse date components
                const [year, month_str, day] = hoa_don.ngaySua;
                const month_index = parseInt(month_str) - 1;

                // Validate month index
                if (isNaN(month_index) || month_index < 0 || month_index > 11) {
                    console.log('Invalid month index:', month_index, 'for', hoa_don);
                    return;
                }

                // Create date object
                const date = new Date(`${year}-${month_str}-${day}`);

                // Validate entry based on selected filters
                if (this.isValidEntry(date, dateRange, month_index, selectedMonth, selectedDay)) {
                    months[month_index]++;
                    revenue[month_index] += (parseFloat(hoa_don.tongTien) || 0);

                    totalInvoices++;
                    totalRevenue += (parseFloat(hoa_don.tongTien) || 0);
                }
            });
        } catch (error) {
            console.error('Error processing statistics:', error);
        }

        console.log('Monthly Orders:', months);
        console.log('Monthly Revenue:', revenue);
        console.log('Total Invoices:', totalInvoices);
        console.log('Total Revenue:', totalRevenue);

        return { orders: months, revenue: revenue, totalInvoices, totalRevenue };
    }

    isValidEntry(date, dateRange, month_index, selectedMonth, selectedDay) {
        // If a specific month is selected
        if (selectedMonth !== null) {
            return month_index === selectedMonth - 1;
        }

        // If a specific day is selected
        if (selectedDay) {
            return date.toISOString().split('T')[0] === selectedDay;
        }

        // Check date range
        return date >= dateRange.start && date <= dateRange.end;
    }

    updateChart() {
        const dateRange = this.calculateDateRange();
        const { month: selectedMonth, day: selectedDay } = this.parseSelectedFilters();

        const { orders, revenue, totalInvoices, totalRevenue } = this.calculateMonthlyStatistics(
            dateRange,
            selectedMonth,
            selectedDay
        );

        // Update total invoices and revenue display
        this.totalInvoicesElement.textContent = totalInvoices.toLocaleString();
        this.totalRevenueElement.textContent = totalRevenue.toLocaleString('vi-VN', {
            style: 'currency',
            currency: 'VND'
        });

        // Initialize or update chart
        if (!this.chart) {
            this.initializeChart(orders, revenue);
        } else {
            this.updateExistingChart(orders, revenue);
        }
    }

    initializeChart(orders, revenue) {
        const ctx = document.getElementById('myChart')?.getContext('2d');
        if (!ctx) {
            console.error('Canvas context not found');
            return;
        }

        this.chart = new Chart(ctx, {
            type: 'bar',
            data: this.getChartData(orders, revenue),
            options: this.getChartOptions()
        });
    }

    updateExistingChart(orders, revenue) {
        if (!this.chart) return;

        this.chart.data.datasets[0].data = orders;
        this.chart.data.datasets[1].data = revenue;
        this.chart.update();
    }

    getChartData(orders, revenue) {
        return {
            labels: MONTH_NAMES,
            datasets: [
                {
                    label: 'Tổng Hóa Đơn',
                    data: orders,
                    backgroundColor: 'rgba(75, 192, 192, 0.5)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    yAxisID: 'y1'
                },
                {
                    label: 'Doanh Thu',
                    data: revenue,
                    backgroundColor: 'rgba(255, 99, 132, 0.5)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1,
                    yAxisID: 'y2'
                }
            ]
        };
    }

    getChartOptions() {
        return {
            scales: {
                x: { stacked: false },
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
                    text: 'Thống Kê Hóa Đơn và Doanh Thu'
                }
            }
        };
    }
}

// Initialize the statistics manager when the document is ready
document.addEventListener('DOMContentLoaded', () => {
    const statisticsManager = new StatisticsManager();
    statisticsManager.updateChart();
});