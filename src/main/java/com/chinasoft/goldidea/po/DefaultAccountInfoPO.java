package com.chinasoft.goldidea.po;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/9/6
 */
@Data
@Entity
@Table(name="default_account_info")
public class DefaultAccountInfoPO {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String open_id;
    @Column(unique = true, nullable = false)
    private String user_id;
    @Column(name = "security", nullable = false)
    private String password;
}
