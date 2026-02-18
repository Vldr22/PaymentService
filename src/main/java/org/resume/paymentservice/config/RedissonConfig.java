package org.resume.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.resume.paymentservice.properties.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        String address = String.format(
                "redis://%s:%d",
                redisProperties.getHost(),
                redisProperties.getPort()
        );

        config.useSingleServer()
                .setAddress(address);

        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }
}
