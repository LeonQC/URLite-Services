package com.tom.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlEvent {
    private String batchId;
    private int index;
    private String longUrl;
}
