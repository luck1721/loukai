package com.example.demo.bll.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author loukai
 * @since 2021-06-22
 */
public class ThirdPartUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ThirdPartUser{" +
            "id=" + id +
            ", name=" + name +
        "}";
    }
}
