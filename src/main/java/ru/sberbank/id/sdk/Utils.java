package ru.sberbank.id.sdk;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author shago.v.s
 */
public class Utils {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String JWT_PATTERN = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_=]*$";

    private Utils() {
    }

    public static String randomStringUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String getPayloadFromJwt(String jwt) {
        String payload = jwt.split("\\.")[1];
        return StringUtils.newStringUtf8(Base64.decodeBase64(payload));
    }

    /**
     * @return случайная строка длиной 32-байта
     */
    public static String randomString() {
        byte[] n = new byte[32];
        secureRandom.nextBytes(n);
        return Base64.encodeBase64URLSafeString(n);
    }

    public static boolean isJWTString(String content) {
        return content.matches(JWT_PATTERN);
    }
}
