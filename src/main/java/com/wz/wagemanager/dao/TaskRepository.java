package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TaskRepository extends JpaRepository<ActTask,String>,JpaSpecificationExecutor<ActTask> {

//    @Query(value = "select * from act_task t where task_date=:taskDate and work_no=:workNo and type=:type",nativeQuery = true)
    ActTask findByTaskDateAndWorkNoAndType(@Param ("taskDate") String taskDate, @Param ("workNo")String workNo, @Param ("type")Integer type);

    List<ActTask> findBySalaryId(String salaryId);

    @Query(value = "select sum(a.amount) from ActTask a where a.salaryId=?1 and a.type=?2")
    BigDecimal sumBySalaryAndType(String salaryId, Integer type);

    List<ActTask> findByDeptId(String deptId);

    List<ActTask> findByIdIn(List<String> ids);

    @Modifying
    @Query(value = "update ActTask a set a.status=?2 where a.id in ?1")
    void updateStatus(List<String> ids,Integer status);
}
