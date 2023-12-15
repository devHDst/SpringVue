package com.restapi.vueconnect.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Tasks")
@Data
public class Task {
    //migration兼モデルファイル
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "startDate", nullable = true)
    private Date startDate;


    @Column(name = "deadLine", nullable = true)
    private Date deadLine;

    //コンストラクタ(空値)
    public Task(){

    }
    //コンストラクタ(引数全部)
    public Task(long id, String title, String content, Date startDate, Date deadLine) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.deadLine = deadLine;
    }

}
