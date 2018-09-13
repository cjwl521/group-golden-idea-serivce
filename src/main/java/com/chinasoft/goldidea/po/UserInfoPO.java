package com.chinasoft.goldidea.po;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="wechat_user_info")
@Data
public class UserInfoPO {

    @Id
    @GeneratedValue // 自增序列
    private Long user_id;
    //微信code
    @Column(nullable = false)
    private String open_id;
    //session_key
    private String session_key;
    //头像信息
    private String head_url;
    //昵称
    @Column(nullable = false)
    private String nick_name;
    private String city;
    private String sex;
    private String country;
    private String province;
}
