package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<SysLog,String> {

}
