package com.chinasoft.goldidea.domain;

import lombok.Data;

@Data
public class IdeaDraftInfoDomain {

    public IdeaDraftInfoDomain() { }
    private Long id;
    private String summary;
    private Integer project_status;
    private Integer project_type;
    private String project_detail;
    private String project_update_time;
    private String open_id;
    private String nickname;
    private String head_url;
}
