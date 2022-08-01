package com.wyf.service.impl;

import com.wyf.enums.Sex;
import com.wyf.mapper.UsersMapper;
import com.wyf.popj.Users;
import com.wyf.popj.bo.UserBo;
import com.wyf.service.UserService;
import com.wyf.utils.JsonResult;
import com.wyf.utils.MD5Utils;
import com.wyf.utils.RandomId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.beans.Transient;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Autowired
    UsersMapper usersMapper;


    //自定义的userBO 用于注册使用
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBo userBo) {
        Users users = new Users();
        // 设置ID
        users.setId(RandomId.generatorId());
        //设置用户名
        users.setUsername(userBo.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        users.setNickname(userBo.getUsername());
        // 默认头像
        users.setFace(USER_FACE);
        // 默认为创建时间
        users.setBirthday(new Date());
        // 默认性别为 保密
        users.setSex(Sex.SECRET.type);
        //设置创建时间 修改时间
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        // 插入数据库
        usersMapper.insert(users);

        // 返回
        return users;
    }

    //根据 username 判断数据库是否已经存在用户
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean IsExistByUsername(String username) {
        // 创建Example对象
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);

        // 根据用户名查询数据库是否已经存在此用户
        Users result = usersMapper.selectOneByExample(userExample);

        // 判断result是否有值 返回
        return result == null ? false : true;

    }


    //登录逻辑 验证用户名跟密码
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users checkForLogin(UserBo userBo) throws Exception {
        String username = userBo.getUsername();
        String password = userBo.getPassword();

        // 1.根据用户名查找到对应的对象
        // 创建Example对象
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);

        // 根据用户名查询数据库
        Users result = usersMapper.selectOneByExample(userExample);

        // 2.校验密码
        if (!result.getPassword().equals(MD5Utils.getMD5Str(password))) {
            throw new Exception("用户名或者密码错误");
        }

        return result;
    }
}
