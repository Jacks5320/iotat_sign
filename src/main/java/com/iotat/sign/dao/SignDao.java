package com.iotat.sign.dao;

import com.iotat.sign.entity.SignRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SignDao extends JpaRepository<SignRecord,Long> {

    /**
     * 根据uid查询
     * @param userId
     * @return
     */
    @Query("from SignRecord s where s.userId = ?1")
    SignRecord findRecordByUserId(Long userId);

    /**
     * 根据uid查询最近一条记录
     * @param userId
     * @return
     */
    @Query(value = "SELECT * FROM tb_sign t WHERE t.user_id = ?1 ORDER BY c_time DESC LIMIT 1;",nativeQuery = true)
    SignRecord findFinalSign(long userId);

    /**
     * 根据时间和uid查找
     */
    @Query(value = "SELECT * FROM tb_sign t WHERE t.c_time LIKE ?1 and t.user_id = ?2",nativeQuery = true)
    SignRecord findByUserIdAndDateTime(String date,long uid);
}
