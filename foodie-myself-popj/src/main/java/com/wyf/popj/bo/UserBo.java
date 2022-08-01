package com.wyf.popj.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录用BO", description = "从客户端用户输入的数据，被封装在entity中返回")
public class UserBo {
    @ApiModelProperty(value = "用户名", name = "username", example = "年兽", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = true)
    private String confirmPassword;

}
