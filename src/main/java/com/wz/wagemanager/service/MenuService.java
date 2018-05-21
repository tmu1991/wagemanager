package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.SysMenu;

import java.util.List;

public interface MenuService {
    List<SysMenu> findAllMenus();
    List<SysMenu> findAllByFirstLevel();
}
