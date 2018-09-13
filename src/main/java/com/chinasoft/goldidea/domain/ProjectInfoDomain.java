package com.chinasoft.goldidea.domain;

import com.chinasoft.goldidea.common.PageModel;
import lombok.Data;

import java.util.Date;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
@Data
public class ProjectInfoDomain extends PageModel {

    private Long id;
    private Long idea_id;
    private String summary;
    private Integer summaryType;
    private String project_detail;
    private Integer project_type;
    private Integer project_status;
    private String nick_name;
    private String open_id;
    private String ideaInfo;
    private Date projectUpdateTime;
    // 阅读次数
    private Integer readingVolume;

}
