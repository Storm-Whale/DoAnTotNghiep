package nhom6.duancanhan.doantotnghiep.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;


@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        List<String> publicUrls = Arrays.asList(
                "/login",
                "/login/login-client",
                "/css/",
                "/js/",
                "/images/",
                "/access-denied"
        );

        // Kiểm tra URL public
        for (String publicUrl : publicUrls) {
            if (requestURI.contains(publicUrl)) {
                return true;
            }
        }


        List<String> dynamicURLPrefixes = Arrays.asList(
                "/admin/khachhang/update-client/",
                "/dia-chi/",
                "/admin/hoadon/delete_client/",
                "/admin/taiquay/xac-nhan-don-hang",
                "/admin/taiquay/xac-nhan"
        );

        for (String prefix : dynamicURLPrefixes) {
            if (requestURI.startsWith(prefix)|| requestURI.contains(prefix)) {
                return true;
            }
        }

        if (session == null || session.getAttribute("nhanvien") == null) {
            // Nếu là yêu cầu AJAX
            if (isAjaxRequest(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return false;
            }
            response.sendRedirect("/login/login-client");
            return false;
        }
        return true;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
                request.getHeader("Accept").contains("application/json");
    }
}
