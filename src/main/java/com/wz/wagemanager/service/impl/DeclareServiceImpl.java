package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.DeclareRepository;
import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.exception.HandThrowException;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.tools.ContextHolderUtils;
import com.wz.wagemanager.tools.GlobalConstant;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/9 18:03
 * @description
 */
@Service("declareService")
@Transactional(rollbackFor = Exception.class)
public class DeclareServiceImpl implements DeclareService {
    @Resource
    private DeclareRepository declareRepository;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;

    @Override
    public void updateProperty (String key, Object value,String id) {
        declareRepository.updateByProperty (key,value,id);
    }

    @Override
    public void save (SysDeclare declare) {
        declareRepository.save (declare);
    }

    @Override
    public Page<SysDeclare> findByUser (SysUser user, Pageable page) {
        return declareRepository.findByUser (user,page);
    }

    @Override
    public void start (String declareId) {
        try{
            identityService.setAuthenticatedUserId (ContextHolderUtils.getPrincipal ().getSysRole ().getId ());
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey (GlobalConstant.PROCESS_KEY, declareId);
            SysDeclare declare = declareRepository.findOne (declareId);
            declare.setStatus (1);
            declare.setProcessInstanceId (processInstance.getId ());
            declare.setDeclareDate (new Date ());
            declareRepository.saveAndFlush (declare);
        }finally {
            identityService.setAuthenticatedUserId (null);
        }

    }

    @Override
    public SysDeclare findByProcessInstanceId (String processInstanceId) {
        return declareRepository.findByProcessInstanceId (processInstanceId);
    }

    @Override
    public SysDeclare findById (String id) {
        return declareRepository.findOne (id);
    }

    @Override
    public List<SysDeclare> findByDeptAndStatus (SysDept dept,int stauts) {
        return declareRepository.findByDeptAndStatus (dept,stauts);
    }

    @Override
    public SysDeclare findNotStart (SysDept dept) {
        List<SysDeclare> declares = findByDeptAndStatus (dept, 0);
        if(CollectionUtils.isEmpty (declares)){
            return null;
        }else if(declares.size ()==1){
            return declares.get (0);
        }
        throw new HandThrowException ();
    }

    private static final Integer[] declareNotComplete=new Integer[]{2,3};
    @Override
    public List<SysDeclare> findNotComplete (SysDept sysDept) {
        return declareRepository.findByDeptAndStatusIn (sysDept,declareNotComplete);
    }


}
