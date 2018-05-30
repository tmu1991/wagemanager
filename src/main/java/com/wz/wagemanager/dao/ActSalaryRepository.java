package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SalaryArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author WindowsTen
 * @date 2018/5/16 10:37
 * @description
 */

public interface ActSalaryRepository extends JpaRepository<ActSalary,String>,JpaSpecificationExecutor<ActSalary> {

    ActSalary findByWorkNo(String workNo);

    ActSalary findByYearAndMonthAndWorkNo(int year, int month, String workNo);

    Page<ActSalary> findByDeptId(String deptId, Pageable pageable);

    void removeByIdIn(String[] ids);

    List<ActSalary> findByDeclareId(String declareId);

    @Query (value = "select new com.wz.wagemanager.entity.SalaryArea(a.deptId,a.deptName," +
            "a.year,a.month,a.declareId,sum(a.grossPay),sum(a.subWork)," +
            "sum(a.allowance),sum(a.insurance),sum(a.accuFund),sum(a.incomeTax),sum(a.payroll)," +
            "sum(a.late),sum(a.otherDebit),sum(a.partyDue),sum(a.loan),sum(a.other),sum(a.otherEl))" +
            "from ActSalary a where a.declareId in (?1) group by a.deptId")
    List<SalaryArea> findGroupByDept(List<String> ids);


}
