package com.chinasoft.goldidea.controller.bean;

import com.chinasoft.goldidea.common.PageModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
@Data
public class ProjectInfoVO extends PageModel {

    @JsonProperty("projectId")
    private Long id;

    @JsonProperty("userId")
    private Long user_id;

    @JsonProperty("ideaId")
    private Long idea_id;

    private IdeaDraftInfoVO Idea;

    private UserInfoVO user;

    private String summary;

    @JsonProperty("summaryType")
    private Integer summaryType;

    @JsonProperty("projectDetail")
    private String project_detail;

    @JsonProperty("projectType")
    private Integer project_type;

    @JsonProperty("nickName")
    private String nick_name;

    @JsonProperty("openId")
    private String open_id;

    @JsonProperty("projectStatus")
    private Integer project_status;

    @JsonProperty("projectUpdateTime")
    private long projectUpdateTime;
    // 阅读次数
    private Integer readingVolume;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filePath;


}
