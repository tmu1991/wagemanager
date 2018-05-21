package com.wz.wagemanager.security;

import com.wz.wagemanager.tools.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter(AuthenticationManager authenticationManager) {
        super();
        this.setAuthenticationManager(authenticationManager);
    }
    private boolean postOnly = true;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String deptId = this.obtainDept(request);
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            if(StringUtils.isNotBlank (deptId)){
                ThreadLocalUtil.set(String.valueOf (deptId));
            }
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    private String obtainDept(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("deptId");
    }
}
