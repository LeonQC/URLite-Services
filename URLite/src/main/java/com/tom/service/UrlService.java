package com.tom.service;

import com.tom.pojo.Url;
import com.tom.pojo.UrlRequestBody;

public interface UrlService {
    Url createUrl(UrlRequestBody urlRequestBody);

    String getTitle(String url);

    String getAliases(String url);
}
