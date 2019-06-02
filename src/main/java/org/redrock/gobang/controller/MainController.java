package org.redrock.gobang.controller;

import org.redrock.gobang.entity.UserEntity;
import org.redrock.gobang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("user")
public class MainController {
    @Autowired
    private UserService userService;

    @GetMapping("/view/login")
    public String view() {
        return "login";
    }

    @GetMapping("/view/register")
    public String view2() {
        return "register";
    }

    @GetMapping("/go")
    public String view3() {
        return "gobang";
    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestParam("username") final String username,
                                        @RequestParam("eMail") final String eMail,
                                        @RequestParam("password") final String password,
                                        @RequestParam("code") final String code,
                                        HttpSession httpSession) {
        Map<String, Object> res = new HashMap<>();
        //无特殊符号,1-35位数字字母汉字均可
        String regex1 = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,35}$";
        //邮箱格式xxxxx@xxxxx.xxx
        String regex2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        //6-35位数字混字母
        String regex3 = "^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{6,35}$";
        if (username.matches(regex1) && eMail.matches(regex2) && password.matches(regex3)) {
            if (httpSession.getAttribute("mCode") != null) {
                String mCode = httpSession.getAttribute("mCode").toString();
                if (mCode.equals(code)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    UserEntity user = new UserEntity(username, eMail, password, sdf.format(new Date()));
                    if (userService.insertUser(user)) {
                        res.put("status", 200);
                        res.put("data", "注册成功");
                    } else {
                        //邮箱查重其实应单独做一个接口
                        res.put("status", 400);
                        res.put("data", "注册失败，邮箱已被注册");
                    }
                } else {
                    res.put("status", 800);
                    res.put("data", "注册失败，验证码有误");
                }
            } else {
                res.put("status", 1000);
                res.put("data", "注册失败，未获取验证码");
            }
        } else {
            res.put("status", 600);
            res.put("data", "注册失败，注册信息格式有误");
        }
        return res;
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam("eMail") final String eMail,
                                     @RequestParam("password") final String password,
                                     Model model,
                                     HttpServletRequest httpServletRequest) {
        Map<String, Object> res = new HashMap<>();
        //邮箱格式xxxxx@xxxxx.xxx
        String regex2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        //6-35位数字混字母
        String regex3 = "^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{6,35}$";
        if (eMail.matches(regex2) && password.matches(regex3)) {
            UserEntity user = null;
            user = userService.checkLogin(eMail, password);
            if (user != null) {
                res.put("status", 200);
                res.put("data", "登录成功");
                model.addAttribute("user", user);
                httpServletRequest.getSession();
            } else {
                res.put("status", 400);
                res.put("data", "登录失败，邮箱或密码错误");
            }
        } else {
            res.put("status", 600);
            res.put("data", "登录失败，邮箱或密码格式有误");
        }
        return res;
    }

    @PostMapping("/email")
    @ResponseBody
    public Map<String, Object> email(@RequestParam("eMail") final String eMail,
                                     Model model,
                                     HttpServletRequest httpServletRequest) {
        //邮箱格式xxxxx@xxxxx.xxx
        String regex2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Map<String, Object> res = new HashMap<>();
        if (eMail.matches(regex2)) {
            try {
                HttpSession httpSession = httpServletRequest.getSession();
                httpSession.setAttribute("mCode", userService.sendEmail(eMail));
                httpSession.setMaxInactiveInterval(60);
                res.put("status", 200);
                res.put("data", "验证码发送成功，请注意查看邮箱，有效期一分钟");
                return res;
            } catch (Exception e) {
                res.put("status", 400);
                res.put("data", "验证码发送失败");
                return res;
            }
        } else {
            res.put("status", 600);
            res.put("data", "验证码发送失败，邮箱格式有误");
            return res;
        }
    }
}
