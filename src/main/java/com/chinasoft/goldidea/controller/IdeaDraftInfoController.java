package com.chinasoft.goldidea.controller;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoVO;
import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoWithProjectVO;
import com.chinasoft.goldidea.controller.bean.TableCountVO;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.repository.IdeaDraftInfoRepository;
import com.chinasoft.goldidea.repository.ProjectInfoRepository;
import com.chinasoft.goldidea.service.IdeaDraftInfoService;
import com.chinasoft.goldidea.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/goldenIdea")
public class IdeaDraftInfoController {

    @Autowired
    IdeaDraftInfoRepository ideaDraftInfoRepository;
    @Autowired
    ProjectInfoRepository projectInfoRepository;
    @Autowired
    IdeaDraftInfoService ideaDraftInfoService;
    @Autowired
    UploadService uploadService;

    @Value("${EERConfig.NN_ERROR_CODE}")
    private String NN_ERROR_CODE;
    @Value("${EERConfig.NN_ERROR_MSG}")
    private String NN_ERROR_MSG;
    @Value("${EERConfig.EP_ERROR_CODE}")
    private String EP_ERROR_CODE;
    @Value("${EERConfig.EP_ERROR_MSG}")
    private String EP_ERROR_MSG;

    /**
     * 查询所有数据
     *
     * @return
     */
    @GetMapping(value = "/info")
    public BaseResponse findAll() {
        log.info("***********************************Start method findAll()*************************************************");
        List<IdeaDraftInfoVO> listIdeaDraftInfoVO = ideaDraftInfoService.findAll();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.getResponseBody().add(listIdeaDraftInfoVO);
        baseResponse.setStatus("200");
        log.info("***********************************End method findAll()*************************************************");

        return baseResponse;
    }

