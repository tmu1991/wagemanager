package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.ActForm;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 待办事项
 *
 * @author WindowsTen
 */
@RestController
@RequestMapping("task")
public class TaskController extends BaseExceptionController {

    @Autowired
    private TaskService taskService;

    @PostMapping("list.json")
    public PageBean<? extends Object> listTask(
            @RequestParam(value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage
    ) {
            Page<ActTask> taskPage = taskService.findByPage(PageUtil.pageable(curPage, pageSize, GlobalConstant.DEFAULT_SORT_ORDER, DEFAULT_SORT_FIELD));
            return new PageBean<>(PageUtil.getPage(taskPage.getTotalElements(), pageSize, curPage), taskPage.getContent());
    }

    @PostMapping("upload.json")
    public PageBean upload(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = UploadUtils.fileVerify(file);
        String filePath = DataUtil.getFilePath(originalFilename);
        File dest = DataUtil.getFile(filePath);
        //文件上传
        file.transferTo(dest);
        taskService.upload (filePath);
        return new PageBean<> ();
    }

    @PostMapping("update.json")
    public PageBean update(@ModelAttribute ActForm form) {
        taskService.update (form);
        return new PageBean<>();
    }

    @PostMapping("salary.json")
    public PageBean<List<ActTask>> update(@RequestParam("workNo")String workNo) {
        return new PageBean<>(taskService.findByWorkNo (workNo));
    }

    @PostMapping("dept.json")
    public PageBean<List<ActTask>> dept(@RequestParam("deptId")String deptId) {
        return new PageBean<>(taskService.findByDeptId (deptId));
    }

    @PostMapping("charged.json")
    public PageBean charged(
            @RequestParam(value = "unCheckIds",required = false)List<String> unCheckIds,
            @RequestParam(value = "checkIds",required = false)List<String> checkIds
    ) {
        taskService.charged(checkIds,unCheckIds);
        return new PageBean<>();
    }

    @RequestMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/xls/loanTemplate.xls");
        try (OutputStream toClient = new BufferedOutputStream (response.getOutputStream ());
             BufferedInputStream fis = new BufferedInputStream (new FileInputStream (file))) {
            byte[] buffer = new byte[fis.available ()];
            fis.read (buffer);
            // 清空response
            response.reset ();
            response.setCharacterEncoding ("utf-8");
            response.setContentType ("application/octet-stream");
            //防止文件名乱码
            String fileName=new String("扣款模板.xls".getBytes(),"ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            toClient.write (buffer);
            toClient.flush ();
        } catch (IOException ex) {
            ex.printStackTrace ();
        }

    }




//    private static final String[] TASK_PROPERTIES = new String[]{"deptName", "workNo", "username", "late", "due", "other", "otherEl", "tasks[0].amount","tasks[0].type", "tasks[0].taskDate", "tasks[0].note"};

    private static final String DEFAULT_SORT_FIELD = "taskDate";

}
