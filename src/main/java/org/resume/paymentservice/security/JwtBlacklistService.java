package org.resume.paymentservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static org.resume.paymentservice.contants.SecurityConstants.BLACKLIST_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final RedissonClient redissonClient;

    public void addToBlacklist(String tokenId, long ttlSeconds) {
        RBucket<String> bucket = redissonClient.getBucket(BLACKLIST_PREFIX + tokenId);
        bucket.set("revoked", Duration.ofSeconds(ttlSeconds));
        log.info("Token added to blacklist: tokenId={}, ttl={}s", tokenId, ttlSeconds);
    }

    public boolean isBlacklisted(String tokenId) {
        RBucket<String> bucket = redissonClient.getBucket(BLACKLIST_PREFIX + tokenId);
        return bucket.isExists();
    }

}
