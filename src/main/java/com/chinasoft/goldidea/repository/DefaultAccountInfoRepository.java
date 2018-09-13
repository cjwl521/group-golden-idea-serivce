package com.chinasoft.goldidea.repository;

import com.chinasoft.goldidea.po.DefaultAccountInfoPO;
import com.chinasoft.goldidea.po.IdeaDraftInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/9/6
 */
public interface DefaultAccountInfoRepository extends JpaRepository<DefaultAccountInfoPO, Long> {
    @Query("select po.user_id from DefaultAccountInfoPO po")
    public List<String> findAllAccount();

    @Query("select po from DefaultAccountInfoPO po where open_id = ?1")
    public List<DefaultAccountInfoPO> findByOpenId (String openId);

    @Transactional
    @Modifying
    @Query("update DefaultAccountInfoPO po set po.password = ?2 where po.open_id = ?1")
    public Integer updateByOpenId (String openId , String password);

    @Query("select po.open_id from DefaultAccountInfoPO po where po.user_id = ?1 and po.password = ?2")
    public String findByUserIdAndPassword(String user_id, String password);
}
