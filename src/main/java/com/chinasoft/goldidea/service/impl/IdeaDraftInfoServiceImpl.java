package com.chinasoft.goldidea.service.impl;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.common.ResponseCode;
import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoVO;
import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoWithProjectVO;
import com.chinasoft.goldidea.controller.bean.TableCountVO;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import com.chinasoft.goldidea.repository.IdeaDraftInfoRepository;
import com.chinasoft.goldidea.repository.ProjectInfoRepository;
import com.chinasoft.goldidea.service.IdeaDraftInfoService;
import com.chinasoft.goldidea.service.UploadService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class IdeaDraftInfoServiceImpl implements IdeaDraftInfoService {

    private static final String SLASH = "/";

    @Autowired
    IdeaDraftInfoRepository ideaDraftInfoRepository;
    @Autowired
    ProjectInfoRepository projectInfoRepository;
    @Autowired
    UploadService uploadService;
    private static Integer IDEADRAF_INFO_STATUS = 0; // 点子发布状态，默认为0
    @Value("${EERConfig.DB_ERROR_MSG}")
    private String DB_ERROR_MSG;
    @Value("${EERConfig.DB_ERROR_CODE}")
    private String DB_ERROR_CODE;

    @Override
    public List<IdeaDraftInfoVO> findAll() {
        List<IdeaDraftInfoPO> listIdeaDraftInfoPO = ideaDraftInfoRepository.findAll();
        List<IdeaDraftInfoVO> listIdeaDraftInfoVO = new ArrayList<>();
        for (IdeaDraftInfoPO ideadrftIndex : listIdeaDraftInfoPO) {
            IdeaDraftInfoVO ideaDraftInfoVO = new IdeaDraftInfoVO();
            BeanUtils.copyProperties(ideadrftIndex, ideaDraftInfoVO);
            Date createTime = ideadrftIndex.getCreateTime();
            Long createTimeLong = null;
            Date updateTime = ideadrftIndex.getUpdateTime();
            Long updateTimeLong = null;
            if (createTime != null) {
                createTimeLong = createTime.getTime();
                ideaDraftInfoVO.setCreateTime(createTimeLong);
            }
            if (updateTime != null) {
                updateTimeLong = updateTime.getTime();
                ideaDraftInfoVO.setUpdateTime(updateTimeLong);
            }
            listIdeaDraftInfoVO.add(ideaDraftInfoVO);
        }
        return listIdeaDraftInfoVO;
    }


    @Override
    public TableCountVO count(String openId) throws Exception {
        Integer rls_projectStatus = 1; // 已发布状态
        Integer notRls_projectStatus = 0; // 未发布状态
        Integer rlsProjectCount = 0;
        Integer notRlsProjectCount = 0;
        Integer countidea = 0;
        List<IdeaDraftInfoVO> ideaDraftInfoVOList = null;
        try {
            countidea = ideaDraftInfoRepository.countByOpen_id(openId);
            ideaDraftInfoVOList = this.findByOpenId(openId);
            for (IdeaDraftInfoVO ideaDraftIndex : ideaDraftInfoVOList) {
                Long ideaId = ideaDraftIndex.getIdea_id();
                IdeaDraftInfoPO ideaDraftInfoPO = ideaDraftInfoRepository.findByIdeaId(ideaId);
                Integer rlsProjectCountTemp = projectInfoRepository.countrlsProjectCount(ideaDraftInfoPO, rls_projectStatus);
                Integer notRlsProjectCountTemp = projectInfoRepository.countrlsProjectCount(ideaDraftInfoPO, notRls_projectStatus);
                rlsProjectCount += rlsProjectCountTemp;
                notRlsProjectCount += notRlsProjectCountTemp;
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("Error Status:" + DB_ERROR_CODE + ",Reason:" + DB_ERROR_MSG);
            throw new BusinessException(DB_ERROR_CODE, DB_ERROR_MSG);
        }
        log.info("ideaDraftInofCount is [{}], rlsProjectCount[{}], notRlsProjectCount [{}]", openId, rlsProjectCount, notRlsProjectCount);

        TableCountVO tableCountVO = new TableCountVO();
        tableCountVO.setIdeaCount(String.valueOf(countidea));
        tableCountVO.setRlsProjectCount(String.valueOf(rlsProjectCount));
        tableCountVO.setNotRlsProjectCount(String.valueOf(notRlsProjectCount));
        return tableCountVO;
    }

    /**
     * 保存方法
     *
     * @param idea
     * @param openId
     * @return
     * @throws Exception
     */
    @Override
    public IdeaDraftInfoVO save(String idea, String openId) throws Exception {

        IdeaDraftInfoPO ideaDraftInfoPO = new IdeaDraftInfoPO();
        ideaDraftInfoPO.setIdea(idea);
        ideaDraftInfoPO.setOpen_id(openId);
        ideaDraftInfoPO.setStatus(IDEADRAF_INFO_STATUS);// 默认为“未发布”状态
        ideaDraftInfoPO.setCreateTime(new Date());
        ideaDraftInfoPO.setUpdateTime(new Date());
        try {
            ideaDraftInfoPO = ideaDraftInfoRepository.save(ideaDraftInfoPO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("Error Status:" + DB_ERROR_CODE + ",Reason:" + DB_ERROR_MSG);
            throw new BusinessException(DB_ERROR_CODE, DB_ERROR_MSG);
        }
        IdeaDraftInfoVO vo = new IdeaDraftInfoVO();
        BeanUtils.copyProperties(ideaDraftInfoPO, vo);

        return vo;
    }

    public IdeaDraftInfoVO saveAndUpload(MultipartFile file, String openId) throws Exception {

        IdeaDraftInfoPO ideaDraftInfoPO = new IdeaDraftInfoPO();
        ideaDraftInfoPO.setOpen_id(openId);
        ideaDraftInfoPO.setStatus(IDEADRAF_INFO_STATUS);// 默认为“未发布”状态
        ideaDraftInfoPO.setCreateTime(new Date());
        ideaDraftInfoPO.setUpdateTime(new Date());
        try {
            ideaDraftInfoPO = ideaDraftInfoRepository.save(ideaDraftInfoPO);
            Long idea_id = ideaDraftInfoPO.getIdea_id();
            BaseResponse baseResponse = uploadService.upload(file, openId, String.valueOf(idea_id), null, null);
            ideaDraftInfoPO.setFilePath((String)baseResponse.getResponseBody().get(0));
            ideaDraftInfoPO = ideaDraftInfoRepository.save(ideaDraftInfoPO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("Error Status:" + DB_ERROR_CODE + ",Reason:" + DB_ERROR_MSG);
            throw new BusinessException(DB_ERROR_CODE, DB_ERROR_MSG);
        }
        IdeaDraftInfoVO vo = new IdeaDraftInfoVO();
        BeanUtils.copyProperties(ideaDraftInfoPO, vo);

        return vo;
    }

    public List<IdeaDraftInfoWithProjectVO> findByIdea_idAndOpen_id(Long idea_id, String open_id) throws Exception {
        List<IdeaDraftInfoWithProjectVO> ideaDraftInfoWithProjectVOList = new ArrayList<>();
        IdeaDraftInfoPO ideaDraftInfoPO = new IdeaDraftInfoPO();
        ideaDraftInfoPO.setIdea_id(idea_id);
        ideaDraftInfoPO.setOpen_id(open_id);
        Example<IdeaDraftInfoPO> example = Example.of(ideaDraftInfoPO);
        List<IdeaDraftInfoPO> ideaDraftInfoPOList = null;
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            ideaDraftInfoPOList = ideaDraftInfoRepository.findAll(example, sort);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("Error Status:" + DB_ERROR_CODE + ",Reason:" + DB_ERROR_MSG);
            throw new BusinessException(DB_ERROR_CODE, DB_ERROR_MSG);
        }
        for (IdeaDraftInfoPO ideadrafIndex : ideaDraftInfoPOList) {
            IdeaDraftInfoWithProjectVO ideaDraftInfoWithProjectVO = new IdeaDraftInfoWithProjectVO();
            Integer countProject = projectInfoRepository.findProjectCountByIdeaId(ideadrafIndex);
            if (null != ideadrafIndex) {
                BeanUtils.copyProperties(ideadrafIndex, ideaDraftInfoWithProjectVO);
            }
            ideaDraftInfoWithProjectVO.setProject_count(countProject);
            Date createTime = ideadrafIndex.getCreateTime();
            Long createTimeLong = null;
            Date updateTime = ideadrafIndex.getUpdateTime();
            Long updateTimeLong = null;
            if (createTime != null) {
                createTimeLong = createTime.getTime();
                ideaDraftInfoWithProjectVO.setCreateTime(createTimeLong);
            }
            if (updateTime != null) {
                updateTimeLong = updateTime.getTime();
                ideaDraftInfoWithProjectVO.setUpdateTime(updateTimeLong);
            }
            ideaDraftInfoWithProjectVOList.add(ideaDraftInfoWithProjectVO);
        }
        return ideaDraftInfoWithProjectVOList;
    }

    @Override
    public IdeaDraftInfoVO update(Long ideaId, String idea) throws Exception {
        IdeaDraftInfoVO ideaDraftInfoVO = new IdeaDraftInfoVO();
        try {
            IdeaDraftInfoPO ideaDraftInfoPO = ideaDraftInfoRepository.findByIdeaId(ideaId);
            ideaDraftInfoPO.setIdea(idea);
            ideaDraftInfoPO.setUpdateTime(new Date());
            IdeaDraftInfoPO resultPO = ideaDraftInfoRepository.save(ideaDraftInfoPO);
            BeanUtils.copyProperties(resultPO, ideaDraftInfoVO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("Error Status:" + DB_ERROR_CODE + ",Reason:" + DB_ERROR_MSG);
            throw new BusinessException(DB_ERROR_CODE, DB_ERROR_MSG);
        }
        return ideaDraftInfoVO;
    }

    @Override
    public List<IdeaDraftInfoVO> findByOpenId(String openId) {
        List<IdeaDraftInfoPO> ideaDraftInfoPOList = ideaDraftInfoRepository.findByOpen_id(openId);
        List<IdeaDraftInfoVO> ideaDraftInfoVOList = new ArrayList<>();
        if (null != ideaDraftInfoPOList) {
            for (IdeaDraftInfoPO iddfIndex : ideaDraftInfoPOList) {
                IdeaDraftInfoVO ideaDraftInfoVO = new IdeaDraftInfoVO();
                BeanUtils.copyProperties(iddfIndex, ideaDraftInfoVO);
                Date createTime = iddfIndex.getCreateTime();
                Long createTimeLong = null;
                Date updateTime = iddfIndex.getUpdateTime();
                Long updateTimeLong = null;
                if (createTime != null) {
                    createTimeLong = createTime.getTime();
                    ideaDraftInfoVO.setCreateTime(createTimeLong);
                }
                if (updateTime != null) {
                    updateTimeLong = updateTime.getTime();
                    ideaDraftInfoVO.setUpdateTime(updateTimeLong);
                }
                ideaDraftInfoVOList.add(ideaDraftInfoVO);
            }
        }
        return ideaDraftInfoVOList;
    }


}
