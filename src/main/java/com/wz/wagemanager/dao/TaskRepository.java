package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<ActTask,String>,JpaSpecificationExecutor<ActTask> {
    @Query(value = "select max(t.year) from ActTask t")
    Integer getMaxYear();

    @Query(value = "select max(t.month) from ActTask t where t.year=?1")
    Integer getMaxMonth(int year);

    @Query(value = "select count(*) from act_task where year=?1 and month=?2 AND status=?3",nativeQuery = true)
    Integer countByYearAndMonthAndStatus(int year, int month, int status);

    List<ActTask> findActTasksByYearAndMonthAndStatus(int year, int month, int status, Pageable pageable);

    ActTask findByYearAndMonthAndWorkNo(int year, int month, String workNo);

    List<ActTask> findByStatus(int status);
}
