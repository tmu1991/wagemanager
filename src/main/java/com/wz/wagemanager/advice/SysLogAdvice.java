package com.wz.wagemanager.advice;

import com.alibaba.fastjson.JSONObject;
import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.annotation.ParmDesc;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SysLog;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.ContextHolderUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author WindowsTen
 *  日志切面 只记录修改
 */
@Aspect
@Component
public class SysLogAdvice {

    @Around ("execution(* com.wz.wagemanager.service.impl.*.*(..)) && @annotation(annotation)")
    public Object doAroundMethod (ProceedingJoinPoint joinPoint, OperInfo annotation) throws Throwable {
        OperationType type = annotation.type ();
        Object[] args = joinPoint.getArgs ();
        SysUser sysUser = ContextHolderUtils.getPrincipal ();
        SysLog sysLog = SysLog.builder ().startTime (sysUser.getLoginTime ()).operation (type.getType ())
                .username (sysUser.getUsername ()).createTime (new Date ()).build ();
        executionTemplate (annotation, args, sysLog);
        Object proceed = joinPoint.proceed ();
        logService.save (sysLog);
        return proceed;
    }

    @Resource
    private LogService logService;
    @Resource
    private UserService userService;
    @Resource
    private ActSalaryService actSalaryService;

    private void executionTemplate (OperInfo annotation, Object[] args, SysLog sysLog) throws IllegalAccessException {
        OperationType type = annotation.type ();
        List<Map<String,Object>> list=new ArrayList<> ();
        try {
            for (Object obj : args) {
                if (type.equals (OperationType.DELETE)) {
                    List<String> ids = (List<String>) obj;
                    ids.forEach (id -> {
                        ActSalary salary = actSalaryService.findById (id);
                        SysUser user = userService.getUserById (salary.getUserId ());
                        Map<String,Object> map= new HashMap<> ();
                        map.put ("年份",salary.getYear ());
                        map.put ("月份",salary.getMonth ());
                        map.put ("员工姓名",user.getUsername ());
                        map.put ("工号",user.getWorkNo ());
                        list.add (map);
                    });
                    sysLog.setOperName (JSONObject.toJSONString (list));
                } else if (type.equals (OperationType.UPDATE)) {
                    ActSalary actSalary = (ActSalary) obj;
                    ActSalary salary = actSalaryService.findById (actSalary.getId ());
                    SysUser user = userService.getUserById (salary.getUserId ());
                    Map<String,Object> map= new HashMap<> (4);
                    map.put ("年份",salary.getYear ());
                    map.put ("月份",salary.getMonth ());
                    map.put ("员工姓名",user.getUsername ());
                    map.put ("工号",user.getWorkNo ());
                    sysLog.setOperName (JSONObject.toJSONString (map));
                    sysLog.setArgs (getArgsString (actSalary, salary));
                }
            }
        } catch (Exception e) {
            if (e instanceof ClassCastException) {
                sysLog.setArgs (annotation.desc ());
            } else {
                e.printStackTrace ();
            }

        }
    }

    private String getArgsString (ActSalary actSalary, ActSalary salary) throws IllegalAccessException {
        Map<String, Object> argsMap = new HashMap<> ();
        Field[] declaredFields = ActSalary.class.getDeclaredFields ();
        for (Field field : declaredFields) {
            field.setAccessible (true);
            Object o = field.get (actSalary);
            if (o != null) {
                ParmDesc annotation = field.getAnnotation (ParmDesc.class);
                if (annotation != null) {
                    Object o1 = field.get (salary);
                    BigDecimal bigDecimal = (BigDecimal) o;
                    BigDecimal bigDecimal1 = (BigDecimal) o1;
                    if (bigDecimal.compareTo (bigDecimal1) != 0) {
                        argsMap.put (annotation.desc (), o);
                    }
                }
            }
        }
        if (! argsMap.isEmpty ()) {
            return JSONObject.toJSONString (argsMap);
        }
        return "";
    }
}
