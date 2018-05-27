package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.tools.ContextHolderUtils;
import com.wz.wagemanager.tools.PageBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author WindowsTen
 */
@Controller
public class ViewController {

    @Resource
    private DeptService deptService;
    @RequestMapping("index/{deptId}")
    public String index(
            @PathVariable("deptId")String deptId,Model model
    ){
        model.addAttribute("dept",deptService.findById(deptId));
        return "index";
    }

    @RequestMapping("home.html")
    public String home(Model model){
        SysUser sysUser = ContextHolderUtils.getPrincipal ();
        SysRole sysRole = sysUser.getSysRole ();
        switch (sysRole.getRoleAlias ()){
            case "ROLE_DIRE":
            case "ROLE_MANAGER":
            case "ROLE_STAT":
                model.addAttribute ("depts", Collections.singleton (deptService.findById(sysUser.getSysDept().getId())));
                break;
            default:
                model.addAttribute ("depts",deptService.findAll());
                break;
        }
        return "home";
    }
}
