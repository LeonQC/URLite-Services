package com.tom.service.impl;

import com.tom.dao.UrlRepository;
import com.tom.pojo.Url;
import com.tom.pojo.UrlEvent;
import com.tom.pojo.UrlRequestBody;
import com.tom.service.UrlService;
import com.tom.service.redis.GlobalCounterService;
import com.tom.utils.AliasGenerator;
import com.tom.utils.Base62;
import com.tom.utils.WebTitleFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final KafkaTemplate<String, UrlEvent> kafkaTemplate;
    private final GlobalCounterService globalCounterService;
    private final Base62 base62;

    @Override
    public Url createUrl(UrlRequestBody urlRequestBody) {
        String requestedAlias = urlRequestBody.getAlias();

        // If an alias was supplied, ensure it doesn't already exist
        if (requestedAlias != null && !requestedAlias.isEmpty()) {
            if (urlRepository.existsByShortCode(requestedAlias)) {
                return null;
            }
        }

        Long id = globalCounterService.nextId("url:id");

        // Compute short code (alias takes precedence)
        String shortCode = (requestedAlias != null && !requestedAlias.isEmpty())
                ? requestedAlias
                : base62.encode(id);

        Url url = new Url();
        url.setId(id);
        url.setOriginalUrl(urlRequestBody.getTarget_url());
        url.setTitle(urlRequestBody.getTitle());
        url.setCreatedAt(LocalDateTime.now());
        url.setClicks(0);
        url.setShortCode(shortCode);
        url.setShortUrl(base62.BASE_URL + shortCode); // it doesn't exist in DB.

        try {
            return urlRepository.save(url);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // possible race on alias/shortCode uniqueness; surface null to indicate failure
            log.warn("Failed to save URL due to data integrity violation: {}", e.getMessage());
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
                url.setShortUrl(base62.BASE_URL + url.getShortCode())
        );
        return urls;
    }

    @Override
    public Url getUrlById(Long id) {
        return urlRepository.findById(id).orElse(null);
    }

    @Override
    public void process(MultipartFile file, String batchId) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                String line;
                int index = 1;

                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String longUrl = line.trim();
                    if (longUrl.isEmpty()) continue;

                    UrlEvent event = new UrlEvent(batchId, index++, longUrl);
                    kafkaTemplate.send("url-create", batchId, event);
                }

                // -1 represents done
                UrlEvent doneEvent = new UrlEvent(batchId, -1, null);
                kafkaTemplate.send("url-create", batchId, doneEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ).start();
    }
}
