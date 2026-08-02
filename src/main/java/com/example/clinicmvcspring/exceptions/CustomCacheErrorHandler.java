package com.example.clinicmvcspring.exceptions;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(
            RuntimeException e,
            Cache cache,
            Object key) {

        log.warn("Redis is down! Falling back to MySQL for GET. Reason: " + e.getMessage());

    }

    @Override
    public void handleCachePutError(
            RuntimeException e,
            Cache cache,
            Object key,
            Object value) {
        log.warn("Redis is down! Couldn't PUT into cache. Reason: " + e.getMessage());

    }

    @Override
    public void handleCacheEvictError(
            RuntimeException e,
            Cache cache,
            Object key) {
        log.warn("Redis is down! Couldn't EVICT from cache. Reason: " + e.getMessage());

    }

    @Override
    public void handleCacheClearError(
            RuntimeException e,
            Cache cache) {
        log.warn("Redis is down! Couldn't Clear cache. Reason: " + e.getMessage());

    }
}