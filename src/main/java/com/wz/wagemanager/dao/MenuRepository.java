package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<SysMenu,String> {
    List<SysMenu> findByMenuCodeIsNull();
}
