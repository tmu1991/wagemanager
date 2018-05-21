package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.MenuRepository;
import com.wz.wagemanager.entity.SysMenu;
import com.wz.wagemanager.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuRepository menuRepository;
    @Override
    public List<SysMenu> findAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public List<SysMenu> findAllByFirstLevel() {
        return menuRepository.findByMenuCodeIsNull();
    }
}
