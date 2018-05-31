package com.wz.wagemanager.tools;

import com.wz.wagemanager.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author WindowsTen
 * @date 2018/2/23 9:40
 * @description
 */

public class ContextHolderUtils {

    private static ServletRequestAttributes getRequestAttr(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    public static HttpServletRequest getRequest(){
        return getRequestAttr ().getRequest();
    }

    public static HttpServletResponse getResponse(){
        return getRequestAttr ().getResponse ();
    }
    private static HttpSession getSession(){
        return getRequest ().getSession ();
    }

    public static void setAttribute(String key,Object object){
        getSession ().setAttribute (key,object);
    }
    public static Object getAttribute(String key){
        return getSession ().getAttribute (key);
    }

    private static Authentication getAuthentication (){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    public static SysUser getPrincipal(){
        return (SysUser) getAuthentication ().getPrincipal();
    }

    public static WebAuthenticationDetails getDetail(){
        return (WebAuthenticationDetails) getAuthentication().getDetails();
    }

}
