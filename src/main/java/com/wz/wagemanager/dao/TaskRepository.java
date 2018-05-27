package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<ActTask,String>,JpaSpecificationExecutor<ActTask> {

    ActTask findByTaskDateAndWorkNoAndType(Date taskDate,String workNo, Integer type);

}
