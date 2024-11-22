// JavaScript code for managing order status and filter
let currentRow = null;
let currentSelect = null;

function changeStatus(select) {
    currentSelect = select;
    currentRow = select.closest('tr');
    const status = select.value;
    showToast();
}

function showToast() {
    const toast = document.getElementById('toast');
    toast.classList.add('show');
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000); // Show for 3 seconds
}

function filterOrders() {
    const filter = document.getElementById('order-status').value.toLowerCase();
    const rows = document.querySelectorAll('#orders-table tbody tr');

    rows.forEach(row => {
        const rowStatus = row.getAttribute('data-status');
        if (filter === '' || rowStatus === filter) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// Modal functionality
function showModal(status) {
    const modal = document.getElementById('status-modal');
    modal.style.display = "block";
    document.getElementById('confirm-btn').setAttribute('data-status', status);
}

function closeModal() {
    const modal = document.getElementById('status-modal');
    modal.style.display = "none";
}

function confirmStatusChange() {
    const status = document.getElementById('confirm-btn').getAttribute('data-status');
    const statusCell = currentRow.querySelector('.status');
    const newStatusText = capitalizeFirstLetter(status);

    // Update status in the table
    statusCell.textContent = newStatusText;
    currentRow.setAttribute('data-status', status);

    // Close modal
    closeModal();
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
