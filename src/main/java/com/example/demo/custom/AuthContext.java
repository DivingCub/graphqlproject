//package com.example.demo.custom;
//
//import com.example.demo.entity.UserEntity;
//import lombok.Data;
//
//@Data
//public class AuthContext {
//
//    private UserEntity userEntity;
//    private boolean tokenInvalid;
//
//    public void ensureAuthenticated(){
//        if(tokenInvalid) throw  new RuntimeException("token无效!");
//        if(userEntity ==null) throw new RuntimeException("未登录，请先登录");
//
//    }
//
//
//}
