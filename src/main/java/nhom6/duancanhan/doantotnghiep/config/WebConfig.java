package nhom6.duancanhan.doantotnghiep.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
//@RequiredArgsConstructor
public class WebConfig {

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
}
