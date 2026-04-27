package com.tom.service;

import com.tom.pojo.Url;
import com.tom.pojo.UrlRequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UrlService {
    Url createUrl(UrlRequestBody urlRequestBody);

    String getTitle(String url);

    String getAliases(String url);

    String getOriginalUrl(String shortCode);

    List<Url> findAll();

    Url getUrlById(Long id);

    void process(MultipartFile file, String batchId);
}
