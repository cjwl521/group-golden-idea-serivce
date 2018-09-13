package com.chinasoft.goldidea.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class UserInfoDomain {
    public UserInfoDomain () {
    }
    //微信code
    private String wxId;
    //头像信息
    private String head_url;
    //昵称
    private String nick_name;
    //性别
    private String sex;
    //国家
    private String country;
    //省份
    private String province;
    //城市
    private String city;
    //openId
    private String open_id;

    private String session_key;

}
