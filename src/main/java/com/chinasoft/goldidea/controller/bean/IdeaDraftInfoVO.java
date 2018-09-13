package com.chinasoft.goldidea.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class IdeaDraftInfoVO {

    @JsonProperty("ideaId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long idea_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String idea;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("openId")
    private String open_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("createTime")
    private Long createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("updateTime")
    private Long updateTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filePath;
}
