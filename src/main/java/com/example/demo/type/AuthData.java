package com.example.demo.type;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthData {

    private Integer userId;
    private String  token;
    private Integer tokenExpiration;
    //要一一对应




}
