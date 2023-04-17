package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class  TokenUtil {

    static final String ISSUER = "Jongee";
    static final String USER_ID = "UserID";
    static final long MILLI_SECONDS_IN_HOUR = 1*60*60*1000;
    static Algorithm algorithm = Algorithm.HMAC256("mysecretkey");
    //别人是无法轻易看到这个算法的

    //step1是生成某个token的值，利用一个算法形成一个秘钥
    //withissuer(就是看谁颁发的)
     public static String signToken(Integer userId,int expirationInHour){
         String token = JWT.create().withIssuer(ISSUER).withClaim(USER_ID,userId).
                 withExpiresAt(new Date(System.currentTimeMillis() + expirationInHour*MILLI_SECONDS_IN_HOUR))
                 .sign(algorithm);
         return token;
     }

     //step2：verify token
    public static Integer verifyToken(String token){
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        //先，生成好一个验证器，然后开始验证token
        DecodedJWT jwt = verifier.verify(token);
        Integer userId = jwt.getClaim(USER_ID).asInt();
        return userId;
    }

}