    /**
     * 统计一个人名下点子和方案的数量
     *
     * @param openId
     * @return
     */
    @GetMapping(value = "/count")
    public BaseResponse count(@RequestParam String openId) {
        log.info("***********************************Start method count()*************************************************");
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isEmpty(openId)) {
            baseResponse.setStatus(EP_ERROR_CODE);
            baseResponse.getResponseBody().add(EP_ERROR_MSG);
            return baseResponse;
        }
        log.info("openId is 【{}】", openId);
        try {
            TableCountVO tableCountVO = ideaDraftInfoService.count(openId);
            baseResponse.getResponseBody().add(tableCountVO);
            baseResponse.setStatus("200");
        } catch (BusinessException e) {
            baseResponse.setStatus(e.getStatus());
            baseResponse.getResponseBody().add(e.getArgs());
        } catch (Exception e) {
            baseResponse.setStatus(NN_ERROR_CODE);
            baseResponse.getResponseBody().add(NN_ERROR_MSG);
            log.error(e.getMessage());
        }
        log.info("***********************************End method count()*************************************************");
        return baseResponse;
    }

    /**
     * 添加一个语言点子
     *
     * @param
     * @return
     */
    @PostMapping(value = "/ideaByVoice")
    public BaseResponse ideaAddByVoice(@RequestParam(value = "file") MultipartFile file,
                                       @RequestParam(value = "openId") String openId) {
        log.info("***********************************Start method ideaAddByVoice()*************************************************");
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isEmpty(openId)) {
            baseResponse.setStatus(EP_ERROR_CODE);
            baseResponse.getResponseBody().add(EP_ERROR_MSG);
            return baseResponse;
        }
        try {
            IdeaDraftInfoVO resultVO = ideaDraftInfoService.saveAndUpload(file, openId);
            baseResponse.getResponseBody().add(resultVO);
            baseResponse.setStatus("200");
        } catch (BusinessException e) {
            baseResponse.getResponseBody().add(e.getArgs());
            baseResponse.setStatus(e.getStatus());
        } catch (Exception e) {
            baseResponse.setStatus(NN_ERROR_CODE);
            baseResponse.getResponseBody().add(NN_ERROR_MSG);
            log.error(e.getMessage());
        }
        log.info("***********************************End method ideaAddByVoice()*************************************************");
        return baseResponse;
    }

    @PostMapping(value = "/updateIdeaByVoice")
    public BaseResponse updateVoice(@RequestParam(value = "file") MultipartFile file,
                                    @RequestParam(value = "openId") String openId,
                                    @RequestParam(value = "ideaId") String ideaId) {
        log.info("***********************************Start method updateVoice()*************************************************");
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isEmpty(ideaId) || StringUtils.isEmpty(openId)) {
            baseResponse.setStatus(EP_ERROR_CODE);
            baseResponse.getResponseBody().add(EP_ERROR_MSG);
            return baseResponse;
        }

        BaseResponse resultResponse = uploadService.update(file, openId, ideaId);

        log.info("***********************************End method updateVoice()*************************************************");
        return resultResponse;
    }

    /**
     * 添加一个文字点子
     * @param vo
     * @return
     */
    @PostMapping(value = "/ideaByIdea")
    public BaseResponse ideaAddByIdea(@RequestBody IdeaDraftInfoVO vo) {
        log.info("***********************************Start method ideaAddByIdea()*************************************************");
        BaseResponse baseResponse = new BaseResponse();

        log.info("idea is 【{}】 , openId is 【{}】", vo.getIdea(), vo.getOpen_id());
        if (StringUtils.isEmpty( vo.getOpen_id())) {
            baseResponse.setStatus(EP_ERROR_CODE);
            baseResponse.getResponseBody().add(EP_ERROR_MSG);
            return baseResponse;
        }
        try {
            IdeaDraftInfoVO resultvo = ideaDraftInfoService.save(vo.getIdea(),  vo.getOpen_id());
            resultvo.setOpen_id(null);// 不需要给前端返回该参数
            baseResponse.getResponseBody().add(resultvo);
            baseResponse.setStatus("200");
        } catch (BusinessException e) {
            baseResponse.getResponseBody().add(e.getArgs());
            baseResponse.setStatus(e.getStatus());
        } catch (Exception e) {
            baseResponse.setStatus(NN_ERROR_CODE);
            baseResponse.getResponseBody().add(NN_ERROR_MSG);
            log.error(e.getMessage());
        }
        log.info("***********************************End method ideaAddByIdea()*************************************************");
        return baseResponse;
    }

    /**
     * 查询
     *
     * @param ideaId
     * @param openId
     * @return
     */
    @GetMapping(value = "/idea")
    public BaseResponse findByIdeaIdAndOpenId(@RequestParam(required = false) String ideaId, String openId) {
        log.info("***********************************Start method findByIdeaIdAndOpenId()*************************************************");
        log.info("ideaId is 【{}】, openId is 【{}】", ideaId, openId);
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isEmpty(ideaId) && StringUtils.isEmpty(openId)) {
            baseResponse.setStatus(EP_ERROR_CODE);
            baseResponse.getResponseBody().add(EP_ERROR_MSG);
            return baseResponse;
        }
        Long ideaIdLong = null;
        if (!StringUtils.isEmpty(ideaId)) {
            ideaIdLong = Long.valueOf(ideaId);
        }
        try {
            List<IdeaDraftInfoWithProjectVO> resultVo = ideaDraftInfoService.findByIdea_idAndOpen_id(ideaIdLong, openId);
            baseResponse.getResponseBody().add(resultVo);
            baseResponse.setStatus("200");
        } catch (BusinessException e) {
            baseResponse.getResponseBody().add(e.getArgs());
            baseResponse.setStatus(e.getStatus());
            log.error(e.getMessage());
        } catch (Exception e) {
            baseResponse.setStatus(NN_ERROR_CODE);
            baseResponse.getResponseBody().add(NN_ERROR_MSG);
            log.error(e.getMessage());
        }
        log.info("***********************************End method findByIdeaIdAndOpenId()*************************************************");
        return baseResponse;
    }

    /**
     * 更新
     *
     * @param vo
     * @return
     */
    @PutMapping(value = "/ideaByIdea")
    public BaseResponse update(@RequestBody IdeaDraftInfoVO vo) {
        log.info("***********************************Start method idea update()*************************************************");
        String idea = vo.getIdea();
        Long ideaId = vo.getIdea_id();
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isEmpty(idea) && null == ideaId) {
            baseResponse.setStatus(EP_ERROR_CODE);
            baseResponse.getResponseBody().add(EP_ERROR_MSG);
            return baseResponse;
        }
        log.info("ideaId is 【{}】, idea is 【{}】", ideaId, idea);
        try {
            IdeaDraftInfoVO ideaDraftInfoVO = ideaDraftInfoService.update(vo.getIdea_id(), vo.getIdea());
            baseResponse.getResponseBody().add(ideaDraftInfoVO);
            baseResponse.setStatus("200");
        } catch (BusinessException e) {
            baseResponse.getResponseBody().add(e.getArgs());
            baseResponse.setStatus(e.getStatus());
            log.error(e.getMessage());
        } catch (Exception e) {
            baseResponse.setStatus(NN_ERROR_CODE);
            baseResponse.getResponseBody().add(NN_ERROR_MSG);
            log.error(e.getMessage());
        }
        log.info("***********************************End method update()*************************************************");
        return baseResponse;
    }


}
