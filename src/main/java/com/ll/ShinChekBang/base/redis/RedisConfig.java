package com.ll.ShinChekBang.base.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.url}")
    private String url;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() throws URISyntaxException {
        URI redisUri = new URI(url);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisUri.getHost(), redisUri.getPort());
        String userInfo = redisUri.getUserInfo();
        if (userInfo != null) {
            String[] userPass = userInfo.split(":", 2);
            if (userPass.length == 2) {
                configuration.setPassword(RedisPassword.of(userPass[1]));
            }
        }

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws URISyntaxException {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() throws URISyntaxException {
        final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }
}
