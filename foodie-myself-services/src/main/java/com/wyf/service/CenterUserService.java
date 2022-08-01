package com.wyf.service;

import com.wyf.popj.Users;
import com.wyf.utils.JsonResult;
import org.springframework.web.multipart.MultipartFile;

public interface CenterUserService {

    /**
     * 根据Id查找对应的用户信息
     * @param id
     * @return
     */
    Users queryUserInfoById(String id);

    /**
     * 上传头像到阿里OSS 并保存头像到数据库
     * @param uploadFile
     * @param userId
     * @return
     */
    JsonResult upLoadAndSaveFile(MultipartFile uploadFile, String userId);
}
