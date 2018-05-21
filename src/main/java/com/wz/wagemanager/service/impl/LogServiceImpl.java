package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.LogRepository;
import com.wz.wagemanager.entity.SysLog;
import com.wz.wagemanager.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LogServiceImpl implements LogService {
    @Resource
    private LogRepository logRepository;
    @Override
    public void save(SysLog syslog) {
        logRepository.save(syslog);
    }

    @Override
    public Page<SysLog> findByPage(Pageable pageRequest) {
      return logRepository.findAll(pageRequest);
    }
}
