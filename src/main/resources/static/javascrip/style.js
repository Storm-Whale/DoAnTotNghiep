function toggleFilter(id) {
    var ul = document.getElementById(id);
    if (ul.style.display === "block") {
        ul.style.display = "none";
    } else {
        ul.style.display = "block";
    }
}