package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.ActSalaryRepository;
import com.wz.wagemanager.dao.DeclareRepository;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.tools.Assert;
import com.wz.wagemanager.tools.CommonUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/16 10:50
 * @description
 */
@Service("actSalaryService")
@Transactional(rollbackFor = Exception.class)
public class ActSalaryServiceImpl implements ActSalaryService {
    @Resource
    private ActSalaryRepository actSalaryRepository;
    @Resource
    private DeclareRepository declareRepository;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (ActSalary salary) {
        actSalaryRepository.save (salary);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (List<ActSalary> salaries) {
        actSalaryRepository.save (salaries);
    }

    @Override
    public Page<ActSalary> findByDeptId(String deptId, Pageable pageable) {
        return actSalaryRepository.findByDeptId(deptId,pageable);
    }

    @Override
    public List<ActSalary> findByDeptIdAndDete (String deptId, int year, int month, PageRequest page) {
        return null;
    }

    @Override
    public Integer countByDeptIdAndDate (String deptId, int year, int month) {
        return null;
    }

    @Override
    public List<ActSalary> findByYearAndMonth (int year, int month, PageRequest page) {
        return null;
    }

    @Override
    public Integer countByYearAndMonth (int year, int month) {
        return null;
    }

    @Override
    public Page<ActSalary> findByWorkNoOrUsername (String workNo, String username, PageRequest page) {
        return null;
    }

    @Override
    public Integer countByWorkNoOrUsername (String workNo, String username) {
        return null;
    }

    @Override
    public ActSalary findByYearAndMonthAndWorkNo (int year, int month, String workNo) {
        return actSalaryRepository.findByYearAndMonthAndWorkNo (year,month,workNo);
    }

    @Override
    public List<SalaryArea> findByGroupDept () throws Exception {
        return actSalaryRepository.findGroupByDept ();
    }

    @Override
    public Integer getMaxYear () {
        return null;
    }

    @Override
    public Integer getMaxMonth (int maxYear) {
        return null;
    }

    @Override
    public List<ActSalary> findByDeptIdAndYearAndMonth (String deptId, int year, int month) {
        return null;
    }

    @Override
    public void removeByIdIn (String[] ids) {
        actSalaryRepository.removeByIdIn (ids);
    }

    @Override
    public void update (ActSalary salary) throws IllegalAccessException {
        final ActSalary actSalary = findById(salary.getId());
        Assert.assertNotNull("当前记录不存在",actSalary);
        CommonUtils.copyNullValue(salary,actSalary);
        actSalaryRepository.save (salary);
    }

    @Override
    public ActSalary findById (String id) {
        return actSalaryRepository.findOne (id);
    }

    @Override
    public void deleteAll (List<ActSalary> actSalaries) {
        actSalaryRepository.delete (actSalaries);
    }

    @Override
    public List<ActSalary> findByDeclareId (String declareId) {
        return actSalaryRepository.findByDeclareId(declareId);
    }

}
