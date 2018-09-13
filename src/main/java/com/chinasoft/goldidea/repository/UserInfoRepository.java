package com.chinasoft.goldidea.repository;

import com.chinasoft.goldidea.po.UserInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserInfoRepository extends JpaRepository<UserInfoPO,Long>{

    @Query(value="select idf from UserInfoPO idf where open_id=?1")
    public UserInfoPO findByOpen_id(String open_id);

    @Query(value="select Count(open_id) from UserInfoPO where open_id=?1")
    public Integer findCountByOpen_id(String openid);
}
