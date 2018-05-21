package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.tools.BaseExceptionController;
import com.wz.wagemanager.tools.PageBean;
import com.wz.wagemanager.tools.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/8 12:51
 * @description
 */
@RestController
@RequestMapping("role")
public class RoleController extends BaseExceptionController {

    @Resource
    private RoleService roleService;

    @RequestMapping("all.json")
    public PageBean<List<SysRole>> findAll(){
        return new PageBean(roleService.findAll());
    }
//    @RequestMapping(value = "list.html")
//    public String findByPage(
//            @RequestParam(value="curPage",defaultValue = "1")Integer curPage,
//            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
//            ModelAndView modelAndView
//    ){
//        Page<SysRole> rolePage = roleService.findByPage(PageUtil.pageable (curPage,pageSize));
//        modelAndView.addObject ("page",PageUtil.getPage (((Number)rolePage.getTotalElements ()).intValue (),pageSize,curPage));
//        modelAndView.addObject ("roles", rolePage.getContent ());
//        return "role";
//    }

}
