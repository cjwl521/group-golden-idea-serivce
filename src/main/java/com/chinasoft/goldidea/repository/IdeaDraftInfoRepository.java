package com.chinasoft.goldidea.repository;

import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IdeaDraftInfoRepository extends JpaRepository<IdeaDraftInfoPO, Long> {

    @Query(value="select Count(open_id) from IdeaDraftInfoPO where open_id=?1")
    public Integer countByOpen_id(String openid);
    @Query(value="select idf from IdeaDraftInfoPO idf where idf.idea_id=?1")
    public IdeaDraftInfoPO findByIdeaId(Long ideaId);

    @Query(value="select idf from IdeaDraftInfoPO idf where idf.open_id=?1")
    public List<IdeaDraftInfoPO> findByOpen_id(String openId);

    @Query(value="select * from golden_idea_draft_info  LEFT JOIN (select idea_id as i from project_db_info ) as pdii ON golden_idea_draft_info.idea_id=pdii.i where golden_idea_draft_info.open_id=?1 and pdii.i IS NULL",nativeQuery = true)
    public List<IdeaDraftInfoPO> findIdeaNotProject(String openId);

}
