package com.wyf.controller;

import com.wyf.popj.bo.ShopcartBO;
import com.wyf.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车API", tags = {"购物车API"})
@RestController
@RequestMapping("/shopcart")
public class ShopCatController {

    @ApiOperation(value = "添加到购物车", notes = "添加到购物车", httpMethod = "POST")
    @RequestMapping("/add")
    public JsonResult addCats(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (StringUtil.isEmpty(userId)) {
            return JsonResult.errorMsg("用户id为空");
        }

        return JsonResult.ok();
    }
}
