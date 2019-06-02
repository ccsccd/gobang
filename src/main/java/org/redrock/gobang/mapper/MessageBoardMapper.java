package org.redrock.gobang.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.redrock.gobang.entity.UserEntity;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "messageBoardMapper")
public interface MessageBoardMapper {

    @Select("SELECT count(id) FROM message_board WHERE id = #{id} AND user_id = #{userId}")
    int checkId(@Param("id") int id, @Param("userId") int userId);

    @Insert("INSERT INTO user(username,e_mail,password,create_time) VALUE(#{username},#{eMail},#{password},#{createTime})")
    void insertUser(UserEntity user);

    @Select("SELECT count(id) FROM `user` WHERE e_mail = #{eMail}")
    int checkUser(@Param("eMail") String eMail);

    @Select("SELECT * FROM `user` WHERE e_mail = #{eMail} and password = #{password}")
    UserEntity checkLogin(@Param("eMail") String eMail, @Param("password") String password);
}
