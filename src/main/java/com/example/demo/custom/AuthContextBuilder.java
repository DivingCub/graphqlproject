//package com.example.demo.custom;
//
//import com.example.demo.entity.UserEntity;
//import com.example.demo.mapper.UserEntityMapper;
//import com.example.demo.util.TokenUtil;
//import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.Nullable;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Map;
//
//@Component
//@Slf4j
//public class AuthContextBuilder implements DgsCustomContextBuilderWithRequest {
//
//    private  final UserEntityMapper userEntityMapper;
//
//    static final String AUTHORIZATION_HEADER = "Authorization";
//
//    public AuthContextBuilder(UserEntityMapper userEntityMapper) {
//        this.userEntityMapper = userEntityMapper;
//    }
//
//    //接下来就要去抓取Header文件了
//    @Override
//    public Object build(@Nullable Map map, @Nullable HttpHeaders httpHeaders, @Nullable WebRequest webRequest) {
//        log.info("Building all context");
//        AuthContext  authContext = new AuthContext();
//        if(!httpHeaders.containsKey(AUTHORIZATION_HEADER)){
//            log.info("用户没认证");
//            return  authContext;
//        }
//
//        String authorization = httpHeaders.getFirst(AUTHORIZATION_HEADER);
//        String token = authorization.replace("Bearer","");
//        Integer userId ;
//
//        try{
//            userId = TokenUtil.verifyToken(token);
//        }catch (Exception ex){
//            authContext.setTokenInvalid(true);
//            return authContext;
//        }
//        UserEntity userEntity = userEntityMapper.selectById(userId);
//        //面对可能存在的拿到userEntity的null的情况
//        if(userEntity == null){
//            authContext.setTokenInvalid(true);
//            return authContext;
//        }
//
//        authContext.setUserEntity(userEntity);
//        log.info("状态设置成功，userId={}",userId);
//
//        return authContext;
//    }
//}
