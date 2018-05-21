package com.wz.wagemanager.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController ("/newPassword.html").setViewName ("newPassword");
        registry.addViewController("/invalid.html").setViewName("invalid");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController ("/home.html").setViewName ("home");
        registry.addViewController ("/history.html").setViewName ("history");
        registry.addViewController ("/statistics.html").setViewName ("statistics");
        registry.addViewController ("/search.html").setViewName ("search");
        registry.addViewController ("/diary.html").setViewName ("diary");
        registry.addViewController ("/backlog.html").setViewName ("backlog");
        registry.addViewController ("/other.html").setViewName ("other");
//        registry.addViewController ("/dept").setViewName ("dept");
        registry.addViewController ("/user.html").setViewName ("user");
//        registry.addViewController ("/404").setViewName ("404");
        registry.addViewController ("/task.html").setViewName ("task");
        registry.addViewController ("/list.html").setViewName ("list");
    }
}
