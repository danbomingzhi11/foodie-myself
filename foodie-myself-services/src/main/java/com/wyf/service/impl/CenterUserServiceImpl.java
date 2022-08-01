package com.wyf.service.impl;

import com.aliyun.oss.OSS;
import com.wyf.mapper.CenterUserMapper;
import com.wyf.popj.Users;
import com.wyf.service.CenterUserService;
import com.wyf.service.config.AliyunConfig;
import com.wyf.utils.JsonResult;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    @Resource
    private OSS ossClient;
    @Resource
    private AliyunConfig aliyunConfig;


    @Autowired
    private CenterUserMapper centerUserMapper;

    // 根据Id查找对应的用户信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfoById(String id) {
        return centerUserMapper.selectByPrimaryKey(id);
    }

    // 上传头像到阿里OSS 并保存头像到数据库
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JsonResult upLoadAndSaveFile(MultipartFile uploadFile, String userId) {
        JsonResult jsonResult = upLoadFile(uploadFile);
        if (!jsonResult.isOK()) {
            return jsonResult;
        }
        Users users = new Users();
        String path = jsonResult.getData().toString();
        users.setId(userId);
        users.setFace(path);
        // 更新数据库逻辑 数据插入完成 返回200 ok 失败返回失败
        Integer integer = centerUserMapper.updateByPrimaryKeySelective(users);
        if (integer == 0 ){
            return JsonResult.errorMsg("插入失败");
        }
        return jsonResult;
    }

    /**
     * 使用ossClient操作阿里云OSS，进行上传、下载、删除、查看所有文件等操作，同时可以将图片的url进行入库操作。
     *
     * @param uploadFile
     * @return
     */
    private JsonResult upLoadFile(MultipartFile uploadFile) {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        if (!isLegal) {
            return JsonResult.errorMsg("error");
        }
        // 文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        // 上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            // 上传失败
            return JsonResult.errorMsg("error");
        }

        // 文件路径需要保存到数据库
        String result = "https://" +this.aliyunConfig.getUrlPrefix() + "/" + filePath;
        return JsonResult.ok(result);
    }

    /**
     * 生成路径以及文件名
     *
     * @param sourceFileName
     * @return
     */
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy") + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis()
                + RandomUtils.nextInt(100, 9999) + "."
                + StringUtils.substringAfterLast(sourceFileName, ".");
    }
}
