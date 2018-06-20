package com.wz.wagemanager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.ActSalaryRepository;
import com.wz.wagemanager.dao.DeclareRepository;
import com.wz.wagemanager.entity.*;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WindowsTen
 * @date 2018/5/16 10:50
 * @description
 */
@Service("actSalaryService")
@Transactional(rollbackFor = Exception.class)
public class ActSalaryServiceImpl implements ActSalaryService {
    @Resource
    private ActSalaryRepository actSalaryRepository;

    @Resource
    private LogService logService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (ActSalary salary) {
//        SysLog log = LogUtils.getLog (salary.getId ());
        actSalaryRepository.save (salary);
//        LogUtils.save (logService,log,salary);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (List<ActSalary> salaries) {
        actSalaryRepository.save (salaries);
    }

    @Override
    public Page<ActSalary> findByDeptId(String deptId, Pageable pageable) {
        return actSalaryRepository.findByDeptId(deptId,pageable);
    }


    @Override
    public ActSalary findByYearAndMonthAndWorkNo (int year, int month, String workNo) {
        return actSalaryRepository.findByYearAndMonthAndWorkNo (year,month,workNo);
    }

    @Override
    public ActSalary findByWorkNo (String workNo) {
        return actSalaryRepository.findByWorkNo (workNo);
    }

    @Override
    public List<SalaryArea> findByGroupDept (List<String> ids) throws Exception {
        return actSalaryRepository.findGroupByDept (ids);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void removeByIdIn (String[] ids) {
        List<Map<String, Object>> mapList = Arrays.stream (ids).filter (StringUtils::isNotBlank)
                .map (id -> getOperMap (findById (id))).collect (Collectors.toList ());
        actSalaryRepository.removeByIdIn (ids);
        new LogUtils (logService).save (OperationType.DELETE,null,mapList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void update (ActSalary salary) throws IllegalAccessException {
        ActSalary actSalary = findById(salary.getId());
        SysUser sysUser = userService.findByWorkNo (actSalary.getWorkNo ());
        boolean userUpdate=false;
        if(salary.getAllowance ()!=null&&salary.getAllowance ().compareTo (BigDecimal.ZERO)!=0
                &&salary.getAllowance ().compareTo (sysUser.getAllowance ())!=0){
            sysUser.setAllowance (salary.getAllowance ());
            userUpdate=true;
            salary.setAllowance (getAllowance (salary.getAllowance (),getAttendance (actSalary.getAttendance (),salary.getRepairWork ())));
        }
        if(salary.getSeniority ()!=null&&salary.getSeniority ().compareTo (BigDecimal.ZERO)!=0
                &&salary.getSeniority ().compareTo (sysUser.getSeniority ())!=0){
            sysUser.setSeniority (salary.getSeniority ());
            userUpdate=true;
        }
        if(userUpdate){
            userService.updateUser (sysUser,false);
        }
        Map<String, Object> properties = CommonUtils.copyProperties (salary, actSalary, updateProperties);
        if(properties != null){
            CommonUtils.calSalary (salary,null, DateUtil.getDateNum (salary.getYear (),salary.getMonth ()));
            actSalaryRepository.save (salary);
            new LogUtils (logService).save (OperationType.UPDATE,properties,getOperMap (actSalary));
        }
    }
    private BigDecimal getAttendance(BigDecimal attendance,BigDecimal repairWork){
        if(repairWork==null){
            return attendance;
        }
        if(attendance == null){
            return repairWork;
        }
        return repairWork.add (attendance);
    }

    private BigDecimal getAllowance(BigDecimal allowance,BigDecimal attendance){
        if(attendance.compareTo (new BigDecimal ("25"))<0){
            return BigDecimal.ZERO;
        }
        return allowance.multiply(attendance).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public ActSalary findById (String id) {
        return actSalaryRepository.findOne (id);
    }

    @Override
    public List<ActSalary> findByDeclareId (String declareId) {
        return actSalaryRepository.findByDeclareId(declareId);
    }

    @Override
    public void deleteByDeclareId (String declareId) {
        actSalaryRepository.deleteByDeclareId (declareId);
    }

    @Override
    public void updateLoanStatus (String declareId, String deptId) {
        actSalaryRepository.updateLoanStatus (declareId,deptId);
    }

    @Override
    public Integer findLoanStatus (String declareId, String deptId) {
        return actSalaryRepository.findLoanStatus (declareId,deptId);
    }

    private static final List<String> updateProperties= Arrays.asList ("coeff","repairWork","base","seniority","busTravel","subDay","allowance","bonus");

    private Map<String,Object> getOperMap(ActSalary actSalary){
        Map<String,Object> map= new HashMap<> (4);
        map.put ("年份",actSalary.getYear ());
        map.put ("月份",actSalary.getMonth ());
        map.put ("员工姓名",actSalary.getUsername ());
        map.put ("工号",actSalary.getWorkNo ());
        return map;
    }

    @Resource
    private DeclareService declareService;
    @Resource
    private UserService userService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void upload(String filePath,String dateStr) throws Exception {
        SysUser sessionUser = ContextHolderUtils.getPrincipal ();
        SysDept sysDept = sessionUser.getSysDept ();
        Date date = DateUtil.getDate (dateStr);
        Integer year = DateUtil.getYear (date);
        Integer month = DateUtil.getMonth (date);
        int dateNum = DateUtil.getDateNum (year, month);
        List<ActSalary> saveList = new ArrayList<> ();
        SysDeclare declare = declareService.findModifiable (sysDept);
        String declareName = declareName (year, month, sysDept.getDeptName ());
        if(declare == null){
            declare = SysDeclare.builder ()
                    .declareName (declareName)
                    .user (sessionUser).dept (sysDept).status (0).build ();
            declareService.save (declare);
        }else{
            Assert.assertTrue (declare.getDeclareName ()+"尚未审核完成,请完成后再提交",declareName.equals (declare.getDeclareName ()));
        }
        List<ActWork> actWorks = UploadUtils.actList (filePath);
        for (ActWork actWork : actWorks) {
            SysUser sysUser = userService.findByWorkNo (actWork.getWorkNo ());
            Assert.assertNotNull ("工号为[" + actWork.getWorkNo () + "]的员工不存在,请联系管理员添加后重试", sysUser);
            Assert.assertTrue ("部门[" + actWork.getDeptName () + "]与当前用户部门不符", sysDept.getDeptName ().equals (actWork.getDeptName ()));
            ActSalary actSalary = actSalaryRepository.findByYearAndMonthAndWorkNo (year, month, sysUser.getWorkNo ());
            if (actSalary == null) {
                actSalary = new ActSalary ();
                actSalary.setMonth (month);
                actSalary.setYear (year);
                actSalary.setDeclareId (declare.getId ());
            }
            Assert.assertTrue ("考勤不能为空",StringUtils.isNotBlank (actWork.getReality ()));
            actSalary.setAttendance (new BigDecimal (actWork.getReality ()));
            actSalary.setAllowance (getAllowance (sysUser.getAllowance (),actSalary.getAttendance ()));
            CommonUtils.calSalary (actSalary, sysUser, dateNum);
            saveList.add (actSalary);
        }
        actSalaryRepository.save (saveList);
    }

    private String declareName (int year, int month, String deptName) {
        return year + "年" + month + "月" + deptName + "工资申请";
//        return deptName + "("+year + "-" + month +")";
    }
}
