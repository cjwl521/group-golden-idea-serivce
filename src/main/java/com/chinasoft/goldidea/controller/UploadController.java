package com.chinasoft.goldidea.controller;


import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.common.ResponseCode;
import com.chinasoft.goldidea.controller.bean.FilePathInfoVO;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.service.UploadService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/goldenIdea")
public class UploadController {

    @Value("${FILEPATHConfig.UPLOAD_FLODER}")
    private String UPLOAD_FLODER;

    private static final String SLASH = "/";

    @Autowired
    UploadService uploadService;

    /**
     * 上传文件接口
     *
     * @param file 需要上传的文件
     * @param openId 微信的openid
     * @param ideaDraft 金点子id
     * @param projectId 方案id
     * @param coverState 备用，如果传入exist ，则传入同名文件不会被覆盖
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse singleFileUpload(@RequestParam(value = "file") MultipartFile file,
                                         @RequestParam(value = "openId") String openId,
                                         @RequestParam(value = "ideaDraft") String ideaDraft,
                                         @RequestParam(value = "projectId", required = false) String projectId,
                                         @RequestParam(value = "coverState", required = false) String coverState) {
        if (file.getSize() == 0 || StringUtils.isEmpty(openId)) {
            return BaseResponse.createByErrorMessage("please select a file or input file path");
        }
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(ideaDraft) ) {
            return BaseResponse.createByErrorMessage("please input openId or ideaDraft");
        }

        return uploadService.upload(file, openId, ideaDraft, projectId, coverState);

    }

    /**
     * 根据url下载文件
     * @param response Servlet Respon 用来承载返回文件的内容
     * @param url 需要下载文件的路径
     * @return
     */
    @GetMapping(value = "/download")
    public BaseResponse downLoad(HttpServletResponse response, @RequestParam String url) {
        if (StringUtils.isEmpty(url)) {
            return BaseResponse.createByErrorMessage(ResponseCode.NULLERROR.getCode(), ResponseCode.NULLERROR.getDesc());
        }
        return uploadService.download(response,url);
    }

    /**
     * 查询方案内的可下载资源
     * @param openId 用户的openid
     * @param ideaDraft 点子的id
     * @param projectId 方案的id
     * @return
     */
    @GetMapping("/list")
    public BaseResponse fileList(@RequestParam(value = "openId") String openId,
                                 @RequestParam(value = "ideaDraft",required = false) String ideaDraft,
                                 @RequestParam(value = "projectId", required = false) String projectId) {

        return uploadService.fileList(openId,ideaDraft,projectId);
    }
}
