package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.TaskRepository;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.service.TaskService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;

    @Override
    public List<ActTask> getTask(int year, int month, PageRequest pageRequest) {
        return taskRepository.findActTasksByYearAndMonthAndStatus(year,month,1,pageRequest);
    }

    @Override
    public Integer getMaxYear() {
        return taskRepository.getMaxYear();
    }

    @Override
    public Integer getMaxMonth(int year) {
        return taskRepository.getMaxMonth(year);
    }

    @Override
    public Integer getCount(int year, int month) {
        return taskRepository.countByYearAndMonthAndStatus(year,month,1);
    }

    @Override
    public ActTask findByYearAndMonthAndWorkNo(int year, int month, String workNo) {
        return taskRepository.findByYearAndMonthAndWorkNo(year,month,workNo);
    }

    @Override
    public List<ActTask> findByStatus(int status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public void save(ActTask task) {
        taskRepository.save(task);
    }
}
