package com.chinasoft.goldidea.controller;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.controller.bean.DefaultAccountInfoVO;
import com.chinasoft.goldidea.controller.bean.UserInfoVO;
import com.chinasoft.goldidea.domain.DefaultAccountInfoDomain;
import com.chinasoft.goldidea.domain.UserInfoDomain;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/21
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/goldenIdea")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Value("${EERConfig.NN_ERROR_CODE}")
    private String NN_ERROR_CODE;

    @Value("${EERConfig.NN_ERROR_MSG}")
    private String NN_ERROR_MSG;

    @Value("${WXConfig.WEBURL}")
    private String WEB_URL;

    /**
     * 通过微信code获取openID,并且绑定用户信息
     *
     * @param userInfo
     * @return BaseResponse
     */
    @PostMapping(value = "/openId")
    public BaseResponse getOpenId (@RequestBody UserInfoVO userInfo) {
        log.info (String.format ("%s%n",
                "*******************************start method getOpenId*******************************"));
        BaseResponse baseResponse = new BaseResponse ();
        UserInfoDomain userInfoDomain = new UserInfoDomain ();
        Assert.notNull (userInfo, "[Assertion failed] - this argument [UserInfoVO] is required; it must not be null");
        Assert.notNull (userInfo.getWxId (), "[Assertion failed] - this argument [WX Code] is required; it must not be null");
        try {
            userInfoDomain.setWxId (userInfo.getWxId ());
            userInfoDomain.setCity (userInfo.getCity ());
            userInfoDomain.setCountry (userInfo.getCountry ());
            userInfoDomain.setHead_url (userInfo.getHead_url ());
            userInfoDomain.setNick_name (userInfo.getNick_name ());
            userInfoDomain.setProvince (userInfo.getProvince ());
            userInfoDomain.setSex (userInfo.getSex ());

            ObjectMapper objMp = new ObjectMapper ();
            log.info ("userInfoDomain is:{}", objMp.writeValueAsString (userInfoDomain));
            UserInfoVO userData = userInfoService.getOpenId (userInfoDomain);
            log.info ("return UserInfoVO is:{}", objMp.writeValueAsString (userData));
            baseResponse.getResponseBody ().add (userData);
        } catch (BusinessException e) {
            baseResponse.setStatus (e.getStatus ());
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            baseResponse.setStatus (NN_ERROR_CODE);
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
            log.error (e.getMessage ());
        }
        log.info (String.format ("%s%n",
                "*******************************End method getOpenId*******************************"));
        return baseResponse;
    }

    /**
     * 获取账户信息
     *
     * @param openId
     * @return
     */
    @GetMapping(value = "/webInfo/{openId}")
    public BaseResponse findWebInfo (@PathVariable String openId) {
        log.info (String.format ("%s%n",
                "*******************************start method findWebInfo*******************************"));
        Assert.notNull (openId, "[Assertion failed] - this argument [openId] is required; it must not be null");
        BaseResponse baseResponse = new BaseResponse ();
        DefaultAccountInfoVO vo = new DefaultAccountInfoVO ();
        try {
            DefaultAccountInfoDomain domain = userInfoService.findWebInfo (openId);
            BeanUtils.copyProperties (domain, vo);
            vo.setWebUrl (WEB_URL);
            baseResponse.getResponseBody ().add (vo);
        } catch (BusinessException e) {
            baseResponse.setStatus (e.getStatus ());
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            baseResponse.setStatus (NN_ERROR_CODE);
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
            log.error (e.getMessage ());
        }
        log.info (String.format ("%s%n",
                "*******************************End method findWebInfo*******************************"));
        return baseResponse;
    }

    /**
     * 更新账户信息
     *
     * @param openId
     * @param defaultAccountInfoVO
     * @return
     */
    @PutMapping(value = "/webInfo/{openId}")
    public BaseResponse updateWebInfo (@PathVariable String openId, @RequestBody DefaultAccountInfoVO defaultAccountInfoVO) {
        log.info (String.format ("%s%n",
                "*******************************start method updateWebInfo*******************************"));
        Assert.notNull (openId, "[Assertion failed] - this argument [openId] is required; it must not be null");
        Assert.notNull (defaultAccountInfoVO, "[Assertion failed] - this argument [DefaultAccountInfoVO] is required; it must not be null");
        DefaultAccountInfoVO vo = new DefaultAccountInfoVO ();
        defaultAccountInfoVO.setOpen_id (openId);
        BaseResponse baseResponse = new BaseResponse ();
        try {
            if (userInfoService.updateWebInfo (defaultAccountInfoVO) == 1) {
                baseResponse.getResponseBody ().add ("password update successful!");
            }
        } catch (BusinessException e) {
            baseResponse.setStatus (e.getStatus ());
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            baseResponse.setStatus (NN_ERROR_CODE);
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
            log.error (e.getMessage ());
        }
        log.info (String.format ("%s%n",
                "*******************************End method updateWebInfo*******************************"));
        return baseResponse;
    }

    @GetMapping(value = "/webInfo/login")
    public BaseResponse login(@RequestParam String userId,String password){
        String opId ="";
        BaseResponse baseResponse = new BaseResponse ();
        DefaultAccountInfoDomain defaultAccountInfoDomain = new DefaultAccountInfoDomain();
        try {
            defaultAccountInfoDomain.setUser_id (new String(Base64.decode(userId)));
            defaultAccountInfoDomain.setPassword (new String(Base64.decode(password)));
            opId = userInfoService.login (defaultAccountInfoDomain);
            if (!StringUtils.isEmpty (opId)){
                DefaultAccountInfoVO vo = new DefaultAccountInfoVO ();
                vo.setOpen_id (opId);
                vo.setUser_id (userId);
                vo.setPassword (password);
                baseResponse.getResponseBody ().add (vo);
            }else {
                baseResponse.setStatus ("201");
                baseResponse.getResponseBody ().add ("Wrong account or password!");
            }
        } catch (BusinessException e) {
            baseResponse.setStatus (e.getStatus ());
            baseResponse.getResponseBody ().add (e.getArgs ());
        } catch (Exception e) {
            baseResponse.setStatus (NN_ERROR_CODE);
            baseResponse.getResponseBody ().add (NN_ERROR_MSG);
            log.error (e.getMessage ());
        }
        return baseResponse;
    }
}

