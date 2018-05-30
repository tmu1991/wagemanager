package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.ActForm;
import com.wz.wagemanager.entity.ActTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TaskService {
    ActTask findByTaskDateAndWorkNoAndType(String taskDate, String workNo, Integer type);
    void save(ActTask task);
    void save(List<ActTask> tasks);
    Page<ActTask> findByPage(Pageable pageable);
    List<ActTask> findBySalaryId(String salaryId);
    ActTask findById(String id);
    BigDecimal sumBySalaryAndType(String salaryId, Integer type);
    void upload(String filePath) throws Exception;
    void update(ActForm actForm);
    List<ActTask> findByDeptId(String deptId);

    void charged (String deptId, List<String> ids);
}
