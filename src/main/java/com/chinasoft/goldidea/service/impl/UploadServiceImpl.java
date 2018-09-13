package com.chinasoft.goldidea.service.impl;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.common.ResponseCode;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import com.chinasoft.goldidea.repository.IdeaDraftInfoRepository;
import com.chinasoft.goldidea.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${FILEPATHConfig.UPLOAD_FLODER}")
    private String UPLOAD_FLODER;
    private static final String SLASH = "/";

    @Autowired
    IdeaDraftInfoRepository ideaDraftInfoRepository;

    /**
     * 上传文件
     *
     * @param file
     * @param openId
     * @param ideaDraft
     * @param projectId
     * @param coverState
     * @return
     */
    @Override
    public BaseResponse upload(MultipartFile file, String openId,
                               String ideaDraft, String projectId, String coverState) {
        StringBuilder folderNamePath = new StringBuilder(UPLOAD_FLODER + SLASH + openId + SLASH + ideaDraft);
        String originalFilename = file.getOriginalFilename();
        StringBuilder returnPath = new StringBuilder(openId + SLASH + ideaDraft + SLASH);
        if (StringUtils.isEmpty(projectId)) {
            returnPath.append(originalFilename);
        } else {
            folderNamePath.append(SLASH + projectId);
            returnPath.append(projectId + SLASH + originalFilename);
        }
        File folderNameDir = new File(folderNamePath.toString());
        if (!folderNameDir.exists()) {
            folderNameDir.mkdirs();
        }
        File filer = new File(folderNameDir, originalFilename);
        if (filer.exists() && "exist".equals(coverState)) {
            return BaseResponse.createByErrorMessage("The file exist");
        }
        try {
            file.transferTo(filer);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.UPLODAFAIL.getCode(), ResponseCode.UPLODAFAIL.getDesc());
        }

        return BaseResponse.createBySuccess("upload success", returnPath.toString());
    }

    /**
     * 更新点子语音文件
     */
    @Override
    public BaseResponse update(MultipartFile file, String openId,
                               String ideaId) {
        StringBuilder folderNamePath = new StringBuilder(UPLOAD_FLODER + SLASH + openId + SLASH + ideaId);
        // TODO 1. 查找该路径是否存在文件，如果存在，删除该文件以夹非文件夹的文件，然后将传入的文件传入
        String originalFilename = file.getOriginalFilename();
        StringBuilder returnPath = new StringBuilder(openId + SLASH + ideaId);

        File folderNameDir = new File(folderNamePath.toString());
        if (!folderNameDir.exists()) {
            return BaseResponse.createByErrorMessage("The file not exist");
        }

        File[] files = folderNameDir.listFiles();
        if (files != null) {
            for (File fi : files) {
                if (fi.isFile()) {
                    fi.delete();
                }
            }
        }
        File filer = new File(folderNameDir, originalFilename);

        try {
            file.transferTo(filer);
            returnPath.append(originalFilename);
            IdeaDraftInfoPO ideaDraftInfoPO = ideaDraftInfoRepository.findByIdeaId(Long.valueOf(ideaId));
            ideaDraftInfoPO.setFilePath(returnPath.toString());
            ideaDraftInfoRepository.save(ideaDraftInfoPO);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.UPLODAFAIL.getCode(), ResponseCode.UPLODAFAIL.getDesc());
        }

        return BaseResponse.createBySuccess("update voice success", returnPath.toString());
    }

    /**
     * 下载文件
     *
     * @param response
     * @param url
     * @return
     */
    @Override
    public BaseResponse download(HttpServletResponse response, String url) {
        String completeUrl = UPLOAD_FLODER + SLASH + url;
        File file = new File(completeUrl);
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attchment;fileName=" + fileName);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("----------   File download " + fileName);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return BaseResponse.createByErrorMessage(ResponseCode.FILE_PATH_ERROR.getCode(), ResponseCode.FILE_PATH_ERROR.getDesc());
        }
        return null;
    }

    /**
     * 查看文件列表
     *
     * @param openId
     * @param ideaDraft
     * @param projectId
     * @return
     */
    @Override
    public BaseResponse fileList(String openId, String ideaDraft, String projectId) {
        String fileRootPath = "";
        String returnRootPath = "";
        if (StringUtils.isEmpty(ideaDraft)) {
            fileRootPath = UPLOAD_FLODER + SLASH + openId;
            returnRootPath = openId;
        } else if (StringUtils.isEmpty(projectId)) {
            fileRootPath = UPLOAD_FLODER + SLASH + openId + SLASH + ideaDraft;
            returnRootPath = openId + SLASH + ideaDraft;
        } else {
            fileRootPath = UPLOAD_FLODER + SLASH + openId + SLASH + ideaDraft + SLASH + projectId;
            returnRootPath = openId + SLASH + ideaDraft + SLASH + projectId;
        }
        File file = new File(fileRootPath);
        List<String> fileNames = new ArrayList<String>();
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            if (listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    fileNames.add(returnRootPath + SLASH + f.getName());
                }
            }
        } else {
            return BaseResponse.createByErrorMessage(ResponseCode.NULLERROR.getCode(), ResponseCode.NULLERROR.getDesc());
        }
        return BaseResponse.createBySuccess(ResponseCode.SUCCESS.getCode(), fileNames);
    }
}
