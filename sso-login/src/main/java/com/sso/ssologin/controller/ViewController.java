package com.sso.ssologin.controller;

import com.sso.ssologin.pojo.User;
import com.sso.ssologin.utils.LoginCacheUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * 页面跳转逻辑
 */
@Controller
@RequestMapping ("/view")
public class ViewController {
    /**
     * 跳转登录界面
     * @return
     */
    @GetMapping ("/login")
    public String toLogin(@RequestParam(required = false,defaultValue = "") String target,
                          HttpSession session,
                          @CookieValue(required = false,value = "TOKEN") Cookie cookie){//不是必须的 有就获取 没有就不获取


        if(StringUtils.isEmpty(target) ){
            target = "http://A.ssosys.com:9010/";
        }
        //如果是已经登录的用户再次访问登录系统 就要重定向
        if(cookie!=null){
            String value=cookie.getValue();
            User user= LoginCacheUtil.loginUser.get(value);
            if(user!=null){
                return "redirect:"+ target;
            }
        }

        //TODO:要做target地址是否合法的校验
        //重定向地址
        session.setAttribute("target",target);
        return "login";
    }
}
