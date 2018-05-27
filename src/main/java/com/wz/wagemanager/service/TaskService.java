package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface TaskService {
    ActTask findByTaskDateAndWorkNoAndType(Date taskDate, String workNo, Integer type);
    void save(ActTask task);
    void save(List<ActTask> tasks);
    Page<ActTask> findByPage(Pageable pageable);
}
