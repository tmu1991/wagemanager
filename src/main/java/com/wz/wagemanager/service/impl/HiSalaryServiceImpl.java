package com.wz.wagemanager.service.impl;


import com.wz.wagemanager.dao.HiSalaryRepository;
import com.wz.wagemanager.dao.TaskRepository;
import com.wz.wagemanager.dao.UserRepository;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.service.HiSalaryService;
import com.wz.wagemanager.tools.CommonUtils;
import com.wz.wagemanager.tools.CriteriaUtils;
import com.wz.wagemanager.tools.GlobalConstant;
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
import javax.persistence.criteria.Predicate;
import java.util.*;

@Service("hiSalaryService")
@Transactional
public class HiSalaryServiceImpl implements HiSalaryService {

    @Resource
    private HiSalaryRepository salaryRepository;

    @Override
    public void save(HiSalary salary) {
        salaryRepository.save(salary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public void mutilSave(List<HiSalary> saveList) {
//        salaryRepository.batchInsert(saveList);
        salaryRepository.save (saveList);
    }



    @Override
    public List<HiSalary> findByDeptIdAndDete (String deptId,int year,int month,Pageable page) {
        return salaryRepository.findByDeptIdAndYearAndMonth (deptId,year,month,page);
    }

    @Override
    public Integer countByDeptIdAndDate (String deptId,int year,int month) {
        return salaryRepository.countByDeptIdAndDate (deptId,year,month);
    }

    @Override
    public List<HiSalary> findByYearAndMonth (int year, int month, Pageable page) {
        return salaryRepository.findHiSalariesByYearAndMonth (year,month,page);
    }

    @Override
    public Integer countByYearAndMonth (int year, int month) {
        return salaryRepository.countByYearAndMonth (year,month);
    }

    @Override
    public Page<HiSalary> findByWorkNoOrUsername(String workNo,String username, Pageable page){
        Map<String,Object> speMap=new HashMap<> (2);
        speMap.put ("workNo",workNo);
        speMap.put ("username",username);
        return salaryRepository.findAll(CriteriaUtils.getSpe (speMap), page);
    }

    @Override
    public HiSalary findByYearAndMonthAndUserId (int year, int month, String userId) {
        return salaryRepository.findByYearAndMonthAndUserId (year,month,userId);
    }

    @Override
    public List<SalaryArea> findByGroupDept (int year, int month) throws Exception {
        return salaryRepository.findByGroupDept (year,month);
//        return CommonUtils.castEntity(salaryRepository.findByGroupDept (year, month),HiSalary.class, GlobalConstant.properties);
    }

    @Override
    public Integer getMaxYear() {
        return salaryRepository.getMaxYear();
    }

    @Override
    public Integer getMaxMonth(int maxYear) {
        return salaryRepository.getMaxMonth(maxYear);
    }

    @Override
    public List<HiSalary> findByDeptIdAndYearAndMonth(String deptId, int year, int month) {
        return salaryRepository.findByDeptIdAndYearAndMonth(deptId,year,month);
    }

}
