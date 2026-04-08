package com.tom.dao;

import com.tom.pojo.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    boolean existsByAlias(String alias);
}
