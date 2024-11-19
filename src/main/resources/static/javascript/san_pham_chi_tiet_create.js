$(document).ready(function () {
    $('.js-select2').select2({
        placeholder: "Chọn một tùy chọn",
        allowClear: true
    });
});

function updateColorDetails() {
    const colorSelect = document.getElementById("idMauSac");
    const colorDetailsContainer = document.getElementById("colorDetailsContainer");
    colorDetailsContainer.innerHTML = "";

    Array.from(colorSelect.selectedOptions).forEach((option, index) => {
        const colorId = option.value;
        const colorName = option.text;

        const detailContainer = document.createElement("div");
        detailContainer.classList.add("border", "rounded", "p-3", "mb-3");

        detailContainer.innerHTML = `
                <h5 class="mb-3">Chi tiết cho ${colorName}</h5>
                <input type="hidden" name="colorDetails[${index}].idMauSac" value="${colorId}">

                <div class="mb-3">
                    <label class="form-label">Giá</label>
                    <input type="number"
                           class="form-control"
                           name="colorDetails[${index}].gia"
                           step="any"
                           min="0"
                           required
                           placeholder="Nhập giá sản phẩm">
                </div>

                <div class="mb-3 d-flex align-items-center">
                    <label class="form-label">Kích thước</label>
                    <select class="form-control js-select2"
                            id="idKichCo"
                            name="colorDetails[${index}].idKichCos"
                            multiple required>
                        ${kichCoList.map(size => `
                            <option value="${size.id}">${size.tenKichCo}</option>
                        `).join('')}
                    </select>
                    <button type="button" class="btn btn-primary btn-btnQuickAddKichCo ms-2" onclick="hienForm('/admin/kichco/quick-add')">Thêm</button>
                </div>

                <div class="mb-3">
                    <label class="form-label">Số lượng</label>
                    <input type="number"
                           class="form-control"
                           name="colorDetails[${index}].soLuong"
                           min="1"
                           required
                           placeholder="Nhập số lượng">
                </div>

                <div class="mb-3">
                    <label class="form-label">Ảnh sản phẩm</label>
                    <input type="file"
                           class="form-control"
                           name="colorDetails[${index}].images"
                           multiple
                           accept="image/*"
                           required>
                </div>
            `;

        colorDetailsContainer.appendChild(detailContainer);

        // Khởi tạo Select2 cho select mới thêm vào
        $(detailContainer).find('.js-select2').select2({
            placeholder: "Chọn kích thước",
            allowClear: true
        });
    });
}