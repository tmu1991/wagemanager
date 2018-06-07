package com.wz.wagemanager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.ActSalaryRepository;
import com.wz.wagemanager.dao.DeclareRepository;
import com.wz.wagemanager.entity.*;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.tools.Assert;
import com.wz.wagemanager.tools.CommonUtils;
import com.wz.wagemanager.tools.DateUtil;
import com.wz.wagemanager.tools.LogUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private LogService logService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (ActSalary salary) {
//        SysLog log = LogUtils.getLog (salary.getId ());
        actSalaryRepository.save (salary);
//        LogUtils.save (logService,log,salary);
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
    public void removeByIdIn (String[] ids) {
        List<Map<String, Object>> mapList = Arrays.stream (ids).filter (StringUtils::isNotBlank)
                .map (id -> getOperMap (findById (id))).collect (Collectors.toList ());
        actSalaryRepository.removeByIdIn (ids);
        new LogUtils (logService).save (OperationType.DELETE,null,mapList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void update (ActSalary salary) throws IllegalAccessException {
        ActSalary actSalary = findById(salary.getId());
        if(salary.getAllowance ()!=null){
            salary.setAllowance (getAllowance (salary.getAllowance (),actSalary.getAttendance ()));
        }
        Map<String, Object> properties = CommonUtils.copyProperties (salary, actSalary, updateProperties);
        if(properties != null){
            CommonUtils.calSalary (salary,null, DateUtil.getDateNum (salary.getYear (),salary.getMonth ()));
            actSalaryRepository.save (salary);
            new LogUtils (logService).save (OperationType.UPDATE,properties,getOperMap (actSalary));
        }
    }

    private BigDecimal getAllowance(BigDecimal allowance,BigDecimal attendance){
        return allowance.multiply(attendance).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public ActSalary findById (String id) {
        return actSalaryRepository.findOne (id);
    }

    @Override
    public List<ActSalary> findByDeclareId (String declareId) {
        return actSalaryRepository.findByDeclareId(declareId);
    }

    @Override
    public void deleteByDeclareId (String declareId) {
        actSalaryRepository.deleteByDeclareId (declareId);
    }

    @Override
    public void updateLoanStatus (String declareId, String deptId) {
        actSalaryRepository.updateLoanStatus (declareId,deptId);
    }

    @Override
    public Integer findLoanStatus (String declareId, String deptId) {
        return actSalaryRepository.findLoanStatus (declareId,deptId);
    }

    private static final List<String> updateProperties= Arrays.asList ("coeff","base","seniority","busTravel","subDay","allowance","bonus");

    private Map<String,Object> getOperMap(ActSalary actSalary){
        Map<String,Object> map= new HashMap<> (4);
        map.put ("年份",actSalary.getYear ());
        map.put ("月份",actSalary.getMonth ());
        map.put ("员工姓名",actSalary.getUsername ());
        map.put ("工号",actSalary.getWorkNo ());
        return map;
    }
}
