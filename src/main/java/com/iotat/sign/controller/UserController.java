package com.iotat.sign.controller;

import com.alibaba.fastjson.JSONObject;
import com.iotat.sign.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;
    // @Autowired
    // public RedisTool redis;

    @ApiOperation("登录")
    @PostMapping("/login")
    public String login(@RequestBody JSONObject json){
        String userName = json.getString("userName");
        String password = json.getString("password");
        // System.out.println(userName);
        // System.out.println(password);
        return userService.login(userName,password);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public String register(@RequestBody String json){
        JSONObject obj = JSONObject.parseObject(json);
        return userService.register(obj);
    }

    @ApiOperation("连续签到次数")
    @GetMapping("/totalSign")
    public String totalSign(@RequestHeader("token") String token) throws ParseException {
        // System.out.println(token);
        return userService.totalSign(token);
    }

    @ApiOperation("签到")
    @GetMapping("/sign")
    public String sign(@RequestHeader("token") String token) throws ParseException {
        return userService.sign(token);
    }

    @ApiOperation("退出")
    @GetMapping("/loginOut")
    public String loginOut(@RequestHeader("token") String token){
        return userService.loginOut(token);
    }

    // @ApiOperation("验证码")
    // @PostMapping("/captcha")
    // public String getCaptcha(@RequestParam("key") String random){
    //     // LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200,100);
    //     // redis.set(random,lineCaptcha.getCode());
    //     // JSONObject data = new JSONObject();
    //     // data.put("img64",lineCaptcha.getImageBase64());
    //     // return Result.resultData(200,data);
    //     return null;
    // }
}
