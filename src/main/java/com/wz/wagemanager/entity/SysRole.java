package com.wz.wagemanager.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author WindowsTen
 * 角色表
 */
@Data
@Builder
@Entity
@Table(name = "sys_role")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SysRole {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_alias")
    private String roleAlias;

    /**角色描述*/
    private String descrption;

    //部门id
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id")
    private SysDept dept;

//    @Column(name = "create_user")
//    private String createUser;
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "create_date")
//    private Date createDate;
//    @Column(name = "update_user")
//    private String updateUser;
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "update_date")
//    private Date updateDate;

//    //角色资源路径
//    @Column(name = "page_url")
//    private String pageUrl;
//    @Transient
//    @ManyToMany
//    @JoinColumn(name = "sys_auth",referencedColumnName = "auth_id")
//    private List<SysAuth> sysAuths;
}
