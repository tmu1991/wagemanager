package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.DeptRepository;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.service.DeptService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Resource
    private DeptRepository deptRepository;
    @Override
    @OperInfo(type = OperationType.QUERY,desc = "通过部门名称查找部门")
    public SysDept getDeptByDeptName(String deptName) {
        return deptRepository.findByDeptName(deptName);
    }

    @Override
    public SysDept save(SysDept sysDept) {
        return deptRepository.save(sysDept);
    }

    @Override
    public List<SysDept> findAll() {
        return deptRepository.findAll();
    }

    @Override
    public SysDept findById(String id) {
        return deptRepository.findById(id);
    }

    @Override
    public Page<SysDept> findByPage (Pageable pageable) {
        return deptRepository.findAll (pageable);
    }

    @Override
    public void deleteById (String deptId) {
        deptRepository.delete (deptId);
    }
}