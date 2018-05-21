package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/16 10:37
 * @description
 */

public interface ActSalaryRepository extends JpaRepository<ActSalary,String>,JpaSpecificationExecutor<ActSalary> {
    ActSalary findByYearAndMonthAndUserId(int year, int month, String userId);

    Page<ActSalary> findByDeptId(String deptId, Pageable pageable);
}