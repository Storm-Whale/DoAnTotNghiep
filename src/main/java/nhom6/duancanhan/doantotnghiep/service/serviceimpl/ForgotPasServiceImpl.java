package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import nhom6.duancanhan.doantotnghiep.dto.KhachHangDTO;
import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;
import nhom6.duancanhan.doantotnghiep.repository.ForgotKHRepository;
import nhom6.duancanhan.doantotnghiep.repository.ForgotRepository;
import nhom6.duancanhan.doantotnghiep.repository.KhachHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.TaiKhoanRepository;
import nhom6.duancanhan.doantotnghiep.service.service.ForgotPasswordService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;
@Service
public class ForgotPasServiceImpl implements ForgotPasswordService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private ForgotRepository forgotRepository;

    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private ForgotKHRepository forgotKHRepository;

    @Override
    public String sendResetCode(String email) {
        Optional<KhachHangDTO> khachHangOpt = forgotKHRepository.findByEmail(email);
//        Optional<KhachHangDTO> khachHangDTO =
        if (khachHangOpt.isEmpty()) {
            return "Email không tồn tại trong hệ thống.";
        }


        KhachHangDTO khachHang = khachHangOpt.get();
        TaiKhoanDTO taiKhoan = khachHang.getTaiKhoan();


        if (taiKhoan == null) {
            return "Không tìm thấy tài khoản tương ứng.";
        }

        String resetCode = UUID.randomUUID().toString();
        taiKhoan.setResetCode(resetCode);
        forgotRepository.save(taiKhoan);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Đặt tên hiển thị với xử lý ngoại lệ trong try-catch
            try {
                helper.setFrom(new InternetAddress("your-email@example.com", "Yagi Shop"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "Lỗi mã hóa tên hiển thị.";
            }

            helper.setTo(email);
            helper.setSubject("Yêu cầu đặt lại mật khẩu");

            String logoUrl = "https://scontent.fhan2-3.fna.fbcdn.net/v/t39.30808-6/450538583_122093493734416855_6776650255010347746_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=FT06rq3PoX8Q7kNvgHg7BeL&_nc_zt=23&_nc_ht=scontent.fhan2-3.fna&_nc_gid=A4g5T09cMzam4FhcGpMuY-5&oh=00_AYDL6Y2tYEonuzBcq_4ROJzEj5Oc-BB6DLqHiXsPNQQ2Yw&oe=672CE46E";

            String htmlContent = "<div style='text-align: center;'>" +

                    "</div>" +
                    "<h1 style='color: #333;'>Xin chào!</h1>" +
                    "<p style='font-size: 16px; color: #555;'>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình.</p>" +
                    "<p style='font-size: 18px; font-weight: bold; color: #000;'>Mã đặt lại mật khẩu của bạn là: <strong style='color: #007BFF; font-size: 20px;'>" + resetCode + "</strong></p>" +
                    "<p style='font-size: 16px; color: #555;'>Vui lòng sử dụng mã này để đặt lại mật khẩu của bạn.</p>" +
                    "<p style='font-size: 16px; color: #555;'>Nếu bạn không yêu cầu thay đổi mật khẩu, hãy bỏ qua email này.</p>" +
                    "<p style='font-size: 16px; color: #555;'>Trân trọng,<br><em>Đội ngũ hỗ trợ của Yagi Shop</em></p>";

            helper.setText(htmlContent, true); // true để gửi dưới dạng HTML
            mailSender.send(message);
            return "Mã xác nhận đã được gửi đến email của bạn.";
        } catch (MessagingException e) {
            return "Gửi email thất bại.";
        }
    }


    @Override
    public String resetPassword(String resetCode, String newPassword) {
        Optional<TaiKhoanDTO> taiKhoanOpt = forgotRepository.findByResetCode(resetCode);

        if (taiKhoanOpt.isEmpty()) {
            return "Mã xác thực không hợp lệ.";
        }
        TaiKhoanDTO taiKhoan = taiKhoanOpt.get();
        taiKhoan.setMat_khau(newPassword);
        taiKhoan.setResetCode(null); // Xóa mã xác thực sau khi sử dụng
        forgotRepository.save(taiKhoan);
        return "Mật khẩu đã được cập nhật thành công.";
    }

    @Override
    public TaiKhoanDTO findByResetCode(String resetCode) {
        return forgotRepository.findByResetCode(resetCode).orElse(null);
    }

    @Override
    public TaiKhoanDTO saveTaiKhoan(TaiKhoanDTO taiKhoan) {
        return forgotRepository.save(taiKhoan);
    }

}

