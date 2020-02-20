package com.sssj.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 	分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    /**
     * SQL执行效率插件,很好用！值得推荐，可以在控制台查看执行的sql时间以及执行的语句！！！！！！！
     */
    @Bean
    @Profile("dev")//这样只有引用application-dev.yml才会使该方法生效，生产环境不需要，浪费资源
    public PerformanceMonitorInterceptor performanceInterceptor() {
        return new PerformanceMonitorInterceptor();
    }
}