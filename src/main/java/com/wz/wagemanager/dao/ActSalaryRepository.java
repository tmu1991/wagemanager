package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/16 10:37
 * @description
 */

public interface ActSalaryRepository extends JpaRepository<ActSalary,String>,JpaSpecificationExecutor<ActSalary> {

    ActSalary findByYearAndMonthAndUserId(int year, int month, String userId);

    Page<ActSalary> findByDeptId(String deptId, Pageable pageable);

    void removeByIdIn(String[] ids);

    List<ActSalary> findByDeclareId(String declareId);

    @Query(value = "select dept_id,dept_name,year,month,declare_id,sum(gross_pay) gross_pay," +
            "sum(sub_work) sub_work,sum(allowance) allowance,sum(insurance) insurance," +
            "sum(accu_fund) accu_fund,sum(income_tax) income_tax,sum(payroll) payroll," +
            "sum(late+other_debit+party_due+loan+other+other_el) other_debit" +
            "from act_salary GROUP BY dept_id",nativeQuery = true)
    List<ActSalary> findGroupByDept();
}
