package com.chinasoft.goldidea.service;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.controller.bean.ProjectInfoVO;
import com.chinasoft.goldidea.domain.ProjectInfoDomain;

import java.util.List;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
public interface ProjectInfoService {

    /**
     * 创建方案信息
     * @param projectInfoDomain
     * @return
     */
    public ProjectInfoVO createProjectSerice(ProjectInfoDomain projectInfoDomain)throws Exception;

    /**
     * 修改方案信息
     * @param projectInfoDomain
     * @return
     */
    public ProjectInfoVO modifyProjectSerice(ProjectInfoDomain projectInfoDomain) throws Exception;

    /**
     * 查询方案信息
     * @param projectInfoDomain
     * @return
     */
    public List<ProjectInfoVO> viewProjectService(ProjectInfoDomain projectInfoDomain) throws Exception;

    /**
     * 查询方案总数
     * @return
     * @throws Exception
     */
    public Integer findProjectCount() throws Exception;

    /**
     * 通过主键查询方案详情
     * @param id
     * @return List <ProjectInfoVO>
     * @throws Exception
     */
    List <ProjectInfoVO> findProjectById (Long id) throws Exception;

    /**
     * 计算阅读数量
     * @param projectId
     * @return
     */
    BaseResponse readingVolume(Long projectId);
}
