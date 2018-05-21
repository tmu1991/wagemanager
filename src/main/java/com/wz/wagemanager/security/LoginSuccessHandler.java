package com.wz.wagemanager.security;

import com.wz.wagemanager.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//登录成功后，默认跳转到对应角色下的页面
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
//        this.setDefaultTargetUrl ("/home");
        //获得授权后可得到用户信息
//        CustomUsernamePasswordToken token = (CustomUsernamePasswordToken)authentication.getPrincipal();
//        SysUser sysUser = token.getSysUser();
//        sysUser.setLoginTime(new Date());
        new DefaultRedirectStrategy ().sendRedirect(request, response, "/home");
//        String ajaxHeader = request.getHeader("X-Requested-With");
//        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
//        if (isAjax) {
//            response.setCharacterEncoding ("UTF-8");
//            response.getWriter().print(sysUser.getSysRole().getPageUrl());
//            response.getWriter().flush();
//        } else {
//            super.onAuthenticationSuccess(request, response, authentication);
//        }
    }

    public String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
