package nhom6.duancanhan.doantotnghiep.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

//    @Bean
//    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
//        return new HiddenHttpMethodFilter();
//    }
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
                .addResourceLocations("file:///D:/FALL_2024/DATN/DoAnTotNghiep/upload/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/admin/**") // Áp dụng cho đường dẫn này
                .excludePathPatterns("/login", "/css/**", "/js/**"); // Ngoại trừ các đường dẫn không cần kiểm tra
    }
}
