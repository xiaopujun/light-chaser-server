package com.ctbu.lc.entity;

import lombok.Data;

@Data
public class TestEntity {
    private Long id;
    private String name;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
    }
}
