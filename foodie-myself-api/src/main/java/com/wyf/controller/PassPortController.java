package com.wyf.controller;

import com.wyf.popj.Users;
import com.wyf.popj.bo.UserBo;
import com.wyf.service.UserService;

import com.wyf.utils.CookieUtils;
import com.wyf.utils.JsonResult;
import com.wyf.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "登陆注册", tags = {"登录注册"})
@RestController
@RequestMapping("/passport")
public class PassPortController {

    @Autowired
    private UserService userservice;

    // 注册结构
    @ApiOperation(value = "注册", notes = "注册", httpMethod = "POST")
    @RequestMapping("/regist")
    public JsonResult register(@RequestBody UserBo userBo) {

        String username = userBo.getUsername();
        String password = userBo.getPassword();
        String confirmPwd = userBo.getConfirmPassword();

        // 1.检查用户名是否为空
        if (StringUtil.isEmpty(username) ||
                StringUtil.isEmpty(password) ||
                StringUtil.isEmpty(confirmPwd)) {
            return JsonResult.errorMsg("用户名为空");
        }

        // 2.检查密码与确认密码是否一致
        if (!password.equals(confirmPwd)) {
            return JsonResult.errorMsg("两次密码输入不一致");
        }

        // 3.检查用户是否已经创建
        if (userservice.IsExistByUsername(username)) {
            return JsonResult.errorMsg("用户名重复，请重新输入");
        }

        // 4.创建用户
        Users userResult = userservice.createUser(userBo);

        // 5.检查创建是否成功
        if (userResult.getUsername() == null) {
            return JsonResult.errorMsg("创建用户失败，重新尝试(数据已回滚)");
        }

        // 6.返回
        return JsonResult.ok(userResult);
    }

    // 登录用：判断数据库是否有该用户
    @ApiOperation(value = "根据用户名判断该用户是否存在", notes = "根据用户名判断该用户是否存在", httpMethod = "GET")
    @RequestMapping("/usernameIsExist")
    public JsonResult usernameIsExiset(@RequestParam String username) {
        // 根据用户名判断数据库是否有此用户
        Boolean result = userservice.IsExistByUsername(username);

        // 为空返回错误信息
        if (!result) {
            return JsonResult.errorMsg("此用户不存在");
        }

        // 返回
        return JsonResult.ok();
    }

    // 登录接口
    @ApiOperation(value = "登录", notes = "登录", httpMethod = "POST")
    @RequestMapping("/login")
    public JsonResult login(@RequestBody UserBo userBo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String username = userBo.getUsername();
        String password = userBo.getPassword();

        // 1.判断用户名和密码是否为空
        if (StringUtil.isEmpty(username) ||
                StringUtil.isEmpty(password)) {
            return JsonResult.errorMsg("用户名或者密码不能为空");
        }
        Users checkResult = new Users();
        // 2.登陆逻辑 判断用户名密码
        try {
            checkResult = userservice.checkForLogin(userBo);
        } catch (Exception e) {
            return JsonResult.errorMsg(e.getLocalizedMessage());
        }


        // 3.有状态登录注册 设置前端cookie
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(checkResult), true);

        return JsonResult.ok(checkResult);
    }

    // 退出登录接口API
    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "GET")
    @RequestMapping("/logout")
    public JsonResult logout(@RequestParam String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        return JsonResult.ok();
    }

}
