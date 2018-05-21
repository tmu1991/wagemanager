package com.wz.wagemanager.security;

import com.alibaba.fastjson.JSONObject;
import com.wz.wagemanager.tools.PageBean;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        HttpServletRequest request = event.getRequest();
        String ajaxHeader = request.getHeader("X-Requested-With");
        System.out.println("ajaxHeader:"+ajaxHeader);
//        if ("XMLHttpRequest".equals(ajaxHeader)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSONObject.toJSONString(new PageBean<>(400)));
            response.flushBuffer();
//        } else {
//            response.sendRedirect("/login");
//        }

    }
}
