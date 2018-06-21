package com.wz.wagemanager.service.impl;


import com.wz.wagemanager.dao.HiSalaryRepository;
import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.service.HiSalaryService;
import com.wz.wagemanager.tools.CriteriaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Page<HiSalary> findByPage (int year, int month,String deptId, Pageable page) {
        if(StringUtils.isNotBlank (deptId)){
            return salaryRepository.findHiSalariesByYearAndMonthAndDeptId (year,month,deptId,page);
        }
        return salaryRepository.findHiSalariesByYearAndMonth (year,month,page);
    }

    @Override
    public Integer countByYearAndMonth (int year, int month) {
        return salaryRepository.countByYearAndMonth (year,month);
    }

    @Override
    public Page<HiSalary> findByIDNumberOrUsername(String idNumber,String username, Pageable page){
        Map<String,Object> speMap=new HashMap<> (2);
        speMap.put ("iDNumber",idNumber);
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
