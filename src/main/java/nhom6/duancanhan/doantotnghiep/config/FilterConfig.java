package nhom6.duancanhan.doantotnghiep.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistration() {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationFilter());
        registrationBean.addUrlPatterns("/*"); // Áp dụng cho tất cả các URL
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Chạy filter đầu tiên
        return registrationBean;
    }
}
