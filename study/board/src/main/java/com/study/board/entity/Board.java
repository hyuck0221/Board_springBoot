package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity  //table을 의미
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //IDENTITY는 mysql에서 사용
    private Integer id;  //Mysql에서 형식에 맞게 순서대로 만든 것
    private String title;
    private String content;
}
