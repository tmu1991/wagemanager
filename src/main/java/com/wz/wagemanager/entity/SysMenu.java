package com.wz.wagemanager.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sys_menu")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SysMenu {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_code")
    private String menuCode;
    @Column(name = "menu_idx")
    private int menuIndex;
    @Column(name = "menu_icon")
    private String menuIcon;
//    @Column(name = "menu_url")
//    private String menuUrl;
    //是否可见
    private int visible;
    @OneToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER, targetEntity = SysMenu.class)
    @JoinColumn(name = "menu_code",insertable = false,updatable = false)
    private List<SysMenu> sysMenus;
}
