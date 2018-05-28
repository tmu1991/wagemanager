package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/9 17:33
 * @description
 */
public interface DeclareService {

    void updateProperty(String key, Object value, String id);

    void save(SysDeclare declare);

    Page<SysDeclare> findByUser(SysUser user, Pageable page);

    void start(String declareId);

    SysDeclare findByProcessInstanceId(String processInstanceId);

    SysDeclare findById(String id);

    List<SysDeclare> findByDeptAndStatus(SysDept dept, int stauts);

    SysDeclare findModifiable(SysDept dept);

    List<SysDeclare> findNotComplete(SysDept sysDept);
}
