package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.TaskRepository;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;

    @Override
    public Page<ActTask> findByPage(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public List<ActTask> findBySalaryId (String salaryId) {
        return taskRepository.findBySalaryId (salaryId);
    }

    @Override
    public ActTask findById (String id) {
        return taskRepository.findOne (id);
    }

    @Override
    public ActTask findByTaskDateAndWorkNoAndType(String taskDate, String workNo, Integer type) {
        return taskRepository.findByTaskDateAndWorkNoAndType(taskDate,workNo,type);
    }

    @Override
    public void save(ActTask task) {
        taskRepository.save(task);
    }

    @Override
    public void save(List<ActTask> tasks) {
        taskRepository.save(tasks);
    }
}
