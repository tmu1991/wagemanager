package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ActTaskRepository extends JpaRepository<ActTask,String>,JpaSpecificationExecutor<ActTask> {

    ActTask findByTaskDateAndWorkNoAndType(String taskDate,String workNo,Integer type);

    @Query(value = "select sum(a.amount) from ActTask a where a.status=0 and a.type=?1 and a.workNo=?2")
    BigDecimal sumByTypeAndWorkNo(Integer type,String workNo);

    List<ActTask> findByWorkNo(String workNo);

    List<ActTask> findByDeptId(String deptId);

    List<ActTask> findByStatusAndDeptId(Integer status,String deptId);

    @Modifying
    @Query(value = "update ActTask a set a.status=?2 where a.id in ?1")
    void updateStatus(List<String> ids,Integer status);

    void deleteByStatusAndDeptId(Integer status,String deptId);
}
