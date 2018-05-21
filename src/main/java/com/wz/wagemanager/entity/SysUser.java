package com.wz.wagemanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * 库表设计：
 *   用户表 1 to 1 工资表
 *   用户表 1 to many 工资历史表
 * */
@Entity
@Table(name = "sys_user")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SysUser implements UserDetails {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;
    @Column
    private String username;
    @JSONField(serialize = false)
    private String password;
    @Column(name = "login_time")
    private Date loginTime;
    /** 创建人 */
    @Column(name = "create_user")
    private String createUser;
    /** 更新人 */
    @Column(name = "update_user")
    private String updateUser;
    /** 创建日期 */
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    /** 修改日期*/
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    /**用户状态 0表示禁用 1正常*/
    private Integer status;
    //optional是否可以为空
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private SysRole sysRole;
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id")
    private SysDept sysDept;

//    @Column(name = "role_id")
//    private String roleId;
//    @Column(name = "dept_id")
//    private String deptId;

    private BigDecimal base;
    //工龄工资
    private BigDecimal seniority;
    //考勤编号
    @Column(name = "work_no",unique = true)
    private String workNo;
    //自定义编号
    @Column(name = "custom_no")
    private String customNo;
    //银行卡号
    @Column(name = "credit_card")
    private String creditCard;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.sysRole.getRoleAlias()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
