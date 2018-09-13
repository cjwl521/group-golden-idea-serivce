package com.chinasoft.goldidea.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
@Entity
@Table(name="project_db_Info")
@Data
public class ProjectInfoPO {
    @Id
    @GeneratedValue // 自增序列
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idea_id", nullable = false)
    private IdeaDraftInfoPO idea;
    private String summary;
    @Column(name="summary_type",columnDefinition="tinyint default 0")
    private Integer summaryType=0;
    @Lob
    @Column(columnDefinition="TEXT")
    private String project_detail;
    private Integer project_type=0;
    private Integer project_status=0;
    private String nick_name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfoPO user;
    private Date project_create_time;
    @Column(name="project_update_time")
    private Date projectUpdateTime;

    @Column(name="reading_volume")
    private Integer readingVolume;
    private String filePath;

}
