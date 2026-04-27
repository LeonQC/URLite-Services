package com.tom.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "urls")
public class Url {
    @Id
    private Long id;
    @Column(name = "original_url")
    private String originalUrl;
    @Column(name = "short_code")
    private String shortCode;
    @Transient
    private String shortUrl;
    private String title;
    private Integer clicks;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
