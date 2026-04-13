package com.tom.dao;

import com.tom.pojo.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    boolean existsByShortCode(String shortCode);

    Optional<Url> findByShortCode(String shortCode);
}
