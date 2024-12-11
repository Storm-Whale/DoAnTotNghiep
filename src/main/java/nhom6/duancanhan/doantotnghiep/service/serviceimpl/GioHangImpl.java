package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.GioHang;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.repository.GioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class GioHangImpl implements GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;
    @Override
    public GioHang addGioHang(KhachHang khachHang) {
        if (khachHang == null) {
            throw new IllegalArgumentException("Khách hàng không được null.");
        }

        GioHang gioHang = new GioHang();
        gioHang.setKhachHang(khachHang);
        return gioHangRepository.save(gioHang);
    }
}
