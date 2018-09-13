package com.chinasoft.goldidea.controller;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.common.PageModel;
import com.chinasoft.goldidea.common.ResponseCode;
import com.chinasoft.goldidea.controller.bean.ProjectInfoVO;
import com.chinasoft.goldidea.domain.ProjectInfoDomain;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.service.ProjectInfoService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/goldenIdea")
public class ProjectInfoController {

    @Autowired
    private ProjectInfoService projectInfoService;

    @Value("${EERConfig.NN_ERROR_CODE}")
    private String NN_ERROR_CODE;

    @Value("${EERConfig.NN_ERROR_MSG}")
    private String NN_ERROR_MSG;

    /**
     * 创建方案信息
     *
     * @param vo
     * @return BaseResponse
     */
    @PostMapping(value = "/project")
    public BaseResponse createProject (@RequestBody ProjectInfoVO vo) {
        log.info (String.format ("%s%n",
                "*******************************Start method createProject*******************************"));
        BaseResponse baseResponse = new BaseResponse ();
        ProjectInfoDomain projectInfoDomain = new ProjectInfoDomain ();
        BeanUtils.copyProperties (vo, projectInfoDomain);
        try {
            ProjectInfoVO projectInfoVO = projectInfoService.createProjectSerice (projectInfoDomain);
            baseResponse.setStatus ("200");
            baseResponse.getResponseBody ().add (projectInfoVO);
        } catch (BusinessException e) {
            baseResponse.setStatus (e.getStatus ());
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            baseResponse.setStatus (NN_ERROR_CODE);
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
        }
        log.info (String.format ("%s%n",
                "*******************************End method createProject*******************************"));
        return baseResponse;
    }

    /**
     * 修改方案信息
     *
     * @param vo
     * @return BaseResponse
     */
    @PutMapping(value = "/project")
    public BaseResponse modifyProject (@RequestBody ProjectInfoVO vo) {
        log.info (String.format ("%s%n",
                "*******************************Start method modifyProject*******************************"));
        Assert.notNull (vo, "[Assertion failed] - this argument [ProjectInfoVO] is required; it must not be null");
        Assert.notNull (vo.getOpen_id (), "[Assertion failed] - this argument [openId] is required; it must not be null");
        //初始化
        String status = "200";
        ProjectInfoVO projectInfoVO = null;
        //实例化对象
        BaseResponse baseResponse = new BaseResponse ();
        ProjectInfoDomain projectInfoDomain = new ProjectInfoDomain ();
        try {
            //参数转换
            BeanUtils.copyProperties (vo, projectInfoDomain);
            //调用Service修改方法
            projectInfoVO = projectInfoService.modifyProjectSerice (projectInfoDomain);
            baseResponse.getResponseBody ().add (projectInfoVO);
        } catch (BusinessException e) {
            status = e.getStatus ();
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            status = NN_ERROR_CODE;
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
        }
        //装载状态字段
        baseResponse.setStatus (status);
        log.info (String.format ("%s%n",
                "*******************************End method modifyProject*******************************"));
        return baseResponse;
    }

    @GetMapping(value = "/project/{projectId}")
    public BaseResponse findProjectById(@PathVariable Long projectId){
        String status = "200";
        BaseResponse baseResponse = new BaseResponse ();
        List <ProjectInfoVO> vo = null;
        try {
            vo = projectInfoService.findProjectById (projectId);
        }  catch (BusinessException e) {
            status = e.getStatus ();
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            status = NN_ERROR_CODE;
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
        }
        //装载状态字段
        baseResponse.setStatus (status);
        baseResponse.getResponseBody ().add (vo);
        return baseResponse;
    }
    /**
     * 查询方案信息
     *
     * @param openId
     * @param ideaInfo
     * @param ideaId
     * @param projectStatus
     * @return BaseResponse
     */
    @GetMapping(value = "/project")
    public BaseResponse findProject (@RequestParam(required = false) String openId, String ideaInfo, Long ideaId, Integer projectStatus, int curPage, int pageSize) {
        log.info (String.format ("%s%n",
                "*******************************Start method findProject*******************************"));
        ProjectInfoDomain projectInfoDomain = new ProjectInfoDomain ();
        BaseResponse baseResponse = new BaseResponse ();
        projectInfoDomain.setIdeaInfo (ideaInfo);
        projectInfoDomain.setOpen_id (openId);
        projectInfoDomain.setIdea_id (ideaId);
        projectInfoDomain.setProject_status (projectStatus);
        projectInfoDomain.setCurPage (curPage);
        projectInfoDomain.setPageSize (pageSize);
        List <ProjectInfoVO> voList = null;
        try {
            voList = projectInfoService.viewProjectService (projectInfoDomain);
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            baseResponse.setStatus (NN_ERROR_CODE);
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
        }
        baseResponse.setStatus ("200");
        baseResponse.getResponseBody ().addAll (voList);
        log.info (String.format ("%s%n",
                "*******************************End method findProject*******************************"));
        return baseResponse;
    }

    @GetMapping(value = "/projectCount")
    public BaseResponse findProjectCount () throws Exception {
        Integer count = projectInfoService.findProjectCount ();
        BaseResponse baseResponse = new BaseResponse ();
        PageModel PageModel = new PageModel();
        PageModel.setCount (count);
        baseResponse.setStatus ("200");
        baseResponse.getResponseBody ().add (PageModel);
        return baseResponse;
    }

    @GetMapping(value = "/updateReadingVolume/{projectId}")
    public BaseResponse updateReadingVolume (@PathVariable Long projectId) throws Exception {
        if ( projectId == null){
            BaseResponse.createByErrorMessage(ResponseCode.NULLERROR.getCode(),ResponseCode.NULLERROR.getDesc());
        }
        return projectInfoService.readingVolume(projectId);
    }
}
