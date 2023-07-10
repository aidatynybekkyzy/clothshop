package com.aidatynybekkyzy.clothshop;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class TestCacheConfig extends CachingConfigurerSupport {

    @Override
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}

