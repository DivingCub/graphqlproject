package com.example.demo.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.example.demo.custom.AuthContext;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.example.demo.entity.EventEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.EventEntityMapper;
import com.example.demo.mapper.UserEntityMapper;
import com.example.demo.type.Event;
//import com.spring2go.easyevent.type;

import com.example.demo.type.EventInput;
import com.example.demo.type.User;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.autoconfig.DgsConfigurationProperties;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

//相当于是告知下面是一个controller，controller里面要实现所有的参数保存，就是events这个东西
@DgsComponent
public class EventDataFetcher {

        //模拟保存了全部的参数数据
//        private List<Event> events= new ArrayList<>();


    //用实际的数据访问层（就是数据库里面的东西）
    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;

    //获取所有这个端口的一些数据和操作类别
    public EventDataFetcher(EventEntityMapper eventEntityMapper, UserEntityMapper userEntityMapper) {
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }


    //查询逻辑的编辑
        @DgsQuery
        public List<Event> events (){
//            return Array.asList("Reading book","Watching movie","Cooking");
//            return events;
            //查询所有的写法
            List<EventEntity> eventEntityList = eventEntityMapper.selectList(new QueryWrapper<>());
            //上面的数据，要传输出去，还要进行一层转化
            List<Event> eventList = eventEntityList.stream().map(Event::fromEntity).collect(Collectors.toList());
            //这一步骤，将还没有添加Creator的数据给拿出来，将User给添加上去处理
            return eventList;
        }


        //改东西时候的逻辑操作
        //逻辑，只有认证过的，才能创建好的event
        @DgsMutation
        public Event createEvent(@InputArgument(name = "eventInput") EventInput input,DataFetchingEnvironment dfe){
            //上面标书 name= xxx，是因为输入的input 和 在schema中的文件名字是不一样的，所以要标注出来

//            Event newEvent = new Event();

//            newEvent.setId(UUID.randomUUID().toString());
//            newEvent.setTitle(input.getTitle());
//            newEvent.setDescription(input.getDescription());
//            newEvent.setPrice(input.getPrice());
//            newEvent.setDate(input.getDate());
//            events.add(newEvent);


            //先过来认证一下这个event,只有认证过没问题的，才能继续查询
//            AuthContext authContext = DgsContext.getCustomContext(dfe);
//            authContext.ensureAuthenticated();
//
//            //从而Id的来源，也不是开始的event，而是从EvenEntity里面进行获取,从authContext里面获取
//            EventEntity newEvenEntity = EventEntity.fromEventInput(input);
//            newEvenEntity.setCreatorId(authContext.getUserEntity().getId());

            EventEntity newEventEntity = EventEntity.fromEventInput(input);
            eventEntityMapper.insert(newEventEntity);
            Event newEvent =  Event.fromEntity(newEventEntity);

            return newEvent;

        }

//
//        private void populateEventWithUser(Event event,Integer userId){
//            UserEntity userEntity = userEntityMapper.selectById(userId);
//            User user = User.fromEntity(userEntity);
//            event.setCreator(user);
//        }
//        这个也是一种很基本的填充方式，后面不会采用这种来进行

        @DgsData(parentType = "Event", field = "creator")
        public User creator(DgsDataFetchingEnvironment dfe){
            Event event = dfe.getSource();
            UserEntity userEntity = userEntityMapper.selectById(event.getCreatorId());
            User user = User.fromEntity(userEntity);
            return user;
        }

}
