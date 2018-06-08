package com.wz.wagemanager.tools;

import com.alibaba.fastjson.JSONObject;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.annotation.ParmDesc;
import com.wz.wagemanager.entity.SysLog;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.LogService;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WindowsTen
 * @date 2018/6/1 9:35
 * @description
 */

public class LogUtils {

    private LogService logService;

    public LogUtils(LogService logService){
        this.logService=logService;
    }

    public static OperationType getLog(String id){
        if(StringUtils.isBlank (id)){
            return OperationType.ADD;
        }
        return OperationType.UPDATE;
    }

    public static String paramToStr(Object obj){
        return JSONObject.toJSONString(obj);
    }

    public static <T> String updateParams(T c1, T c2) throws IllegalAccessException {
        Map<String,Object> params=new HashMap<> ();
        Field[] declaredFields = c1.getClass ().getDeclaredFields ();
        Object o;
        for(Field field:declaredFields){
            field.setAccessible (true);
            if(field.isAnnotationPresent (ParmDesc.class)){
                o = field.get (c2);
                if(!field.get (c1).equals (o)){
                    params.put (field.getAnnotation (ParmDesc.class).desc (),o);
                }
            }
        }
        return paramToStr (params);
    }

    public void save(OperationType type,Object args){
        save (type,args,null);
    }
    public void save(OperationType type,Object args,Object operNames){
        try{
            SysUser sysUser = ContextHolderUtils.getPrincipal ();
            SysLog log = SysLog.builder ().startTime (sysUser.getLoginTime ()).operation (type.getType ())
                    .username (sysUser.getUsername ()).workNo (sysUser.getWorkNo ()).createTime (new Date ()).build ();
            log.setEndTime (new Date ());
            if(args!=null && !"".equals (args)){
                log.setArgs (paramToStr (args));
            }
            if(operNames !=null &&!"".equals (operNames)){
                log.setOperName (paramToStr (operNames));
            }
            this.logService.save (log);
        }catch (Exception e){
            e.printStackTrace ();
        }
    }

}
