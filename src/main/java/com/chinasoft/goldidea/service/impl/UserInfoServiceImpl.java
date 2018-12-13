package com.chinasoft.goldidea.service.impl;

import com.chinasoft.goldidea.common.PrimaryGenerater;
import com.chinasoft.goldidea.controller.bean.DefaultAccountInfoVO;
import com.chinasoft.goldidea.controller.bean.UserInfoVO;
import com.chinasoft.goldidea.domain.DefaultAccountInfoDomain;
import com.chinasoft.goldidea.domain.UserInfoDomain;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.po.DefaultAccountInfoPO;
import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import com.chinasoft.goldidea.po.UserInfoPO;
import com.chinasoft.goldidea.repository.DefaultAccountInfoRepository;
import com.chinasoft.goldidea.repository.UserInfoRepository;
import com.chinasoft.goldidea.service.UserInfoService;
import com.chinasoft.goldidea.service.bean.UserData;
import com.chinasoft.goldidea.util.HttpsRequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DefaultAccountInfoRepository defaultAccountInfoRepository;

    @Override
    public List <UserInfoPO> findAll () {
        return userInfoRepository.findAll ();
    }

    @Value("${WXConfig.APPID}")
    private String APPID;

    @Value("${WXConfig.APPSECRECT}")
    private String APP_SECRECT;

    @Value("${WXConfig.GRANTTYPE}")
    private String GRANT_TYPE;

    @Value("${WXConfig.WXURL}")
    private String WX_URL;

    @Value("${EERConfig.WX_ERROR_CODE}")
    private String WX_ERROR_CODE;

    @Value("${EERConfig.DB_ERROR_CODE}")
    private String DB_ERROR_CODE;

    @Value("${EERConfig.WX_ERROR_MSG}")
    private String WX_ERROR_MSG;

    @Value("${EERConfig.DB_ERROR_MSG}")
    private String DB_ERROR_MSG;

    @Autowired
    private HttpsRequestUtil http;

    /**
     * 获取并绑定用户OpenId
     *
     * @param userInfoDomain
     * @return UserInfoVO
     */
    @Override
    @Transactional
    public UserInfoVO getOpenId (UserInfoDomain userInfoDomain) {
        //参数初始化
        UserInfoVO userInfoVO = new UserInfoVO ();
        UserInfoPO userInfoPO = new UserInfoPO ();
        UserData userData = null;

        //创建请求路径
        String requestUrl = WX_URL.replace ("APPID", APPID).replace ("SECRET", APP_SECRECT)
                .replace ("JSCODE", userInfoDomain.getWxId ()).replace ("authorization_code", GRANT_TYPE);
        log.info ("requestUrl is {}", requestUrl);

        //通过HTTPS请求微信接口
        try {
            userData = new ObjectMapper ().readValue (http.httpsGetRequest (requestUrl, String.class), UserData.class);
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (WX_ERROR_CODE, WX_ERROR_MSG);
        }

        //装载参数
        userInfoDomain.setOpen_id (userData.getOpenid ());
        userInfoDomain.setSession_key (userData.getSession_key ());
        BeanUtils.copyProperties (userInfoDomain, userInfoPO);

        //绑定user与OpenID关系，保存至数据库
        try {
            if (userInfoRepository.findCountByOpen_id (userData.getOpenid ()) < 1) {
                userInfoPO.setHead_url (userInfoDomain.getHead_url ());
                userInfoRepository.save (userInfoPO);
                this.addDefaultAccountInfo (userData.getOpenid ());
            }
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        userInfoVO.setOpen_id (userData.getOpenid ());
        userInfoVO.setSession_key (userData.getSession_key ());
        return userInfoVO;
    }

    /**
     * 新增默认账户信息
     *
     * @param openId
     */
    @Override
    public void addDefaultAccountInfo (String openId) {

        DefaultAccountInfoPO defaultAccountInfoPO = new DefaultAccountInfoPO ();
        //查询默认账户用户列表信息
        List <String> all = defaultAccountInfoRepository.findAllAccount ();
        //调用接口生成默认账户
        defaultAccountInfoPO.setUser_id (PrimaryGenerater.getInstance ().getUserIds (all));
        //调用接口生成默认密码
        defaultAccountInfoPO.setPassword (PrimaryGenerater.getInstance ().getPasswords (8));
        //将新的用户数据存入数据库
        try {
            defaultAccountInfoPO.setOpen_id (openId);
            log.info ("requestData is {}", new ObjectMapper ().writeValueAsString (defaultAccountInfoPO));
            defaultAccountInfoRepository.save (defaultAccountInfoPO);
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
    }

    /**
     * 查询Web账户信息
     * @param openId
     * @return DefaultAccountInfoDomain
     */
    @Override
    public DefaultAccountInfoDomain findWebInfo (String openId) {
        DefaultAccountInfoDomain domain = new DefaultAccountInfoDomain ();
        try {
            log.info ("request openId is {}",openId);
            DefaultAccountInfoPO po = defaultAccountInfoRepository.findByOpenId (openId).get (0);
            BeanUtils.copyProperties (po, domain);
        }catch (Exception e){
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        return domain;
    }

    /**
     * 修改Web账户信息
     * @param defaultAccountInfoVO
     * @return DefaultAccountInfoDomain
     */
    public Integer updateWebInfo(DefaultAccountInfoVO defaultAccountInfoVO) {
        DefaultAccountInfoPO defaultAccountInfoPO = new DefaultAccountInfoPO ();
        DefaultAccountInfoDomain domain = new DefaultAccountInfoDomain ();
        defaultAccountInfoPO.setOpen_id (defaultAccountInfoVO.getOpen_id ());
        Integer result = 0;
        try {
            result = defaultAccountInfoRepository.updateByOpenId (defaultAccountInfoVO.getOpen_id (),defaultAccountInfoVO.getPassword ());
        }catch (Exception e){
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        return result;
    }

    /**
     * 登录验证获取OpenId
     * @param defaultAccountInfoDomain
     * @return
     */
    @Override
    public String login (DefaultAccountInfoDomain defaultAccountInfoDomain) {
        String opId = "";
        try {
            log.info ("requestData is {}", new ObjectMapper ().writeValueAsString (defaultAccountInfoDomain));
            opId = defaultAccountInfoRepository.findByUserIdAndPassword (defaultAccountInfoDomain.getUser_id (),defaultAccountInfoDomain.getPassword ());
            log.info ("opId:{}",opId);
        }catch (Exception e){
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        return opId;
    }
}
