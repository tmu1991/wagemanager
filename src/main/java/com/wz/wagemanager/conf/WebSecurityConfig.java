package com.wz.wagemanager.conf;

import com.wz.wagemanager.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级别的权限注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new LoginAuthenticationProvider(customUserDetailsService());
    }
    @Bean
    public AjaxSessionInformationExpiredStrategy ajaxSessionInformationExpiredStrategy(){
        return new AjaxSessionInformationExpiredStrategy();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
//                .authenticationProvider(authenticationProvider())
            .userDetailsService(customUserDetailsService())
            //指定密码加密所使用的加密器为passwordEncoder()
            .passwordEncoder(passwordEncoder());
            auth.eraseCredentials(false);
    }

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter (super.authenticationManager ());
        loginFilter.setAuthenticationSuccessHandler (loginSuccessHandler ());
        loginFilter.setAuthenticationFailureHandler (loginFailuerHandler());
        return loginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        // 加入自定义UsernamePasswordAuthenticationFilter替代原有Filter
        http.addFilterAt(loginFilter (), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/build/**","/dist/**","/examples/**","/src/**","/dept/all.json","/invalid.html").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginProcessingUrl ("/login")
                .loginPage("/login.html")
                //记住我 时间s 两个星期 key:cookie中的私钥
//                .and().rememberMe().tokenValiditySeconds(1209600).key("myKey")
                .and().logout()
                .logoutUrl("/logout.html")
                .logoutSuccessUrl("/login.html").permitAll()
                .invalidateHttpSession(true);
        // 关闭csrf
        http.csrf().disable();
        //session管理
        //session失效后跳转
        http.sessionManagement().invalidSessionUrl("/invalid.html");
//                .maximumSessions(1).expiredSessionStrategy(ajaxSessionInformationExpiredStrategy());
//        http.sessionManagement().expiredSessionStrategy(ajaxSessionInformationExpiredStrategy);
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler loginFailuerHandler () {
        SimpleUrlAuthenticationFailureHandler failureHandler = new LoginFailuerHandler ();
//        failureHandler.setDefaultFailureUrl ("/login?error");
        return failureHandler;
//        return new SimpleUrlAuthenticationFailureHandler ("/login.html?error=true");
    }
    //登录成功后可使用存储用户信息
    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }
}
