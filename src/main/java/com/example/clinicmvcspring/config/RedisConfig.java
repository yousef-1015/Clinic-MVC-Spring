package com.example.clinicmvcspring.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // the rules of the redid cache
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // TTL IS 5 MINUTES
                .disableCachingNullValues() // SO CACHE DOESN'T FILL UPP WITH NULL VALUES FETCHED FROM THE DATABASE
                .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json())); // to turn objects into
                                                                                                // json values in redis
                                                                                                // cache
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

}
