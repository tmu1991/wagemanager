package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TaskService {
    List<ActTask> getTask(int year, int month, PageRequest pageRequest);
    Integer getMaxYear();
    Integer getMaxMonth(int year);
    Integer getCount(int year, int month);
    ActTask findByYearAndMonthAndWorkNo(int year, int month, String workNo);
    List<ActTask> findByStatus(int status);
    void save(ActTask task);
}
