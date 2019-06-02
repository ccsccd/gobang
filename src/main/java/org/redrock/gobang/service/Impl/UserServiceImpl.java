package org.redrock.gobang.service.Impl;

import org.redrock.gobang.entity.EmailUtilEntity;
import org.redrock.gobang.entity.UserEntity;
import org.redrock.gobang.mapper.EmailUtilMapper;
import org.redrock.gobang.mapper.MessageBoardMapper;
import org.redrock.gobang.service.UserService;
import org.redrock.gobang.util.EmailUtil;
import org.redrock.gobang.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MessageBoardMapper messageBoardMapper;

    @Autowired
    private EmailUtilMapper emailUtilMapper;


    @Override
    public boolean insertUser(UserEntity user) {
        if (messageBoardMapper.checkUser(user.geteMail()) == 0) {
            String newPassword = Encrypt.md5Encode(user.getPassword());
            user.setPassword(newPassword);
            messageBoardMapper.insertUser(user);
            return true;
        }
        return false;
    }

    @Override
    public UserEntity checkLogin(String eMail, String password) {
        String newPassword = Encrypt.md5Encode(password);
        return messageBoardMapper.checkLogin(eMail, newPassword);
    }

    @Override
    public String sendEmail(String eMail) {
        String code = EmailUtil.achieveSimpleCode();
        List<EmailUtilEntity> list = emailUtilMapper.findUtileMail();
        EmailUtil.sendAuthCodeEmail(eMail, code, list.get(0).geteMail(), list.get(0).getPassword());
        return code;
    }
}
