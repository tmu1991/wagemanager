package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface LogService {
    void save(SysLog syslog);

    Page<SysLog> findByPage(Pageable pageRequest);
}
