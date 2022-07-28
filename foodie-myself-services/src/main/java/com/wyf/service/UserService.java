package com.wyf.service;

import com.wyf.popj.Users;
import com.wyf.popj.bo.UserBo;

public interface UserService {
    /**
     * 自定义的userBO 用于注册使用
     * @param userBo
     * @return
     */
    Users createUser(UserBo userBo);

    /**
     * 根据 username 判断数据库是否已经存在用户
     * @param username
     * @return
     */
    Boolean IsExistByUsername(String username);

    /**
     * 登录逻辑 验证用户名跟密码
     * @param userBo
     * @return
     */
    Users checkForLogin(UserBo userBo) throws Exception;
}
