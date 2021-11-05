package com.sso.ssologin.controller;

import com.sso.ssologin.pojo.User;
import com.sso.ssologin.utils.LoginCacheUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@RequestMapping("/login")
@Controller
public class LoginController {

    private static Set<User> dbUsers;
    static {
        dbUsers=new HashSet<>();
        dbUsers.add(new User(0,"zhongli","0000"));
        dbUsers.add(new User(1,"dadaliya","1111"));
        dbUsers.add(new User(2,"xiao","2222"));
    }

    @PostMapping
    public String doLogin(User user, HttpSession session, HttpServletResponse response){
        String target= (String) session.getAttribute("target");
        //模拟从数据库中通过登录用户名和密码去查找用户
        Optional<User> first=dbUsers.stream().filter(dbUsers->dbUsers.getUsername().equals(user.getUsername())&& dbUsers.getPassword().equals(user.getPassword()))
                .findFirst();
        if(first.isPresent()){
            //保存用户登录信息
            String token = UUID.randomUUID().toString();
            Cookie cookie=new Cookie("TOKEN",token);
            cookie.setDomain("ssosys.com");
            response.addCookie(cookie);
            LoginCacheUtil.loginUser.put(token,first.get());

        }else{
            //登录失败
            session.setAttribute("msg","用户名或密码错误");
            return"login";
        }
        //重定向到target地址
        return "redirect:"+target;
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(String token){
        if(!StringUtils.isEmpty(token))  {
            User user = LoginCacheUtil.loginUser.get(token);
            return ResponseEntity.ok(user);
        }else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public String doLogout(@CookieValue(required = false,value = "TOKEN")Cookie cookie,
                           HttpServletResponse response,String target)
    {
        if(cookie!=null){
            String token=cookie.getValue();
            if(!StringUtils.isEmpty(token)){
                cookie.setMaxAge(0);
                LoginCacheUtil.loginUser.remove(cookie.getValue());
            }
        }
        return "redirect:"+target;
    }

}
