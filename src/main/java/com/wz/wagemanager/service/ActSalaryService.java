package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.entity.SysDeclare;
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
    List<ActSalary> findByDeptIdAndDete(String deptId, int year, int month, PageRequest page);
    Integer countByDeptIdAndDate(String deptId, int year, int month);
    List<ActSalary> findByYearAndMonth(int year, int month, PageRequest page);
    Integer countByYearAndMonth(int year, int month);
    Page<ActSalary> findByWorkNoOrUsername(String workNo, String username, PageRequest page);
    Integer countByWorkNoOrUsername(String workNo, String username);
    ActSalary findByYearAndMonthAndWorkNo(int year, int month, String workNo);
    List<SalaryArea> findByGroupDept() throws Exception;

    Integer getMaxYear();

    Integer getMaxMonth(int maxYear);
    List<ActSalary> findByDeptIdAndYearAndMonth(String deptId, int year, int month);

    void removeByIdIn(String[] ids);

    void update(ActSalary salary) throws IllegalAccessException;

    ActSalary findById(String id);

    void deleteAll(List<ActSalary> actSalaries);

    List<ActSalary> findByDeclareId(String declareId);
}
