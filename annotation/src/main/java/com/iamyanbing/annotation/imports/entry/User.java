package com.iamyanbing.annotation.imports.entry;

public class User {
    private String name = "yanbing";
    private Integer age;

    public User() {
        System.out.println("User被创建了");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
