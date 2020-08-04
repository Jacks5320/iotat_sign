package com.iotat.sign.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iotat.sign.dao.SignDao;
import com.iotat.sign.dao.UserDao;
import com.iotat.sign.entity.SignRecord;
import com.iotat.sign.entity.User;
import com.iotat.sign.service.UserService;
import com.iotat.sign.utils.MD5;
import com.iotat.sign.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    SignDao signDao;

    // /**
    //  * 谷歌提供的一个用于缓存的api
    //  * 缓存过期：30min
    //  */
    // public static Cache<String, String> localCache = CacheBuilder
    //         .newBuilder().maximumSize(1000)
    //         .expireAfterAccess(30, TimeUnit.MINUTES).build();

    /**
     * 登录
     * @param userName   用户名
     * @param password   密码
     * @return      状态码和数据
     * 状态码：
     *      201 登录出错
     *      200 登录成功
     */
    @Override
    public String login(String userName, String password) {
        //判断前端请求参数是否为空
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            return Result.resultStatus(201,"账号不能为空！");
        }
        //查询用户是否存在
        User user = userDao.findUserByUsername(userName);
        if (user == null)
            return Result.resultStatus(201,"用户不存在！");
        //查询密码编码是否正确MD5
        if (!user.getPassword().equals(MD5.encodeByMd5(password)))
            return Result.resultStatus(201,"密码输入错误！");
        //查询验证码是否正确

        //生成标识用户的token令牌
        String token = MD5.encodeByMd5(UUID.randomUUID().toString());

        /**
         * 保存用户id和userName，作为值put进data，随随后作为值存入data
         *  SONObject的put是栈操作，先put后push
         */
        JSONObject u = new JSONObject();
        u.put("userId",user.getId());
        u.put("nickName",user.getUsername());

        userDao.updateToken(token,user.getUsername());
        /**
         *  返回给前端的数据，data
         *  包含（userName，userId），token值
         */
        JSONObject data = new JSONObject();
        data.put("token",token);
        data.put("user",u);

        //最终返回的数据有：msg，code，data{user，token}
        return Result.resultData(200,data);
    }

    /**
     * 注册
     * @param
     * @return 注册状态信息
     * 状态码：
     *      200 注册成功
     *      201 注册失败
     */
    @Override
    public String register(JSONObject obj) {
        //空值判断
        if (obj == null) return Result.resultStatus(201,"参数必须完整填写!");

        String userName = obj.getString("userName");
        String password = obj.getString("password");

        //非空判断
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password))
            return Result.resultStatus(201,"参数必填完整填写！");

        //验证账户是否存在
        if (userDao.findUserByUsername(userName) != null)
            return Result.resultStatus(201,"账户已经存在！");

        User u = new User();
        u.setUsername(userName);
        u.setPassword(MD5.encodeByMd5(password));
        u.setPlainPassword(password);
        userDao.save(u);
        userDao.saveScore(u.getId(),0);
        return Result.resultStatus(200,"注册成功");
    }

    /**
     * 查询最后一次签到情况
     * @param token
     * @return
     * 状态码：
     * num:  最近签到记录的次数
     * isCheck:
     *          0   今日未签到
     *          1   今天已签到
     */
    @Override
    public String totalSign(String token) throws ParseException {

        User user = userDao.findUserByToken(token);

        JSONObject data = new JSONObject();

        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月日，用于转换成凌晨毫秒值
        data.put("score",userDao.findScoreById(user.getId()));//通过id查询积分
        if (signDao.findFinalSign(user.getId()) == null){   //没有签到记录，返回num = 0
            data.put("msg","重未签到过!");
            data.put("num",0);
            data.put("isCheck",0);
        }else {     //有签到记录

            SignRecord signRecord = signDao.findFinalSign(user.getId());//找到最近一条记录

            data.put("c_time",signRecord.getTime());//返回最后一次时间
            Date day1 = new Date();//保存当前时间
            Date day2 = d.parse(d.format(d.parse(signRecord.getTime())));  //最后一次签到时间的凌晨毫秒值
            long timeMs = day1.getTime() - day2.getTime();//当前时间与上次时间凌晨相差值
            if (timeMs > 172800000){//超过48h
                data.put("num",0);
                data.put("msg","上一次签到超过48小时");
                data.put("isCheck",0);
            }else if(timeMs > 86400000){//超过24小时，不超过48小时
                data.put("num",signRecord.getNum());
                data.put("msg","上一次签到是昨天");
                data.put("isCheck",0);
            }else {//不超过24小时
                data.put("num",signRecord.getNum());
                data.put("msg","上一次签到是今天");
                data.put("isCheck",1);
            }
        }
        return Result.resultData(200,data);
    }

    /**
     * 签到
     * @param   token
     * @return    签到情况
     * @throws ParseException   时间转换异常
     */
    @Override
    public String sign(String token) throws ParseException {

        User user = userDao.findUserByToken(token);
        SignRecord s = new SignRecord();
        JSONObject data = new JSONObject();

        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");
        Date day = new Date();

        if (signDao.findFinalSign(user.getId()) == null){   //没有签到记录
            s.setUserId(user.getId());
            s.setNum(1);
            s.setTime(d.format(new Date()));
            data.put("msg","第一次签到，签到成功！");
            data.put("num",1);
            signDao.save(s);
        }else{               //有记录
            s = signDao.findFinalSign(user.getId());//根据ID查记录
            Date day2 = d2.parse(s.getTime());//保存上次签到时间Date
            day2 = d2.parse(d2.format(day2));//转成上次签到时间的凌晨值
            SignRecord s2 = new SignRecord();//用于保存一条新的签到记录

            long timeMs = day.getTime() - day2.getTime();//计算出差值
            if (timeMs > 172800000){//超过48h
                s2.setUserId(user.getId());
                s2.setNum(1);
                s2.setTime(d.format(day));
                signDao.save(s2);
                data.put("msg","签到成功,签到不连续");
            }else if(timeMs > 86400000) {//上次签到是昨天

                if (s.getNum() < 7){//小于7次
                    s2.setUserId(user.getId());
                    s2.setNum(s.getNum() + 1);
                    s2.setTime(d.format(day));
                    signDao.save(s2);
                    data.put("msg","连续签到中");
                }else {//上次签到是第7次

                    s2.setUserId(user.getId());
                    s2.setNum(1);
                    s2.setTime(d.format(day));
                    userDao.updateScore(userDao.findScoreById(user.getId()) + 1,user.getId());
                    signDao.save(s2);
                    data.put("msg","连续签到超过7次,积一分");
                }
            }else {
                data.put("msg","今天签过到了，请明天再来");
            }
        }
        return Result.resultData(200,data);
    }

    /**
     * 退出登录状态，清空token
     * @param token
     * @return 反馈信息
     */
    public String loginOut(String token){

        User user = userDao.findUserByToken(token);
        if (user == null){
            return Result.resultStatus(104,"请先登录");
        }else {
            userDao.updateToken(null,user.getUsername());
            return Result.resultStatus(200,"退出成功");
        }
    }
}
