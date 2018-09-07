package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.DeptRepository;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.service.DeptService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("deptService")
@Transactional(rollbackFor = Exception.class)
public class DeptServiceImpl implements DeptService {
    @Resource
    private DeptRepository deptRepository;
    @Override
    public SysDept getDeptByDeptName(String deptName) {
        return deptRepository.findByDeptName(deptName);
    }

    @Override
    @Transactional (propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
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
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void deleteById (String deptId) {
        deptRepository.delete (deptId);
    }

    @Override
    public List<SysDept> findFirst() {
        return deptRepository.findByParentId(null);
    }

    @Override
    public List<SysDept> findByParent(String parentId) {
        return deptRepository.findByParentId(parentId);
    }
}
