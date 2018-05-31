package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.ActSalaryRepository;
import com.wz.wagemanager.dao.DeclareRepository;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.entity.SysLog;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.tools.Assert;
import com.wz.wagemanager.tools.CommonUtils;
import com.wz.wagemanager.tools.DateUtil;
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
import java.util.Arrays;
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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (ActSalary salary) {
        actSalaryRepository.save (salary);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (List<ActSalary> salaries, SysLog sysLog) {
        actSalaryRepository.save (salaries);
    }

    @Override
    public Page<ActSalary> findByDeptId(String deptId, Pageable pageable) {
        return actSalaryRepository.findByDeptId(deptId,pageable);
    }


    @Override
    public ActSalary findByYearAndMonthAndWorkNo (int year, int month, String workNo) {
        return actSalaryRepository.findByYearAndMonthAndWorkNo (year,month,workNo);
    }

    @Override
    public ActSalary findByWorkNo (String workNo) {
        return actSalaryRepository.findByWorkNo (workNo);
    }

    @Override
    public List<SalaryArea> findByGroupDept (List<String> ids) throws Exception {
        return actSalaryRepository.findGroupByDept (ids);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @OperInfo (type = OperationType.UPDATE,desc = "删除工资记录")
    public void removeByIdIn (String[] ids, SysLog sysLog) {
        actSalaryRepository.removeByIdIn (ids);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @OperInfo (type = OperationType.UPDATE,desc = "修改工资信息")
    public void update (ActSalary salary, SysLog sysLog) throws IllegalAccessException {
        ActSalary actSalary = findById(salary.getId());
        CommonUtils.copyProperties(salary,actSalary,updateProperties);
        CommonUtils.calSalary (salary,null, DateUtil.getDateNum (salary.getYear (),salary.getMonth ()));
        actSalaryRepository.save (salary);
    }

    @Override
    public ActSalary findById (String id) {
        return actSalaryRepository.findOne (id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void deleteAll (List<ActSalary> actSalaries) {
        actSalaryRepository.delete (actSalaries);
    }

    @Override
    public List<ActSalary> findByDeclareId (String declareId) {
        return actSalaryRepository.findByDeclareId(declareId);
    }

    @Override
    public void deleteByDeclareId (String declareId) {
        actSalaryRepository.deleteByDeclareId (declareId);
    }

    private static final List<String> updateProperties= Arrays.asList ("coeff","base","seniority","busTravel","subDay","allowance","bonus");
}
