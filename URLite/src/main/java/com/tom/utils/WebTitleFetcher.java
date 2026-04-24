package com.tom.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebTitleFetcher {

    public static String getTitle(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // 模拟浏览器
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc.title();
    }
}
