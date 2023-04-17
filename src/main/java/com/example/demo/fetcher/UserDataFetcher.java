package com.example.demo.fetcher;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.example.demo.custom.AuthContext;
import com.example.demo.entity.EventEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.EventEntityMapper;
import com.example.demo.mapper.UserEntityMapper;
import com.example.demo.type.*;
import com.example.demo.util.TokenUtil;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
public class UserDataFetcher {

    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventEntityMapper eventEntityMapper;
    //config下面文件，都需要通过书写，来往这边的fetcher文件里面注入


    public UserDataFetcher(UserEntityMapper userEntityMapper, PasswordEncoder passwordEncoder, EventEntityMapper eventEntityMapper) {
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventEntityMapper = eventEntityMapper;
    }


    @DgsQuery
    public List<User> users(DataFetchingEnvironment dfe){
        //只有认证过的人，才能查询系统数据
//        AuthContext authContext = DgsContext.getCustomContext(dfe);
//        authContext.ensureAuthenticated();

        List<UserEntity> userEntityList = userEntityMapper.selectList(null);
        List<User> userList = userEntityList.stream().map(User::fromEntity).collect(Collectors.toList());
        return userList;
    }

    @DgsQuery
    public AuthData login(@InputArgument LoginInput loginInput){
        UserEntity userEntity = this.findUserByEmail(loginInput.getEmail());
        if(userEntity == null){
            throw new RuntimeException("使用该Email地址的用户不存在");
        }
        boolean match = passwordEncoder.matches(loginInput.getPassword(),userEntity.getPassword());
        if(!match){
            throw  new RuntimeException("密码wrong");
        }

        String token = TokenUtil.signToken(userEntity.getId(),1);
        AuthData authData = new AuthData().setUserId(userEntity.getId())
                .setToken(token)
                .setTokenExpiration(1);

        return  authData;

    }

    @DgsData(parentType = "User", field = "createdEvents")
    public List<Event> createdEvents(DgsDataFetchingEnvironment dfe){
        User user = dfe.getSource();
        QueryWrapper<EventEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EventEntity::getCreatorId,user.getId());
        //简单的看两个查询对象是否相同的视线方法
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(queryWrapper);
        List<Event> eventList = eventEntityList.stream().map(Event::fromEntity).collect(Collectors.toList());
        return  eventList;
    }

    @DgsMutation
    public User createUser(@InputArgument UserInput userInput){
        //首先要确定用户是不存在的，才能完成后续的操作
        ensurUserNotExists(userInput);
        //如果到这一步能执行下来，说明是没啥问题的
        UserEntity newUserEntity = new UserEntity();
        //userEntity相当于一个车，将创建好的对象放到entity上面，并且方便最终的放入
        //passcode 应为要传输，所以密码是要通过hash进行一个编码上的转变，完成加密的一个小小的操作
        newUserEntity.setEmail(userInput.getEmail());
        newUserEntity.setPassword(passwordEncoder.encode(userInput.getPassword()));

        userEntityMapper.insert(newUserEntity);

        User newUser = User.fromEntity(newUserEntity);
        newUser.setPassword(null);
        //确保password设置成null，不会泄露密码信息
        return newUser;

    }

    private void ensurUserNotExists(UserInput userInput){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail,userInput.getEmail());
        if(userEntityMapper.selectCount(queryWrapper)>=1){
            throw  new RuntimeException("该Email地址已经被使用");
            //如果出问题，那么这个异常直接被throw出去，那么执行应该会中断了
        }
    }

    private UserEntity findUserByEmail(String email){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail,email);
        //相当于是输入一种条件，让后面的内容进行查询出来
        return userEntityMapper.selectOne(queryWrapper);

    }

}
