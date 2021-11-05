package com.sso.ssovip.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 页面跳转逻辑
 */
@Controller
@RequestMapping ("/view")
public class viewController {
    @Autowired
    private RestTemplate restTemplate;
    private final String USER_INFO_ADDRESS="http://www.ssosys.com:9000/login/info?token=";

    @GetMapping ("/index")
    public String toIndex(@CookieValue(required = false,value="TOKEN") Cookie cookie, HttpSession session){
        if(cookie!=null){
            String token=cookie.getValue();
            if(!StringUtils.isEmpty(token)){
                Map result= restTemplate.getForObject(USER_INFO_ADDRESS + token, Map.class);
                session.setAttribute("loginUser",result);
            }
        }
        return "index";
    }//返回视图的名字index
}
