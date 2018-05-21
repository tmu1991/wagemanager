package com.wz.wagemanager.security;

import com.wz.wagemanager.entity.SysUser;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@ToString
@Data
public class CustomUsernamePasswordToken extends UsernamePasswordAuthenticationToken {
    private SysUser sysUser;
    public CustomUsernamePasswordToken(Object principal, Object credentials,SysUser sysUser) {
        super(principal, credentials);
        this.sysUser=sysUser;
    }

    public CustomUsernamePasswordToken(Object principal, Object credentials,
                                       Collection<? extends GrantedAuthority> authorities,SysUser sysUser) {
        super (principal, credentials, authorities);
        this.sysUser=sysUser;
    }
}
