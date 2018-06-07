package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.ActForm;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.HiTask;
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

    ActTask findById(String id);

    BigDecimal sumByTypeAndWorkNo(Integer type,String workNo);

    List<ActTask> findByWorkNo(String workNo);

    void upload(String filePath) throws Exception;

    void update(ActForm actForm);

    List<ActTask> findByDeptId(String deptId);

    void charged (List<String> checkIds, List<String> unCheckIds,String declareId,String deptId);

    void saveHi(List<HiTask> tasks);

    List<ActTask> findByStatus(Integer status,String deptId);

    void deleteByStatus(Integer status,String deptId);
}
