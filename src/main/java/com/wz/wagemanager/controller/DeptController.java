package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.tools.BaseExceptionController;
import com.wz.wagemanager.tools.PageBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
