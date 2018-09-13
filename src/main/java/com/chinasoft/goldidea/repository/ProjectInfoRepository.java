package com.chinasoft.goldidea.repository;

import com.chinasoft.goldidea.common.BaseRepository;
import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import com.chinasoft.goldidea.po.ProjectInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/8/22
 */
public interface ProjectInfoRepository extends BaseRepository<ProjectInfoPO, Long> {


    // 查询 projectStatus 字段为0数据的数量
    @Query(value="select Count(po) from ProjectInfoPO po where idea_id=?1 and project_status=?2")
    Integer countrlsProjectCount(IdeaDraftInfoPO idea_id, Integer projectStatus);

    @Query(value="select Count(po) from ProjectInfoPO po")
    Integer findProjectCount();

    @Query(value="select Count(po) from ProjectInfoPO po where idea_id=?1")
    Integer findProjectCountByIdeaId(IdeaDraftInfoPO idea_id);

    List <ProjectInfoPO> findAllById (Long iterable);

    @Query(value="select po from ProjectInfoPO po where po.id=?1")
    ProjectInfoPO findByProjectId(Long id);

}
