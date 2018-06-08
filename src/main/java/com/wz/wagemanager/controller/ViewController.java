package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.Assert;
import com.wz.wagemanager.tools.ContextHolderUtils;
import com.wz.wagemanager.tools.PageBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("salary.html")
    public String salaryHistory(
            Model model
    ){
        SysUser sysUser = ContextHolderUtils.getPrincipal ();
        model.addAttribute("dept",sysUser.getSysDept ());
        return "salary";
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

    @Resource
    private UserService userService;

    @PostMapping ("newpsd.html")
    public String newPassword(
            @RequestParam (value = "password")String password,
            @RequestParam(value = "newPassword")String newPassword,
            @RequestParam(value = "repeatPsd")String repeatPsd
    ){
        String userId=ContextHolderUtils.getPrincipal ().getId ();
        SysUser user = userService.getUserById (userId);
        Assert.assertNotNull ("用户不存在",user);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
        Assert.assertTrue ("原始密码错误",password!=null&&passwordEncoder.matches(password,user.getPassword()));
        Assert.assertTrue ("两次密码不相等",newPassword.equals (repeatPsd));
        user.setPassword (passwordEncoder.encode (newPassword));
        userService.updateUser (user);
        return "redirect:logout.html";
    }
}
