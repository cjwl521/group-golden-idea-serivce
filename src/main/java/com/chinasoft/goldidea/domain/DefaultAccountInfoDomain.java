package com.chinasoft.goldidea.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/9/6
 */
@Data
public class DefaultAccountInfoDomain {

    private String open_id;
    private String user_id;
    private String password;
}
