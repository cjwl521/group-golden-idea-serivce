package com.chinasoft.goldidea.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "golden_idea_draft_info")
@Data
public class IdeaDraftInfoPO {

    @Id
    @GeneratedValue // 自增序列
    private Long idea_id;
    private String idea;
    private String open_id;
    private Integer status;
    @JoinColumn(name = "create_time", nullable = false)
    private Date createTime;
    @JoinColumn(name = "update_time", nullable = false)
    private Date updateTime;
    private String filePath;


}
