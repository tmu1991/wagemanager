package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.SysDept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeptService {
    SysDept getDeptByDeptName(String deptName);

    SysDept save(SysDept sysDept);

    List<SysDept> findAll();

    SysDept findById(String id);

    Page<SysDept> findByPage(Pageable pageable);

    void deleteById(String deptId);

    List<SysDept> findFirst();

    List<SysDept> findByParent(String parentId);
}
