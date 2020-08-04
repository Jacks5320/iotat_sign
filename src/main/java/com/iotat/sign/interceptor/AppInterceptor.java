package com.iotat.sign.interceptor;

import com.iotat.sign.dao.UserDao;
import com.iotat.sign.entity.User;
import com.iotat.sign.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api 拦截器
 * @author xty
 *
 */
@Component
public class AppInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    UserDao userDao;

	@Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)throws Exception {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Headers,Authorization,token");
        response.setHeader("Access-Control-Max-Age", "1800");//30 min
		response.setContentType("application/json; charset=utf-8");

		String uri = request.getServletPath();

        /**
         * indexOf() = -1 表示没有出现过
         * 过滤以下出现过的字符串的url
         */
		if (uri.indexOf("/api/user/login") != -1){
			return true;
		}
        if (uri.indexOf("/api/user/regist") != -1){
            return true;
        }

        /**
         * 用户登录过后会生成一个token
         * 以下代码用于拦截查询不到token的用户操作
         */
        String token = request.getHeader("token");
        //判断token是否为空
        System.out.println("获取的token：" + token);
        if (token.equals("")) return false;
        if(StringUtils.isBlank(token)) {
        	response.getWriter().write(Result.resultStatus(103,"还没登录!"));
			 return false;
        }

        User user = userDao.findUserByToken(token);
        if(user == null) {
        	response.getWriter().write(Result.resultStatus(103,"未登录!"));
			 return false;
        }
        return true;
    }
}