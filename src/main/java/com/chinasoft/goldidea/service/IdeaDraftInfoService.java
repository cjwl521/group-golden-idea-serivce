package com.chinasoft.goldidea.service;

import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoVO;
import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoWithProjectVO;
import com.chinasoft.goldidea.controller.bean.TableCountVO;
import com.chinasoft.goldidea.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface IdeaDraftInfoService {
    List<IdeaDraftInfoVO> findAll();
    TableCountVO count(String openId)throws Exception;
    IdeaDraftInfoVO save(String idea, String openId)throws Exception;
    IdeaDraftInfoVO saveAndUpload(MultipartFile file, String openId,String filePath) throws Exception;
    List<IdeaDraftInfoWithProjectVO> findByIdea_idAndOpen_id(Long idea_id, String open_id)throws Exception;
    IdeaDraftInfoVO update(Long ideaId, String idea)throws Exception;
    List<IdeaDraftInfoVO> findByOpenId(String openId);
}
