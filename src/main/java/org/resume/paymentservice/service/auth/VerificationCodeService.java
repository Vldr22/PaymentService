package org.resume.paymentservice.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.resume.paymentservice.exception.VerificationException;
import org.resume.paymentservice.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static org.resume.paymentservice.contants.VerificationConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final RedissonClient redissonClient;

    public void sendCode(String recipient) {
        String code = CodeGenerator.generateVerificationCode();

        RBucket<String> bucket = getBucket(recipient);
        bucket.set(code, Duration.ofSeconds(CODE_TTL_SECONDS));

        log.info("Verification code sent to {}: {}", recipient, code);
    }

    public void verifyCode(String recipient, String code) {
        RBucket<String> bucket = getBucket(recipient);
        String storedCode = bucket.get();

        if (storedCode == null) {
            throw VerificationException.smsCodeExpired(recipient);
        }

        if (!storedCode.equals(code)) {
            throw VerificationException.smsCodeInvalid(recipient);
        }

        bucket.delete();
    }

    private RBucket<String> getBucket(String recipient) {
        return redissonClient.getBucket(String.format("%s%s", CODE_PREFIX, recipient));
    }

}
