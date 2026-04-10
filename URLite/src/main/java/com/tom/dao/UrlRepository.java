package com.tom.dao;

import com.tom.pojo.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    boolean existsByAlias(String alias);

    Optional<Url> findByShortCode(String shortCode);
}
