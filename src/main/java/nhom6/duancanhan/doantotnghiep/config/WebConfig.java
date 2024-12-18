package nhom6.duancanhan.doantotnghiep.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;
    @Autowired
    private RoleAccessInterceptor roleAccessInterceptor;

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new HiddenHttpMethodFilter());
        filterBean.addUrlPatterns("/admin/taiquay/*"); // Đảm bảo rằng phương thức DELETE được áp dụng đúng URL
        return filterBean;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///D:/WorkPlace/Java/DuAnTotNghiep/DoAnTotNghiep/upload/");
//                .addResourceLocations("file:///D:/FALL_2024/DATN/DoAnTotNghiep/upload/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleAccessInterceptor)
                .addPathPatterns("/admin/**") // Kiểm tra tất cả các đường dẫn bắt đầu bằng /admin/
                .excludePathPatterns(
                        "/login/login-client",  // Trang đăng nhập duy nhất
                        "/access-denied",       // Trang thông báo truy cập bị từ chối
                        "/css/**",              // Loại trừ các tài nguyên tĩnh
                        "/js/**",
                        "/images/**"
                );
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/login/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/access-denied"
                );
    }
}
