package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.security.CustomUsernamePasswordToken;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.tools.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("dept")
public class DeptController extends BaseExceptionController {
    @Resource
    private DeptService deptService;

    @GetMapping("all.json")
    public PageBean<List<SysDept>> findAll(
    ){
        return new PageBean<> (deptService.findAll());
    }

    @PostMapping("power.json")
    public PageBean<Object> selectById(){
        SysUser sysUser = ContextHolderUtils.getPrincipal ();
//        sysUser.setLoginTime (new Date());
//        userService.updateUser (sysUser);
        SysRole sysRole = sysUser.getSysRole ();
        switch (sysRole.getRoleAlias ()){
            case "ROLE_DIRE":
            case "ROLE_MANAGER":
            case "ROLE_STAT":
                return new PageBean<>(deptService.findById(sysUser.getSysDept().getId()));
            default:
                return new PageBean<>(deptService.findAll());
        }
    }

//    @PostMapping(value = "insert.json")
//    public PageBean insertDept(@ModelAttribute SysDept dept){
//        deptService.save (dept);
//        return new PageBean<>();
//    }
//
//    @PostMapping(value = "delete.json")
//    public PageBean deleteById(@RequestParam(value = "deptId") String deptId){
//        deptService.deleteById (deptId);
//        return new PageBean<>();
//    }
//
//    @PostMapping(value = "list.json")
//    public PageBean<List<SysDept>> findByPage(
//            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
//            @RequestParam(value = "curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage
//    ){
//        Page<SysDept> deptPage = deptService.findByPage(PageUtil.pageable (curPage,pageSize));
//        com.wz.wagemanager.tools.Page page = PageUtil.getPage (deptPage.getTotalElements(), pageSize, curPage);
//        return new PageBean<>(page,deptPage.getContent());
//    }
}
