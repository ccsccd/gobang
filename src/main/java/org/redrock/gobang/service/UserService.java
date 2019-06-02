package org.redrock.gobang.service;

import org.redrock.gobang.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * 插入用户
     *
     * @param user 用户
     * @return 成功与否
     */
    boolean insertUser(UserEntity user);

    /**
     * 登录
     *
     * @param eMail,password e-mail,密码
     * @return 成功返回User类
     */
    UserEntity checkLogin(String eMail, String password);

    /**
     * 发验证码邮件
     *
     * @param eMail e-mail
     * @return 验证码
     */
    String sendEmail(String eMail);
}
