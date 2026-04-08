package com.tom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebTitleFetcher {
    public static void main(String[] args) {
        String url = "https://www.bbc.com/sport/football/articles/c2ev313pld3o";

        try {
            // 连接网页并获取 Document
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // 模拟浏览器
                    .timeout(5000)
                    .get();

            // 获取 title
            String title = doc.title();

            System.out.println("网页标题: " + title);

        } catch (Exception e) {
            System.out.println("获取失败: " + e.getMessage());
        }
    }
}
