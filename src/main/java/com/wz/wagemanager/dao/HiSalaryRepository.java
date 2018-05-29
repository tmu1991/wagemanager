package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SalaryArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface HiSalaryRepository extends JpaRepository<HiSalary,String>,JpaSpecificationExecutor<HiSalary> {

//    void batchInsert(List<HiSalary> list);

    Page<HiSalary> findHiSalariesByYearAndMonth(int year, int month, Pageable pageable);

    Page<HiSalary> findHiSalariesByYearAndMonthAndDeptId(int year, int month,String deptId, Pageable pageable);

    List<HiSalary> findByDeptIdAndYearAndMonth(String deptId, int year, int month, Pageable pageable);

    HiSalary findByYearAndMonthAndUserId(int year, int month, String userId);

    @Query(value = "SELECT count(*) from hi_salary WHERE dept_id=?1 AND year=?2 and month=?3",nativeQuery = true)
    Integer countByDeptIdAndDate(String deptId, int year, int month);

    @Query(value = "select count(*) from hi_salary WHERE YEAR =?1 AND MONTH =?2",nativeQuery = true)
    Integer countByYearAndMonth(int year, int month);

    @Query(value = "select new com.wz.wagemanager.entity.SalaryArea(a.deptId,a.deptName," +
            "a.year,a.month,a.declareId,sum(a.grossPay),sum(a.subWork)," +
            "sum(a.allowance),sum(a.insurance),sum(a.accuFund),sum(a.incomeTax),sum(a.payroll)," +
            "sum(a.late),sum(a.otherDebit),sum(a.partyDue),sum(a.loan),sum(a.other),sum(a.otherEl))" +
            "from HiSalary a WHERE a.year =?1 AND a.month =?2 GROUP BY a.deptId")
    List<SalaryArea> findByGroupDept(int year, int month);

    @Query(value = "select max(year) from hi_salary",nativeQuery = true)
    Integer getMaxYear();

    @Query(value = "select max(month) from hi_salary WHERE year=?1",nativeQuery = true)
    Integer getMaxMonth(int year);

    List<HiSalary> findByDeptIdAndYearAndMonth(String deptId, int year, int month);

//    void removeByIdIn (List<String> ids);

//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query(value = "update info p set p.status =?1 where p.id = ?2",nativeQuery = true)
//    Integer updateStatusById (String status, String id);

    HiSalary findById(String id);

//    @Query()
//    List<HiSalary> findHiSalariesByUsernameOrWorkNo(String queryStr);
}
