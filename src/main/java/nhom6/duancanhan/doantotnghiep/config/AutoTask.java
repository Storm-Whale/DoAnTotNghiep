package nhom6.duancanhan.doantotnghiep.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.ForgotPasswordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AutoTask {

    LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // Đầu ngày hôm nay (00:00:00)
    LocalDateTime endOfDay = startOfDay.plusDays(1);

    private final HoaDonRepository hoaDonRepository;
    private final ForgotPasswordService forgotPasswordService;

    @Scheduled(fixedDelay = 30000)  // 5 giây = 5000 mili giây
    public void performTask() {
//        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
//        List<HoaDon> hoaDons = hoaDonRepository.findHoaDonByTrangThaiAndNgayBeHonNgayHomNay3Ngay(threeDaysAgo);
        List<HoaDon> hoaDons = hoaDonRepository.findHoaDonByTrangThaiAndNgayBangHomNay(startOfDay, endOfDay);
        if (!hoaDons.isEmpty()){
            for (HoaDon hoaDon : hoaDons){
                hoaDon.setTrangThai(6);
                forgotPasswordService.sendHoaDonHT(hoaDon.getKhachHang().getEmail());
                hoaDonRepository.save(hoaDon);
            }
        }
       log.info("Hello");
    }

}
