package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.type.Event;
import com.example.demo.type.EventInput;
import com.example.demo.util.DateUtil;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@TableName(value = "tb_event")
public class EventEntity {

    @TableId(type= IdType.AUTO)
    private Integer id;
    //EventInput 里面，本身是没有id的，完全是entity里面自己往里面创建出来
    //告诉数据库这是一个自增的字段，并且数据库会auto generate 进行生成，并且这个东西是原先表传输里面没有的内容
    private String title;
    private String description;
    private Double price;
    private Date date;
    private Integer creatorId;


    //传输层和实体层会进行一些转换，存入的时候，传输类型会被转化为实体类型，取出来的时候实体类型会被转化成为传输类型。
    //下面的操作，是留下传入信息的这个个体讯息
    public static  EventEntity fromEventInput(EventInput input){

        EventEntity eventEntity  = new EventEntity();
        eventEntity.setTitle(input.getTitle());
        eventEntity.setDescription(input.getDescription());
        eventEntity.setPrice(input.getPrice());
        eventEntity.setDate(DateUtil.convertISOStringToDate(input.getDate()));
        eventEntity.setCreatorId(input.getCreatorId());
        return eventEntity;



    }

}
