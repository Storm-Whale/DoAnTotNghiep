package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.entity.VaiTro;

import java.util.List;

public interface VaiTroService{
    List<VaiTro> getAll();
    VaiTro findById (Integer id);
}
