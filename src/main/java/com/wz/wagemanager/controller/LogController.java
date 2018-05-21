package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysLog;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.tools.BaseExceptionController;
import com.wz.wagemanager.tools.PageBean;
import com.wz.wagemanager.tools.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@Controller
public class LogController extends BaseExceptionController {
    @Resource
    private LogService logService;
    @PostMapping("diary.html")
    public String listTask(
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
            @RequestParam(value = "curPage",defaultValue = "1") int curPage,
            Model model
    ){
        PageRequest pageRequest = new PageRequest (curPage-1,pageSize,getDefaultSort ());
        Page<SysLog> logPage = logService.findByPage (pageRequest);
        model.addAttribute ("logs",logPage.getContent ());
        model.addAttribute ("page", PageUtil.getPage (((Number)logPage.getTotalElements ()).intValue (),pageSize,curPage));
        return "diary";
    }

    private Sort getDefaultSort(){
        return new Sort(Sort.Direction.DESC,"createTime");
    }
}
