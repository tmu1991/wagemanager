package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SalaryArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HiSalaryService {
    void save(HiSalary salary);
    void mutilSave(List<HiSalary> saveList);
    List<HiSalary> findByDeptIdAndDete(String deptId, int year, int month, Pageable page);
//    Integer countByDeptIdAndDate(String deptId, int year, int month);

    Page<HiSalary> findByPage(int year, int month,String deptId, Pageable page);

    Integer countByYearAndMonth(int year, int month);
    Page<HiSalary> findByWorkNoOrUsername(String workNo, String username, Pageable page);
//    Integer countByWorkNoOrUsername(String workNo, String username);
    HiSalary findByYearAndMonthAndUserId(int year, int month, String userId);
    List<SalaryArea> findByGroupDept(int year, int month) throws Exception;

    Integer getMaxYear();

    Integer getMaxMonth(int maxYear);
    List<HiSalary> findByDeptIdAndYearAndMonth(String deptId, int year, int month);
//
//    void removeByIdIn (List<String> ids);
//
//    void update (HiSalary hiSalary) throws IllegalAccessException;
}
