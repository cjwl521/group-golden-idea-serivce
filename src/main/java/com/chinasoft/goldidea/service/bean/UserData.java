package com.chinasoft.goldidea.service.bean;

import lombok.Data;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/21
 */
@Data
public class UserData {
    /**
     * wx session key
     */
    private String session_key;
    /**
     * wx openid
     */
    private String openid;
}
