package nhom6.duancanhan.doantotnghiep.service.service;


import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;

public interface ForgotPasswordService {

    String sendResetCode(String email);

    String resetPassword(String resetCode, String newPassword);

    TaiKhoanDTO findByResetCode(String resetCode);

    TaiKhoanDTO saveTaiKhoan(TaiKhoanDTO taiKhoan);

    void sendHoaDon(String email, int idHoaDon);
}