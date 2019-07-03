package com.iamyanbing.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @Auther: yanbing
 * @Date: 2019/2/23 14:03
 */
@Data
@ToString
public class User {
    private Long id;
    private String name;
    private Integer age;
}
