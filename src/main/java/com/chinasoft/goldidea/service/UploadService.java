package com.chinasoft.goldidea.service;

import com.chinasoft.goldidea.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface UploadService {
    BaseResponse upload(MultipartFile file, String openId,String ideaDraft, String projectId, String coverState);

    BaseResponse download(HttpServletResponse response, String url);

    BaseResponse fileList(String openId, String ideaDraft, String projectId);

    BaseResponse update(MultipartFile file, String openId,String ideaId);
}
