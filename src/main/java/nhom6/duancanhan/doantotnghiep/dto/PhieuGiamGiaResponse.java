package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PhieuGiamGiaResponse implements Serializable {
    private Integer id;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private int soLuong;
    private int dieuKien;
    private int kieuGiamGia;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double giaTriMin;
    private double giaTriMax;
    private int trangThai;
}
