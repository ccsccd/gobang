package org.redrock.gobang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.redrock.gobang.entity.EmailUtilEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "emailUtilMapper")
public interface EmailUtilMapper {
    @Select("SELECT * FROM email_util")
    List<EmailUtilEntity> findUtileMail();
}
