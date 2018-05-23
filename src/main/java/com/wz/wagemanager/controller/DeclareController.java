package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.ProcessEntity;
import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.tools.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WindowsTen
 * @date 2018/5/16 11:47
 * @description
 */
@RequestMapping("declare")
@RestController
public class DeclareController extends BaseExceptionController{
    @Resource
    private DeclareService declareService;

    @RequestMapping(value = "start.json")
    public PageBean startDeclare(
            @RequestParam(value = "declareId")String declareId
    ){
        declareService.start(declareId);
        return new PageBean<>();
    }

    @RequestMapping(value = "list.json")
    public PageBean findByUser(
            @RequestParam(value="curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE)Integer curPage,
            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE)Integer pageSize
    ){
        SysUser sysUser = ContextHolderUtils.getPrincipal ();
        Pageable pageable = PageUtil.pageable (curPage, pageSize, GlobalConstant.DEFAULT_SORT_ORDER, GlobalConstant.DEFAULT_DECLARE_SORT_FIELD);
        Page<SysDeclare> declarePage = declareService.findByUser (sysUser, pageable);
        return new PageBean<>(PageUtil.getPage (declarePage.getTotalElements (),pageSize,curPage),declarePage.getContent ());
    }

    @Resource
    private TaskService taskService;
    @RequestMapping(value = "task.json")
    public PageBean<List<SysDeclare>> queryTask(
            @RequestParam(value="curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE)Integer curPage,
            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE)Integer pageSize
    ){
        SysUser sysUser = ContextHolderUtils.getPrincipal ();
        String roleId = sysUser.getSysRole ().getId ();
        com.wz.wagemanager.tools.Page page = PageUtil.getPage (((Number) taskService.createTaskQuery ()
                .taskCandidateGroup (roleId).count ()).intValue (), pageSize, curPage);
        List<SysDeclare> declares = taskService.createTaskQuery ()
                .taskCandidateGroup (roleId).orderByTaskCreateTime ().asc ().listPage (page.getStart (), pageSize)
                .stream ().map (t -> {
                    SysDeclare sysDeclare = declareService.findByProcessInstanceId (t.getProcessInstanceId ());
                    sysDeclare.setTaskId (t.getId ());
                    return sysDeclare;
                }).collect (Collectors.toList ());
        return new PageBean<>(page,declares);
    }

    @Resource
    private IdentityService identityService;

    @PostMapping(value = "complete1.json")
    public PageBean completeTask(
            @RequestParam(value = "declareId") String declareId,
            @RequestParam(value = "comment",required = false) String comment,
            @RequestParam(value = "msg",required = false)Integer msg
    ){
        SysDeclare declare = declareService.findById (declareId);
        String processInstanceId = declare.getProcessInstanceId ();
        return completeTask (processInstanceId,null,comment,msg);
    }

    @PostMapping(value = "complete.json")
    public PageBean completeTask(
            @RequestParam(value = "processInstanceId") String processInstanceId,
            @RequestParam(value = "taskId")String taskId,
            @RequestParam(value = "comment",required = false) String comment,
            @RequestParam(value = "msg",required = false)Integer msg
    ){
        String roleId = ContextHolderUtils.getPrincipal ().getSysRole ().getId ();
        if(org.apache.commons.lang3.StringUtils.isBlank (taskId)){
            Task task = taskService.createTaskQuery ().processInstanceId (processInstanceId).taskCandidateGroup (roleId).singleResult ();
            taskId = task.getId ();
        }

        if(org.apache.commons.lang3.StringUtils.isNotBlank (comment)){
            identityService.setAuthenticatedUserId (ContextHolderUtils.getPrincipal ().getUsername ());
            taskService.addComment (taskId, processInstanceId,comment);
        }
        if(msg!=null){
            Map<String,Object> variables=new HashMap<> (1);
            variables.put ("msg",msg);
            taskService.complete (taskId,variables);
        }else{
            taskService.complete (taskId);
        }
        return new PageBean<>();
    }

    @Resource
    private HistoryService historyService;

    @PostMapping(value="comment1.json")
    public PageBean<List<ProcessEntity>> comment1(
            @RequestParam(value = "declareId") String declareId
    ){
        return comment (declareService.findById (declareId).getProcessInstanceId ());
    }

    @PostMapping(value="comment.json")
    public PageBean<List<ProcessEntity>> comment(
            @RequestParam(value = "processInstanceId") String processInstanceId
    ){
        List<ProcessEntity> processEntities = historyService.createHistoricActivityInstanceQuery ().processInstanceId (processInstanceId)
                .orderByHistoricActivityInstanceStartTime ().asc ().list ().stream ().map (instance -> {
                    ProcessEntity processEntity = ProcessEntity.builder ().id (instance.getId ())
                            .activityName (instance.getActivityName ())
                            .endTime (instance.getEndTime ()).startTime (instance.getStartTime ())
                            .processInstanceId (processInstanceId).build ();
                    List<Comment> taskComments = taskService.getTaskComments (instance.getTaskId ());
                    if (! CollectionUtils.isEmpty (taskComments)) {
                        Comment comment = taskComments.get (0);
                        processEntity.setAssignMsg (comment.getFullMessage ());
                        processEntity.setAssignUser (comment.getUserId ());
                    }
                    return processEntity;
                }).collect (Collectors.toList ());
        return new PageBean<> (processEntities);
    }

}
