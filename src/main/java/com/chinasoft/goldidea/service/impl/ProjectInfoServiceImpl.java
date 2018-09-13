package com.chinasoft.goldidea.service.impl;

import com.chinasoft.goldidea.common.BaseResponse;
import com.chinasoft.goldidea.common.ResponseCode;
import com.chinasoft.goldidea.controller.bean.IdeaDraftInfoVO;
import com.chinasoft.goldidea.controller.bean.ProjectInfoVO;
import com.chinasoft.goldidea.controller.bean.UserInfoVO;
import com.chinasoft.goldidea.domain.ProjectInfoDomain;
import com.chinasoft.goldidea.exception.BusinessException;
import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import com.chinasoft.goldidea.po.ProjectInfoPO;
import com.chinasoft.goldidea.po.UserInfoPO;
import com.chinasoft.goldidea.repository.IdeaDraftInfoRepository;
import com.chinasoft.goldidea.repository.ProjectInfoRepository;
import com.chinasoft.goldidea.repository.UserInfoRepository;
import com.chinasoft.goldidea.service.ProjectInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
@Service
@Slf4j
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private IdeaDraftInfoRepository ideaDraftInfoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Value("${EERConfig.DB_ERROR_MSG}")
    private String DB_ERROR_MSG;

    @Value("${EERConfig.DB_ERROR_CODE}")
    private String DB_ERROR_CODE;

    /**
     * 创建方案信息
     *
     * @param projectInfoDomain
     * @return ProjectInfoVO
     */
    @Override
    public ProjectInfoVO createProjectSerice (ProjectInfoDomain projectInfoDomain) throws Exception {
        //实例化对象
        ObjectMapper objMp = new ObjectMapper ();
        log.info ("projectInfoDomain is:{}", objMp.writeValueAsString (projectInfoDomain));
        ProjectInfoPO projectInfoPO = new ProjectInfoPO ();
        ProjectInfoVO projectInfoVO = null;
        Date time = new Date ();
        try {
            //复制其它参数到PO
            BeanUtils.copyProperties (projectInfoDomain, projectInfoPO);
            //保存时间参数
            projectInfoPO.setProject_create_time (time);
            projectInfoPO.setProjectUpdateTime (time);
            projectInfoPO.setReadingVolume (0);
            projectInfoPO.setSummaryType (projectInfoDomain.getSummaryType () == null ? 0 : projectInfoDomain.getSummaryType ());
            projectInfoPO.setProject_type (projectInfoDomain.getProject_type () == null ? 0 : projectInfoDomain.getProject_type ());
            //初始化状态
            projectInfoPO.setProject_status (projectInfoDomain.getProject_status () == null ? 0 : projectInfoDomain.getProject_status ());
            //保存方案信息
            projectInfoVO = saveIdeaAndUser (projectInfoDomain, projectInfoPO);
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        log.info ("return data is:{}", objMp.writeValueAsString (projectInfoVO));
        return projectInfoVO;
    }

    /**
     * 修改方案信息
     *
     * @param projectInfoDomain
     * @return
     */
    @Override
    public ProjectInfoVO modifyProjectSerice (ProjectInfoDomain projectInfoDomain) throws Exception {
        //实例化对象
        ObjectMapper objMp = new ObjectMapper ();
        ProjectInfoPO projectInfoPO = new ProjectInfoPO ();
        ProjectInfoVO projectInfoVO = null;
        try {
            log.info ("projectInfoDomain is:{}", objMp.writeValueAsString (projectInfoDomain));
            //复制参数到PO
            BeanUtils.copyProperties (projectInfoDomain, projectInfoPO);
            //通过主键查询方案信息
            Optional <ProjectInfoPO> po = projectInfoRepository.findById (projectInfoPO.getId ());
            projectInfoPO.setProject_create_time (po.get ().getProject_create_time ());
            projectInfoPO.setProject_type (projectInfoDomain.getProject_type () == null ? 0 : projectInfoDomain.getProject_type ());
            projectInfoPO.setProjectUpdateTime (new Date ());
            //修改方案信息
            projectInfoVO = saveIdeaAndUser (projectInfoDomain, projectInfoPO);
        } catch (Exception e) {
            log.warn ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        log.info ("return data is:{}", objMp.writeValueAsString (projectInfoVO));
        return projectInfoVO;
    }

    /**
     * 查询方案信息
     *
     * @param projectInfoDomain
     * @return
     */
    @Override
    public List <ProjectInfoVO> viewProjectService (ProjectInfoDomain projectInfoDomain) throws Exception {

        ObjectMapper objMp = new ObjectMapper ();
        log.info ("projectInfoDomain is:{}", objMp.writeValueAsString (projectInfoDomain));
        LinkedList <ProjectInfoVO> listVO = new LinkedList <> ();
        List <ProjectInfoPO> list = null;
        //装载User信息
        UserInfoPO userInfoPO = new UserInfoPO ();
        userInfoPO.setOpen_id (projectInfoDomain.getOpen_id ());
        //装载Idea信息
        IdeaDraftInfoPO ideaDraftInfoPO = new IdeaDraftInfoPO ();
        ideaDraftInfoPO.setIdea_id (projectInfoDomain.getIdea_id ());

        //装载其它字段信息
        ProjectInfoPO projectInfoPO = new ProjectInfoPO ();
        projectInfoPO.setProject_status (projectInfoDomain.getProject_status ());
        projectInfoPO.setSummary (projectInfoDomain.getIdeaInfo ());
        projectInfoPO.setProject_detail (projectInfoDomain.getIdeaInfo ());
        projectInfoPO.setUser (userInfoPO);
        projectInfoPO.setIdea (ideaDraftInfoPO);

        try {
            //按主键降序排列
            Sort sort = new Sort (Sort.Direction.DESC, "projectUpdateTime");
            PageRequest pr = new PageRequest (projectInfoDomain.getCurPage () - 1, projectInfoDomain.getPageSize (), sort);

            //查询接口
            list = projectInfoRepository.findAll (dynamicQueryProjectInfo (projectInfoDomain), pr).getContent ();

            //遍历查询结果
            for (ProjectInfoPO ProjectInfoPO : list) {
                ProjectInfoVO pVO = new ProjectInfoVO ();
                BeanUtils.copyProperties (ProjectInfoPO, pVO);
                pVO.setIdea (copyIdeaProperties (ProjectInfoPO.getIdea ()));
                pVO.setUser (copyUserProperties (ProjectInfoPO.getUser ()));
                pVO.setCount (list.size ());
                if (null != ProjectInfoPO.getProjectUpdateTime ()) {
                    pVO.setProjectUpdateTime (ProjectInfoPO.getProjectUpdateTime ().getTime ());
                }
                listVO.add (pVO);
            }
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }

        log.info ("return data is:{}", objMp.writeValueAsString (list));
        return listVO;
    }

    /**
     * 查询方案总数
     *
     * @return Integer
     * @throws Exception
     */
    @Override
    public Integer findProjectCount () throws Exception {
        return projectInfoRepository.findProjectCount ();
    }

    /**
     * 通过主键查询方案详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public List <ProjectInfoVO> findProjectById (Long id) throws Exception {
        List <ProjectInfoPO> poList = null;
        try {
            poList = projectInfoRepository.findAllById (id);
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        List <ProjectInfoVO> voList = new ArrayList <> ();
        for (ProjectInfoPO po : poList) {
            ProjectInfoVO projectInfoVO = new ProjectInfoVO ();
            BeanUtils.copyProperties (po, projectInfoVO);
            projectInfoVO.setUser (copyUserProperties (po.getUser ()));
            projectInfoVO.setIdea (copyIdeaProperties (po.getIdea ()));
            if (null != po.getProjectUpdateTime ()) {
                projectInfoVO.setProjectUpdateTime (po.getProjectUpdateTime ().getTime ());
            }
            projectInfoRepository.save (po);
            voList.add (projectInfoVO);
        }
        return voList;
    }

    /**
     * idea信息处理
     *
     * @param projectInfoDomain
     * @param projectInfoPO
     * @return
     */
    private ProjectInfoVO saveIdeaAndUser (ProjectInfoDomain projectInfoDomain, ProjectInfoPO projectInfoPO) {
        ProjectInfoVO projectInfoVO = new ProjectInfoVO ();
        //查询Idea信息
        IdeaDraftInfoPO ideaDraftInfoPO = ideaDraftInfoRepository.findByIdeaId (projectInfoDomain.getIdea_id ());
        //判断Idea信息不能为空
        if (null == ideaDraftInfoPO) {
            log.warn ("Reason:{}", "idea Id不存在");
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        //查询User信息
        UserInfoPO userInfoPO = userInfoRepository.findByOpen_id (projectInfoDomain.getOpen_id ());
        //判断User信息不能为空
        if (null == userInfoPO) {
            log.warn ("Reason:{}", "user不存在");
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }
        try {
            //装载Idea和User信息到方案PO中
            projectInfoPO.setIdea (ideaDraftInfoPO);
            projectInfoPO.setUser (userInfoPO);
            log.info (new ObjectMapper ().writeValueAsString (projectInfoPO));
            //保存方案信息
            ProjectInfoPO po = projectInfoRepository.save (projectInfoPO);
            //复制方案信息到VO
            BeanUtils.copyProperties (po, projectInfoVO);
            if ((null != po.getIdea ()) && (null != po.getUser ())) {
                IdeaDraftInfoVO ideaVO = copyIdeaProperties (po.getIdea ());
                UserInfoVO userVO = copyUserProperties (po.getUser ());
                projectInfoVO.setIdea_id (po.getIdea ().getIdea_id ());
                projectInfoVO.setIdea (ideaVO);
                projectInfoVO.setUser (userVO);
                if (null != po.getProjectUpdateTime ()) {
                    projectInfoVO.setProjectUpdateTime (po.getProjectUpdateTime ().getTime ());
                }
            }
        } catch (Exception e) {
            log.error ("Error Message:{}", e.getMessage ());
            throw new BusinessException (DB_ERROR_CODE, DB_ERROR_MSG);
        }

        return projectInfoVO;
    }

    /**
     * PO to VO数据转换
     *
     * @param ideaPO
     * @return
     */
    private IdeaDraftInfoVO copyIdeaProperties (IdeaDraftInfoPO ideaPO) {
        IdeaDraftInfoVO ideaVO = new IdeaDraftInfoVO ();
        BeanUtils.copyProperties (ideaPO, ideaVO);
        return ideaVO;
    }

    /**
     * PO to VO数据转换
     *
     * @param userPO
     * @return
     */
    private UserInfoVO copyUserProperties (UserInfoPO userPO) {
        UserInfoVO userVO = new UserInfoVO ();
        BeanUtils.copyProperties (userPO, userVO);
        return userVO;
    }

    /**
     * 动态查询
     *
     * @param domain
     * @return Specification <ProjectInfoPO>
     */
    public Specification <ProjectInfoPO> dynamicQueryProjectInfo (final ProjectInfoDomain domain) {
        return new Specification <ProjectInfoPO> () {

            @Override
            public Predicate toPredicate (Root <ProjectInfoPO> root, CriteriaQuery <?> query,
                                          CriteriaBuilder builder) {
                List <Predicate> list = new ArrayList <Predicate> ();

                if (!StringUtils.isEmpty (domain.getIdeaInfo ())) {
                    list.add (builder.or (builder.like (root.get ("summary"), "%" + domain.getIdeaInfo () + "%"), builder.like (root.get ("project_detail"), "%" + domain.getIdeaInfo () + "%")));
                }
                if (!StringUtils.isEmpty (domain.getOpen_id ())) {
                    list.add (builder.and (builder.equal (root.get ("user").get ("open_id").as (String.class), domain.getOpen_id ())));
                }
                if (!StringUtils.isEmpty (domain.getIdea_id ())) {
                    list.add (builder.and (builder.equal (root.get ("idea").get ("idea_id").as (String.class), domain.getIdea_id ())));
                }
                if (!StringUtils.isEmpty (domain.getProject_status ())) {
                    list.add (builder.and (builder.equal (root.get ("project_status"), domain.getProject_status ())));
                }
                Predicate[] p = new Predicate[list.size ()];
                query.where (builder.and (list.toArray (p)));
                return null;
            }
        };

    }

    public BaseResponse readingVolume (Long projectId) {
        ProjectInfoPO projectInfoPO = projectInfoRepository.findByProjectId (projectId);
        Integer readingVolume = projectInfoPO.getReadingVolume ();
        if (StringUtils.isEmpty (readingVolume)) {
            readingVolume = 0;
        }
        projectInfoPO.setReadingVolume (readingVolume + 1);
        ProjectInfoPO resultPo = projectInfoRepository.save (projectInfoPO); // 更新阅读量字段
        if (resultPo != null) {
            return BaseResponse.createBySuccess ();
        } else {
            return BaseResponse.createByErrorMessage (ResponseCode.DB_ERROR.getCode (), ResponseCode.DB_ERROR.getDesc ());
        }

    }
}
