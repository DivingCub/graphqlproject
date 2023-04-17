package com.example.demo.type;


import lombok.Data;

@Data
public class EventInput {
    private String title;
    private String description;
    private Double price;
    private String date;
    private Integer creatorId;
}
