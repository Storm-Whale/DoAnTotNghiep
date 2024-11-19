package nhom6.duancanhan.doantotnghiep.service.service;


import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;

public interface ForgotPasswordService {


    public String sendResetCode(String email);

    public String resetPassword(String resetCode, String newPassword);

    public TaiKhoanDTO findByResetCode(String resetCode);
    public TaiKhoanDTO saveTaiKhoan(TaiKhoanDTO taiKhoan);
}