package nhom6.duancanhan.doantotnghiep.entity;

public enum TrangThaiPhieuGiamGia {
    ACTIVE(1),       // Hoạt động
    INACTIVE(0),     // Không hoạt động
    TEMPORARY_STOP(2); // Tạm dừng
    private final int value;
    TrangThaiPhieuGiamGia(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    // Phương thức để chuyển đổi giá trị int sang enum (tùy chọn)
    public static TrangThaiPhieuGiamGia fromValue(int value) {
        for (TrangThaiPhieuGiamGia status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
