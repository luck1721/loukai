package com.example.demo.bll.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lk
 * @date 2020/5/12
 */
@Entity
@Table(name = "article")
public class Article {
    @Id
    private int id;

    @Column(name = "num") //给此文章点赞的数量
    private int greatNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGreatNum() {
        return greatNum;
    }

    public void setGreatNum(int greatNum) {
        this.greatNum = greatNum;
    }

}
