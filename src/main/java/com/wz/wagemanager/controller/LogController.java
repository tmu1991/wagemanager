package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysLog;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.tools.BaseExceptionController;
import com.wz.wagemanager.tools.GlobalConstant;
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
import java.util.List;

/**
 * @author WindowsTen
 */
@RestController
@RequestMapping("log")
public class LogController extends BaseExceptionController {
    @Resource
    private LogService logService;
    @PostMapping("list.json")
    public PageBean<List<SysLog>> listLog(
            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage
    ){
        Page<SysLog> logPage = logService.findByPage (PageUtil.pageable (curPage,pageSize,GlobalConstant.DEFAULT_SORT_ORDER,DEFAULT_SORT_FIELD));
        return new PageBean<> (PageUtil.getPage (logPage.getTotalElements (),pageSize,curPage),logPage.getContent ());
    }

    private static final String DEFAULT_SORT_FIELD="createTime";
}
