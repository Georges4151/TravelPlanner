package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private Integer memberLevel;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
