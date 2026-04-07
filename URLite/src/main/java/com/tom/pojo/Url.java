package com.tom.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "original_url")
    private String originalUrl;
    @Column(name = "short_code")
    private String shortCode;
    private String alias;
    @Transient
    private String shortUrl;
    private String title;
    private Integer clicks;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
