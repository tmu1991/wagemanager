package com.wz.wagemanager.security;

import com.alibaba.fastjson.JSONObject;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.ContextHolderUtils;
import com.wz.wagemanager.tools.PageBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//登录成功后，默认跳转到对应角色下的页面
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements InitializingBean {

//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
//        this.setDefaultTargetUrl ("/home.html");
//        String ajaxHeader = request.getHeader("X-Requested-With");
//        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
//        if (isAjax) {
        this.saveLoginInfo (request,authentication);
        response.setCharacterEncoding ("UTF-8");
        response.getWriter().print(JSONObject.toJSONString(new PageBean<> ()));
        response.getWriter().flush();
//        } else {
//            super.onAuthenticationSuccess(request, response, authentication);
//        }
    }

    @Resource
    private UserService userService;

    @Transactional (propagation= Propagation.REQUIRED,rollbackFor={Exception.class})
    public void saveLoginInfo(HttpServletRequest request,Authentication authentication){
        SysUser user = (SysUser)authentication.getPrincipal();
        try {
            user.setLoginTime (new Date());
            user.setLoginIp(getIpAddress(request));
            this.userService.insertUser (user);
        } catch (DataAccessException e) {
            if(logger.isWarnEnabled()){
                logger.info("无法更新用户登录信息至数据库");
            }
        }
    }

    private String getIpAddress(HttpServletRequest request){
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

    @Override
    public void afterPropertiesSet () throws Exception {

    }
}
