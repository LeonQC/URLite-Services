package com.tom.service.impl;

import com.tom.dao.UrlRepository;
import com.tom.pojo.Url;
import com.tom.pojo.UrlRequestBody;
import com.tom.service.UrlService;
import com.tom.utils.AliasGenerator;
import com.tom.utils.Base62;
import com.tom.utils.WebTitleFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    public Url createUrl(UrlRequestBody urlRequestBody) {
        boolean aliasExists = urlRepository.existsByAlias(urlRequestBody.getAlias());
        if (!aliasExists) {
            Url url = new Url();
            url.setOriginalUrl(urlRequestBody.getTarget_url());
            url.setTitle(urlRequestBody.getTitle());
            url.setShortCode("");//避免数据库not null报错
            url.setCreatedAt(LocalDateTime.now());//避免数据库not null报错
            url.setClicks(0);
            url.setAlias("");//避免出现null，考虑到没有alias的request body.
            urlRepository.save(url);

            // 先存储得到id，再如下返回id。
            String shortCode = Base62.encode(url.getId());
            url.setShortCode(shortCode);
            // 避免数据库short code unique报错
            urlRepository.save(url);

            if (urlRequestBody.getAlias() != null && !urlRequestBody.getAlias().isEmpty()) {
                url.setAlias(urlRequestBody.getAlias());
                url.setShortUrl(Base62.BASE_URL + urlRequestBody.getAlias());
            } else {
                url.setShortUrl(Base62.BASE_URL + shortCode);
            }

            urlRepository.save(url);
            return url;
        } else {
            return null;
        }
    }

    @Override
    public String getTitle(String url) {
        return WebTitleFetcher.getTitle(url);
    }

    @Override
    public String getAliases(String url) {
        return AliasGenerator.generate(url);
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getOriginalUrl)
                .orElse(null);
    }

    @Override
    public List<Url> findAll() {
        List<Url> urls = urlRepository.findAll();
        urls.forEach(url ->
                url.setShortUrl(Base62.BASE_URL + url.getShortCode())
        );
        return urls;
    }
}
