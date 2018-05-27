package com.wz.wagemanager.task;

import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class TimeTask {
    @Resource
    private UserService userService;
//    @Resource
//    private TaskService taskService;
    @Scheduled(cron = "0 0 0 6 1 *")
    public void seniorityTask(){
        userService.findAll().forEach(sysUser -> {
            BigDecimal seniority = sysUser.getSeniority();
            sysUser.setSeniority(seniority.add(new BigDecimal("70")));
            userService.insertUser(sysUser);
        });
    }
//    @Scheduled(cron = "0 0 0 6 * *")
//    public void backlogTask(){
//        taskService.findByStatus(1).forEach(actTask -> {
//            actTask.setStatus(0);
//            taskService.save(actTask);
//        });
//    }

}
