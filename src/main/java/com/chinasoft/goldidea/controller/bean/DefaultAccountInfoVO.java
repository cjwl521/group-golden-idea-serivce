package com.chinasoft.goldidea.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/9/6
 */
@Data
public class DefaultAccountInfoVO {

    @JsonProperty("openId")
    private String open_id;
    @JsonProperty("userId")
    private String user_id;
    private String password;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String webUrl;
}
