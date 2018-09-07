package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.SysDept;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DeptRepository extends JpaRepository<SysDept,String>,JpaSpecificationExecutor<SysDept> {

//    @Query("select d from SysDept d where d.dept_name = ?1")
    SysDept findByDeptName(String deptName);
    @Query(value = "select count(*) from sys_dept",nativeQuery = true)
    long getSysDeptCount();

    SysDept findById(String id);

    List<SysDept> findByParentId(String parentId);

//    void batchInsert(List<SysDept> list);
}

