package com.iotat.sign.service;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

public interface UserService {
    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    String login(String account,String password);

    /**
     * 注册
     * @param
     * @return
     */
    String register(JSONObject obj);
    // String regist(String userName,String password);

    /**
     * 查询昨日的累计签到数
     * @param token
     * @return
     */
    String totalSign(String token) throws ParseException;

    /**
     * 签到
     */
    String sign(String token) throws ParseException;

    /**
     * 退出用户
     * @param token
     * @return
     */
    String loginOut(String token);
}
