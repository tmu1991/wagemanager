package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.entity.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/16 10:49
 * @description
 */

public interface ActSalaryService {

    void save(ActSalary salary);

    void save(List<ActSalary> salaries);

    Page<ActSalary> findByDeptId(String deptId, Pageable pageable);

    ActSalary findByYearAndMonthAndWorkNo(int year, int month, String workNo);

    ActSalary findByWorkNo(String workNo);

    List<SalaryArea> findByGroupDept(List<String> ids) throws Exception;


    void removeByIdIn(String[] ids);

    void update(ActSalary salary) throws IllegalAccessException;

    ActSalary findById(String id);

    List<ActSalary> findByDeclareId(String declareId);

    void deleteByDeclareId(String declareId);

    void updateLoanStatus (String declareId, String deptId);

    Integer findLoanStatus(String declareId, String deptId);

    void upload(String filePath,String dateStr) throws Exception;
}
