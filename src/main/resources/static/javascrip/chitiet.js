document.getElementById('button-minus').onclick = function() {
    var value = parseInt(document.getElementById('input-number').value, 10);
    value = isNaN(value) ? 1 : value;
    value = value > 1 ? value - 1 : 1;
    document.getElementById('input-number').value = value;
}

document.getElementById('button-plus').onclick = function() {
    var value = parseInt(document.getElementById('input-number').value, 10);
    value = isNaN(value) ? 1 : value;
    value = value < 100 ? value + 1 : 100;
    document.getElementById('input-number').value = value;
}
