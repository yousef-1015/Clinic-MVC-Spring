package com.example.clinicmvcspring.config;

import java.time.Duration;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.example.clinicmvcspring.exceptions.CustomCacheErrorHandler;

@Configuration
public class RedisConfig implements CachingConfigurer {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration patientConfig;
        RedisCacheConfiguration appointmentConfig;

        // the rules of the redid cache
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // TTL IS 5 MINUTES
                .disableCachingNullValues() // SO CACHE DOESN'T FILL UPP WITH NULL VALUES FETCHED FROM THE DATABASE
                .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json())); // to turn objects into
                                                                                                // json values in redis
                                                                                                // cache
        patientConfig = config.entryTtl(Duration.ofMinutes(15));
        appointmentConfig = config.entryTtl(Duration.ofMinutes(0));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("Patients", patientConfig)
                .withCacheConfiguration("Appointments", appointmentConfig)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

}
