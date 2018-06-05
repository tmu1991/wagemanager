package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseExceptionController {

    @Resource
    private UserService userService;

    @PostMapping("newpsd.json")
    public PageBean newPassword(
            @RequestParam(value = "password")String password,
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
        return new PageBean<> ();
    }

    @PostMapping("list.json")
    public PageBean<List<SysUser>> listUser(
            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage,
            @RequestParam(value = "deptId",required = false)String deptId,
            @RequestParam(value = "roleId",required = false)String roleId,
            @RequestParam(value = "username",required = false)String username
    ){
        Pageable pageRequest = PageUtil.pageable (curPage,pageSize);
        org.springframework.data.domain.Page<SysUser> userPage = userService.findByPage(deptId, roleId,username, pageRequest);
        Page page = PageUtil.getPage (userPage.getTotalElements(), pageSize, curPage);
        return new PageBean<>(page,userPage.getContent());
    }

    @PostMapping("update.json")
    public PageBean updateById(@ModelAttribute SysUser user) throws IllegalAccessException {
        userService.updateUserByProperties (user);
        return new PageBean<>();
    }

    @PostMapping("delete.json")
    public PageBean removeByIds(
            @RequestParam(value = "ids")String ids
    ){
        userService.removeByIds(ids.split(","));
        return new PageBean();
    }


    @PostMapping(value = "insert.json")
    public PageBean insertDept(@ModelAttribute SysUser user){
        userService.insertUser (user);
        return new PageBean();
    }

}
