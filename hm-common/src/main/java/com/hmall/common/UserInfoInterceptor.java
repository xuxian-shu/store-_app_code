package com.hmall.common;


import cn.hutool.core.util.StrUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取登录用户信息
        String userInfo = request.getHeader("userInfo");

        //2.判断是否获取了用户，如果有，存人ThreadLocal
        if(StrUtil.isNotBlank(userInfo)) {//如果有用户信息
            UserContext.setUser(Long.parseLong(userInfo));
        }
        //3.放行
        return true;
    }

    //4.请求完成后，清除ThreadLocal
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
