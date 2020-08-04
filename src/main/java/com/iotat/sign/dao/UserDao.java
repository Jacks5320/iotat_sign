package com.iotat.sign.dao;

import com.iotat.sign.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {

    /**
     * 根据userName查询数据库记录
     * @param userName
     * @return
     */
    @Query(value = "select * from tb_user u where BINARY u.username = ?1",nativeQuery = true)
    User findUserByUsername(String userName);

    /**
     * 根据token查询user
     * @param token
     * @return
     */
    @Query(value = "select * from tb_user u where u.token = ?1",nativeQuery = true)
    User findUserByToken(String token);

    /**
     * 更新token令牌
     * @param token
     * @param userName
     */
    @Modifying
    @Query(value = "update User u set u.token=?1 where  u.username=?2")
    void updateToken(String token,String userName);

    /**
     * 查找id积分值
     * @param id
     */
    @Query(value = "select n.score from tb_score n where n.user_id = ?1",nativeQuery = true)
    int findScoreById(long id);

    /**
     * 保存Score
     * @param id
     * @param score
     * @return
     */
    @Transactional//提交事务表单
    @Modifying
    @Query(value = "insert into tb_score(user_id,score) value (?1,?2)",nativeQuery = true)
    void saveScore(long id,int score);
    /**
     * 更新Score
     * @param score
     * @param userId
     */
    @Transactional//提交事务表单
    @Modifying
    @Query(value = "update tb_score t set t.score = ?1 where t.user_id = ?2",nativeQuery = true)
    int updateScore(int score,long userId);

    @Transactional
    @Modifying
    @Query(value = "insert into tb_user(username,password,plain_password,token) value (?1,?2,?3,?4) ",nativeQuery = true)
    int saveUser(String username,String psw,String ppsw,String token);


}












