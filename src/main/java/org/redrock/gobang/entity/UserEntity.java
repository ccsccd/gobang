package org.redrock.gobang.entity;

public class UserEntity {
    private int id;
    private String username;
    private String eMail;
    private String password;
    private String createTime;
    private String updateTime;

    public UserEntity() {
    }

    public UserEntity(String eMail, String password) {
        this.eMail = eMail;
        this.password = password;
    }

    public UserEntity(String username, String eMail, String password, String createTime) {
        this.username = username;
        this.eMail = eMail;
        this.password = password;
        this.createTime = createTime;
    }

    public UserEntity(int id, String username, String eMail, String createTime) {
        this.id = id;
        this.username = username;
        this.eMail = eMail;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
