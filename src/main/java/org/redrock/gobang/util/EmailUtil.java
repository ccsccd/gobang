package org.redrock.gobang.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {

    public static String achieveSimpleCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    public static void sendAuthCodeEmail(String email, String authCode,String UtilEmail,String UtilPassword) {
        //不要把邮箱及密码嵌在代码里还上传到github
        try {
            SimpleEmail mail = new SimpleEmail();
            //发送邮件的服务器
            mail.setHostName("smtp.qq.com");
            //登录邮箱的密码，是开启SMTP的密码
            mail.setAuthentication(UtilEmail, UtilPassword);
            //发送邮件的邮箱和发件人
            mail.setFrom(UtilEmail, "邮箱验证机器人");
            //使用安全链接
            mail.setSSLOnConnect(true);
            //utf-8编码
            mail.setCharset("UTF-8");
            //接收的邮箱
            mail.addTo(email);
            //设置邮件的主题
            mail.setSubject("注册验证码");
            //设置邮件的内容
            mail.setMsg("尊敬的用户：您好！您的注册验证码为：" + authCode + "，有效期为一分钟，请尽快验证。");
            //发送
            mail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}

