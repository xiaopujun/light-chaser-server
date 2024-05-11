package com.dagu.lightchaser.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DbExecutorEntity {
    private Long id;
    private String sql;
}
