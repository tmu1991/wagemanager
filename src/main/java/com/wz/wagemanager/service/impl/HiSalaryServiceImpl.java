package com.wz.wagemanager.service.impl;


import com.wz.wagemanager.dao.HiSalaryRepository;
import com.wz.wagemanager.dao.TaskRepository;
import com.wz.wagemanager.dao.UserRepository;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SalaryArea;
import com.wz.wagemanager.service.HiSalaryService;
import com.wz.wagemanager.tools.CommonUtils;
import com.wz.wagemanager.tools.CriteriaUtils;
import com.wz.wagemanager.tools.GlobalConstant;
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
import javax.persistence.criteria.Predicate;
import java.util.*;

@Service("hiSalaryService")
@Transactional
public class HiSalaryServiceImpl implements HiSalaryService {

    @Resource
    private HiSalaryRepository salaryRepository;

    @Override
    public void save(HiSalary salary) {
        salaryRepository.save(salary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public void mutilSave(List<HiSalary> saveList) {
//        salaryRepository.batchInsert(saveList);
        salaryRepository.save (saveList);
    }



    @Override
    public List<HiSalary> findByDeptIdAndDete (String deptId,int year,int month,Pageable page) {
        return salaryRepository.findByDeptIdAndYearAndMonth (deptId,year,month,page);
    }

    @Override
    public Integer countByDeptIdAndDate (String deptId,int year,int month) {
        return salaryRepository.countByDeptIdAndDate (deptId,year,month);
    }

    @Override
    public List<HiSalary> findByYearAndMonth (int year, int month, Pageable page) {
        return salaryRepository.findHiSalariesByYearAndMonth (year,month,page);
    }

    @Override
    public Integer countByYearAndMonth (int year, int month) {
        return salaryRepository.countByYearAndMonth (year,month);
    }

    @Override
    public Page<HiSalary> findByWorkNoOrUsername(String workNo,String username, Pageable page){
        Map<String,Object> speMap=new HashMap<> (2);
        speMap.put ("workNo",workNo);
        speMap.put ("username",username);
        return salaryRepository.findAll(CriteriaUtils.getSpe (speMap), page);
    }

//    @Override
//    public Integer countByWorkNoOrUsername (String workNo,String username) {
//        return ((Number)salaryRepository.count(getSpe(workNo,username))).intValue();
//    }

//    private Specification<HiSalary> getSpe(String workNo, String username){
//        return  (root, criteriaQuery, criteriaBuilder) -> {
//            List<Predicate> list = new ArrayList<> ();
//            if(StringUtils.isNotBlank (workNo)){
//                list.add (criteriaBuilder.equal(root.get("workNo").as(String.class),workNo));
//            }
//            if(StringUtils.isNotBlank (username)){
//                list.add (criteriaBuilder.equal(root.get("username").as(String.class),username));
//            }
//            Predicate[] p = new Predicate[list.size()];
//            criteriaQuery.where(criteriaBuilder.and(list.toArray(p)));
//            return criteriaQuery.getRestriction();
//        };
//    }

    @Override
    public HiSalary findByYearAndMonthAndUserId (int year, int month, String userId) {
        return salaryRepository.findByYearAndMonthAndUserId (year,month,userId);
    }

    @Override
    public List<SalaryArea> findByGroupDept (int year, int month) throws Exception {
        return salaryRepository.findByGroupDept (year,month);
//        return CommonUtils.castEntity(salaryRepository.findByGroupDept (year, month),HiSalary.class, GlobalConstant.properties);
    }

    @Override
    public Integer getMaxYear() {
        return salaryRepository.getMaxYear();
    }

    @Override
    public Integer getMaxMonth(int maxYear) {
        return salaryRepository.getMaxMonth(maxYear);
    }

    @Override
    public List<HiSalary> findByDeptIdAndYearAndMonth(String deptId, int year, int month) {
        return salaryRepository.findByDeptIdAndYearAndMonth(deptId,year,month);
    }

//    @Override
//    @OperInfo(type = OperationType.DELETE,desc = "根据年月删除工资信息")
//    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
//    public void removeByIdIn(List<String> ids){
//        salaryRepository.removeByIdIn(ids);
//    }

    @Resource
    private UserRepository userRepository;

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
//    @OperInfo(type = OperationType.UPDATE,desc = "更新员工工资信息")
//    public void update(HiSalary hiSalary) throws IllegalAccessException {
//        HiSalary salary = salaryRepository.findById(hiSalary.getId());
//        CommonUtils.copyNullValue(hiSalary,salary);
//        updateUser(salary,hiSalary);
//        addTask(hiSalary);
//        CommonUtils.setSalary(hiSalary,null,null, DateUtil.getDateNum(hiSalary.getYear(),hiSalary.getMonth()));
//        salaryRepository.save(hiSalary);
//    }

//    private void updateUser(HiSalary salary,HiSalary hiSalary ){
//        if(hiSalary.getBase()!=null&&salary.getBase().compareTo(hiSalary.getBase())!=0||
//                hiSalary.getSeniority()!=null&&salary.getSeniority().compareTo(hiSalary.getSeniority())!=0||
//                hiSalary.getWorkNo()!=null&&!salary.getWorkNo().equals(hiSalary.getWorkNo())||
//                hiSalary.getCreditCard()!=null&&!salary.getCreditCard().equals(hiSalary.getCreditCard())){
//            SysUser user = userRepository.getUserById(salary.getUserId());
//            user.setBase(hiSalary.getBase());
//            user.setSeniority(hiSalary.getSeniority());
//            user.setWorkNo(hiSalary.getWorkNo());
//            user.setCreditCard(hiSalary.getCreditCard());
//            userRepository.save(user);
//        }
//    }

    @Resource
    private TaskRepository taskRepository;

    private void addTask(ActSalary actSalary){
        ActTask actTask=taskRepository.findByYearAndMonthAndWorkNo(actSalary.getYear(),actSalary.getMonth(),actSalary.getWorkNo());
        if(CommonUtils.isNotBlank(actSalary.getLate())||CommonUtils.isNotBlank(actSalary.getOtherDebit())||CommonUtils.isNotBlank(actSalary.getLoan())||
                CommonUtils.isNotBlank(actSalary.getPartyDue())||CommonUtils.isNotBlank(actSalary.getOther())||CommonUtils.isNotBlank(actSalary.getOtherEl())){
            if(actTask==null){
                actTask=ActTask.builder().createDate(new Date()).year(actSalary.getYear()).month(actSalary.getMonth())
                        .username(actSalary.getUsername()).deptName(actSalary.getDeptName()).workNo(actSalary.getWorkNo())
                        .status(1).debit(CommonUtils.getDebit(actSalary)).loan(actSalary.getLoan()).build();
            }else{
                actTask.setLoan(actSalary.getLoan());
                actTask.setDebit(CommonUtils.getDebit(actSalary));
            }
        }
        if(actTask!=null){
            taskRepository.save(actTask);
        }
    }


}
