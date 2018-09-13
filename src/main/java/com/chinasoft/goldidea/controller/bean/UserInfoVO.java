package com.chinasoft.goldidea.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class UserInfoVO {
    public UserInfoVO () {
    }
    //微信code
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wxId;
    //头像信息
    @JsonProperty("headUrl")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String head_url;
    //昵称
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("nickName")
    private String nick_name;
    //性别
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sex;
    //国家
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;
    //省份
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String province;
    //城市
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;
    //openId
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("openId")
    private String open_id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sessionKey")
    private String session_key;

}
