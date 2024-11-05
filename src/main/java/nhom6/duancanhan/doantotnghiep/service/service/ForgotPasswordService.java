package nhom6.duancanhan.doantotnghiep.service.service;



public interface ForgotPasswordService {


    public String sendResetCode(String email);

    public String resetPassword(String resetCode, String newPassword);
}