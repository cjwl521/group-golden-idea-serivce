package com.chinasoft.goldidea.service;

import com.chinasoft.goldidea.controller.bean.DefaultAccountInfoVO;
import com.chinasoft.goldidea.controller.bean.UserInfoVO;
import com.chinasoft.goldidea.domain.DefaultAccountInfoDomain;
import com.chinasoft.goldidea.domain.UserInfoDomain;
import com.chinasoft.goldidea.po.UserInfoPO;

import java.util.List;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
public interface UserInfoService {
    /**
     * 查询所有用户信息
     * @return
     */
    public List<UserInfoPO> findAll();

    /**
     * 记录用户信息，查询openID
     * @param userInfoDomain
     * @return
     */
    public UserInfoVO getOpenId(UserInfoDomain userInfoDomain);

    /**
     * 新增默认账户信息
     * @param openId
     */
    public void addDefaultAccountInfo(String openId);

    /**
     * 查询Web账户信息
     * @param openId
     * @return
     */
    public DefaultAccountInfoDomain findWebInfo(String openId);

    /**
     * 修改Web账户信息
     * @param defaultAccountInfoVO
     * @return Integer
     */
    public Integer updateWebInfo(DefaultAccountInfoVO defaultAccountInfoVO);

    /**
     * 登录验证获取OpenId
     * @param defaultAccountInfoDomain
     * @return
     */
    public String login(DefaultAccountInfoDomain defaultAccountInfoDomain);
}
