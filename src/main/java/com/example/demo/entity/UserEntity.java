package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.type.UserInput;
import lombok.Data;
import org.apache.catalina.User;

@Data
@TableName(value = "tb_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String email;
    private String password;

    //传输类型要转换成数据库实体类型
    public UserEntity fromUserInput(UserInput userInput){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userInput.getEmail());
        userEntity.setPassword(userInput.getPassword());
        return userEntity;

    }

}
