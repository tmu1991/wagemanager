package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.ActTaskRepository;
import com.wz.wagemanager.dao.HiTaskRepository;
import com.wz.wagemanager.entity.*;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WindowsTen
 */
@Service("taskServiceImpl")
@Transactional (rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Resource
    private ActTaskRepository taskRepository;

    @Override
    public Page<ActTask> findByPage(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public ActTask findById (String id) {
        return taskRepository.findOne (id);
    }

    @Override
    public BigDecimal sumByTypeAndWorkNo (Integer type,String workNo) {
        return taskRepository.sumByTypeAndWorkNo (type,workNo);
    }

    @Override
    public List<ActTask> findByWorkNo (String workNo) {
        return taskRepository.findByWorkNo (workNo);
    }

    @Override
    public ActTask findByTaskDateAndWorkNoAndType(String taskDate, String workNo, Integer type) {
        return taskRepository.findByTaskDateAndWorkNoAndType(taskDate,workNo,type);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @OperInfo (type = OperationType.UPDATE,desc = "添加/修改员工扣款记录")
    public void save(ActTask task) {
        taskRepository.save(task);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @OperInfo (type = OperationType.UPDATE,desc = "添加/修改员工扣款记录")
    public void save(List<ActTask> tasks) {
        taskRepository.save(tasks);
    }

    @Resource
    private LogService logService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void upload (String filePath) throws Exception {
        Map<String,Object> argsMap=new HashMap<> (6);
        ExcelUtil.readForm (filePath).forEach (actForm -> this.oprForm (actForm,argsMap));
        if(argsMap.size ()>0){
            new LogUtils (logService).save (OperationType.UPDATE,argsMap);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void update (ActForm actForm) {
        Map<String,Object> argsMap=new HashMap<> (6);
        this.oprForm (actForm,argsMap);
        if(argsMap.size ()>0){
            new LogUtils (logService).save (OperationType.UPDATE,argsMap);
        }
    }

    @Override
    public List<ActTask> findByDeptId (String deptId) {
        return taskRepository.findByDeptId (deptId);
    }
    @OperInfo
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void charged (List<String> checkIds, List<String> unCheckIds,String declareId,String deptId) {
        if(!CollectionUtils.isEmpty (checkIds)){
            taskRepository.updateStatus (checkIds,0);
        }
        if(!CollectionUtils.isEmpty (checkIds)){
            taskRepository.updateStatus (unCheckIds,1);
        }
        actSalaryService.updateLoanStatus(declareId,deptId);
    }

    @Resource
    private ActSalaryService actSalaryService;
    @Resource
    private UserService userService;

    private Map<String, Object> oprForm(ActForm form,Map<String,Object> argsMap) {

        //根据工号取到用户
        SysUser user = userService.findByWorkNo(form.getWorkNo());
        Assert.assertNotNull("工号[" + form.getWorkNo() + "]的用户不存在", user);
        Assert.assertTrue("工号[" + form.getWorkNo() + "]与用户名不符，请核实", user.getUsername().equals(form.getUsername()));
        Assert.assertTrue("工号[" + form.getWorkNo() + "]部门与当前用户部门不符，请核实",
                user.getSysDept() != null && form.getDeptName().equals(user.getSysDept().getDeptName()));
        form.setDeptId (user.getSysDept ().getId ());

        //更新工资表的借款和扣款记录
        ActSalary salary = actSalaryService.findByWorkNo (form.getWorkNo());
        Assert.assertNotNull("工号[" + form.getWorkNo() + "]的用户工资记录不存在，请添加工资记录后再添加借款记录", salary);

        boolean isChange=false;
        if(form.getLate ()!=null&&form.getLate().compareTo(salary.getLate()) != 0){
            argsMap.put ("迟到/早退",form.getLate ());
            salary.setLate(form.getLate());
            isChange = true;
        }
        if(form.getDue() != null&&form.getDue().compareTo(salary.getPartyDue()) != 0){
            argsMap.put ("党费",form.getLate ());
            salary.setPartyDue(form.getDue());
            isChange = true;
        }
        if(form.getOther() != null&&form.getOther().compareTo(salary.getOther()) != 0){
            argsMap.put ("其他",form.getLate ());
            salary.setOther(form.getOther());
            isChange = true;
        }
        if(form.getOtherEl() != null&&form.getOtherEl().compareTo(salary.getOtherEl()) != 0){
            argsMap.put ("其他1",form.getLate ());
            salary.setOtherEl(form.getOtherEl());
            isChange = true;
        }

        List<ActTask> formTask = form.getTasks ();
        if(!CollectionUtils.isEmpty (formTask)){
            List<ActTask> taskList=new ArrayList<> ();
            boolean isTask=false;
            for(ActTask actTask:formTask){
                Assert.assertNotNull ("扣款金额不能为空",actTask.getAmount ());
                Assert.assertTrue ("扣款日期不能为空且不能超过当月", compareDate (actTask.getTaskDate ()));
                Assert.assertTrue ("扣款类型不能为空",actTask.getType ()!=null);
                actTask.setTaskDate (actTask.getTaskDate ().trim ());
                if(org.apache.commons.lang3.StringUtils.isNotBlank (actTask.getNote ())){
                    actTask.setNote (actTask.getNote ().trim ());
                }
                ActTask taskDateAndWorkNoAndType = findByTaskDateAndWorkNoAndType (actTask.getTaskDate (),form.getWorkNo (),actTask.getType ());
                if(taskDateAndWorkNoAndType!=null){
                    if(taskDateAndWorkNoAndType.getAmount ().compareTo (actTask.getAmount ())==0
                            &&conNote (taskDateAndWorkNoAndType.getNote (),actTask.getNote ())){
                        continue;
                    }
                    taskDateAndWorkNoAndType.setAmount (actTask.getAmount ());
                    taskDateAndWorkNoAndType.setNote (actTask.getNote ());
                    actTask=taskDateAndWorkNoAndType;
                }else{
                    actTask.setDeptId (form.getDeptId ());
                    actTask.setDeptName (form.getDeptName ());
                    actTask.setUsername (form.getUsername ());
                    actTask.setWorkNo (form.getWorkNo ());
                }
                if(actTask.getType ()==0){
                    Object obj = argsMap.get ("借款");
                    if(obj == null){
                        obj = new ArrayList<Map<String,Object>> ();
                        argsMap.put ("借款",obj);
                    }
                    List<Map<String,Object>> loanList= (List<Map<String, Object>>) obj;
                    loanList.add (getLoanMap (actTask));
                }else{
                    Object obj = argsMap.get ("其他扣款");
                    if(obj == null){
                        obj = new ArrayList<Map<String,Object>> ();
                        argsMap.put ("其他扣款",obj);
                    }
                    List<Map<String,Object>> otherList= (List<Map<String, Object>>) obj;
                    otherList.add (getLoanMap (actTask));
                }
                if(actTask != null && actTask.getAmount ().compareTo (BigDecimal.ZERO) == 0){
                    taskRepository.delete (actTask);
                }else{
                    actTask.setStatus (0);
                    taskList.add (actTask);
                }
                isTask=true;
            }
            if(isTask){
                isChange=true;
                taskRepository.save (taskList);
                BigDecimal bigDecimal = sumByTypeAndWorkNo (1,form.getWorkNo ());
                salary.setOtherDebit (bigDecimal==null?BigDecimal.ZERO:bigDecimal);
                BigDecimal decimal = sumByTypeAndWorkNo (0,form.getWorkNo ());
                salary.setLoan (decimal==null?BigDecimal.ZERO:decimal);
            }
        }
        if (isChange) {
            CommonUtils.calSalary(salary, null, DateUtil.getDateNum (salary.getYear (),salary.getMonth ()));
            actSalaryService.save (salary);
        }
        return argsMap;
    }

    private static boolean compareDate(String taskDate){
        try {
            return new SimpleDateFormat ("yyyy-MM-dd").parse (taskDate).compareTo (new Date ())<0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main (String[] args) {
        System.out.println (compareDate (null));
    }

    private Map<String,Object> getLoanMap(ActTask actTask){
        Map<String,Object> loanMap=new HashMap<> (4);
        loanMap.put ("员工名称",actTask.getUsername ());
        loanMap.put ("考勤编号",actTask.getWorkNo ());
        loanMap.put ("金额",actTask.getAmount ());
        loanMap.put ("日期",actTask.getTaskDate ());
        return loanMap;
    }

    private boolean conNote(String note1,String note2){
        if(org.apache.commons.lang3.StringUtils.isBlank (note1)
                &&org.apache.commons.lang3.StringUtils.isNotBlank (note2)){
            return false;
        }else if(org.apache.commons.lang3.StringUtils.isBlank (note2)
                &&org.apache.commons.lang3.StringUtils.isNotBlank (note1)){
            return false;
        }else if(org.apache.commons.lang3.StringUtils.isBlank (note1)
                &&org.apache.commons.lang3.StringUtils.isBlank (note2)){
            return true;
        }
        return note1.equals (note2);
    }


    @Resource
    private HiTaskRepository hiTaskRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void saveHi (List<HiTask> tasks) {
        hiTaskRepository.save (tasks);
    }

    @Override
    public List<ActTask> findByStatus (Integer status,String deptId) {
        return taskRepository.findByStatusAndDeptId (status,deptId);
    }

    @Override
    public void deleteByStatus (Integer status,String deptId) {
        taskRepository.deleteByStatusAndDeptId (status,deptId);
    }
}
