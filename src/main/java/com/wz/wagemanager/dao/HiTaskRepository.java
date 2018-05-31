package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.HiTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author WindowsTen
 * @date 2018/5/31 12:02
 * @description
 */

public interface HiTaskRepository extends JpaRepository<HiTask,String>,JpaSpecificationExecutor<HiTask> {

}
