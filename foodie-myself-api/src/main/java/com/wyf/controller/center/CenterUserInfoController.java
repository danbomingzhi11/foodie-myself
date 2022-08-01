package com.wyf.controller.center;

import com.wyf.service.CenterUserService;
import com.wyf.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "center - 用户信息中心", tags = {"用户用户信息中心中心展示的相关接口"})
@RestController
@RequestMapping("/userInfo")
public class CenterUserInfoController {
    @Autowired
    private CenterUserService centerUserService;


    @ApiOperation(value = "上传头像", notes = "上传头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public JsonResult updataFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "file", required = true)
            @RequestBody MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

        // 上传头像逻辑
        JsonResult jsonResult = centerUserService.upLoadAndSaveFile(file, userId);

        return jsonResult;
    }
}
