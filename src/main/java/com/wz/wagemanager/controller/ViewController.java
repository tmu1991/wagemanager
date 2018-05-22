package com.wz.wagemanager.controller;

import com.wz.wagemanager.service.DeptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

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
}
