package com.tom.controller;

import com.tom.pojo.Url;
import com.tom.pojo.UrlRequestBody;
import com.tom.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequiredArgsConstructor
@Slf4j
@RestController
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<?> createUrl(@Valid @RequestBody UrlRequestBody urlRequestBody) {
        log.info("create a short url: {}", urlRequestBody);
        Url url = urlService.createUrl(urlRequestBody);
        if (url != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(url);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("alias is already taken!");
        }
    }

    @GetMapping("/title")
    public ResponseEntity<?> getTitle(@RequestParam String url) {
        log.info("get url for title: {}", url);
        String title = urlService.getTitle(url);
        return ResponseEntity.status(HttpStatus.OK).body(title);
    }

    @GetMapping("/aliases")
    public ResponseEntity<?> getAliases(@RequestParam String url) {
        log.info("get url for aliases: {}", url);
        String aliases = urlService.getAliases(url);
        return ResponseEntity.status(HttpStatus.OK).body(aliases);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        log.info("get url for shortCode: {}", shortCode);
        String originalUrl = urlService.getOriginalUrl(shortCode);
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }
}