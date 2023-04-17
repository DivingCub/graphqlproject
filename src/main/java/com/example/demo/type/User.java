package com.example.demo.type;


import com.example.demo.entity.UserEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    private Integer id;
    private String email;
    private String password;
    private List<Event> createdEvents = new ArrayList<>();

    //从实例成层面过度到传输层面，需要转换上做的方法
    public static User fromEntity(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        return  user;
        //为什么没有password，是因为传输的时候密码并不是必须要做的事情，因为要保密，不能全部传输，所以传输层的密码这块是不一定需要的
    }

}
