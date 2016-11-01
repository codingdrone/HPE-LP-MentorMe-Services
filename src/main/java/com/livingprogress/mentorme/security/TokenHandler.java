package com.livingprogress.mentorme.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.livingprogress.mentorme.aop.LogAspect;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.utils.Helper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * The JWT token handler.
 */
public class TokenHandler {

    /**
     * The hmac algorithm name.
     */
    private static final String HMAC_ALGO = "HmacSHA256";

    /**
     * The separator.
     */
    private static final String SEPARATOR = ".";

    /**
     * The separator splitter.
     */
    private static final String SEPARATOR_SPLITTER = "\\.";

    /**
     * The mac.
     */
    private Mac hmac;

    /**
     * Token handler constructor.
     *
     * @param secretKey the secret key.
     * @throws IllegalStateException throws if error to create mac instance.
     */
    public TokenHandler(byte[] secretKey) {
        try {
            hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        }
    }

    /**
     * Parse user from token.
     *
     * @param token the token.
     * @return parsed user from token.
     */
    public User parseUserFromToken(String token) {
        final String[] parts = token.split(SEPARATOR_SPLITTER);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            try {
                final byte[] userBytes = fromBase64(parts[0]);
                final byte[] hash = fromBase64(parts[1]);

                boolean validHash = Arrays.equals(createHmac(userBytes), hash);
                if (validHash) {
                    final User user = fromJSON(userBytes);
                    if (new Date().getTime() < user.getExpires()) {
                        return user;
                    }
                }
            } catch (IllegalArgumentException e) {
                Helper.logException(LogAspect.LOGGER, "com.livingprogress.mentorme.security" +
                        ".TokenHandler#parseUserFromToken", e);
            }
        }
        return null;

    }

    /**
     * Create token from user.
     *
     * @param user the user.
     * @return the generated token from user.
     */
    public String createTokenForUser(User user) {
        byte[] userBytes = toJSON(user);
        byte[] hash = createHmac(userBytes);
        final StringBuilder sb = new StringBuilder(170);
        sb.append(toBase64(userBytes));
        sb.append(SEPARATOR);
        sb.append(toBase64(hash));
        return sb.toString();
    }

    /**
     * Parse user from bytes.
     *
     * @param userBytes the user bytes.
     * @return user parsed from bytes.
     * @throws IllegalStateException throws if error to
     */
    private User fromJSON(final byte[] userBytes) {
        try {
            return Helper.MAPPER.readValue(new ByteArrayInputStream(userBytes), User.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Generate json fro user.
     *
     * @param user the user.
     * @return user json.
     * @throws IllegalStateException throws if error to
     */
    private byte[] toJSON(User user) {
        try {
            return Helper.MAPPER.writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Generate base64 string.
     *
     * @param content the bytes content.
     * @return base64 string.
     */
    private String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content)
                                .replace('+', '-')
                                .replace('/', '_')
                                .replaceAll("=", "");
    }

    /**
     * Get bytes from base64 string
     *
     * @param val the base64 string value.
     * @return bytes from base64 string
     */
    private byte[] fromBase64(String val) {
        val = val.replace('-', '+')
                 .replace('_', '/');
        final int rest = val.length() % 4;
        if (rest != 0) {
            val += rest == 3 ? "=" : "==";
        }
        return DatatypeConverter.parseBase64Binary(val);
    }

    /**
     * Synchronized to guard internal hmac object.
     *
     * @param content the bytes
     * @return the bytes after synchronized to guard internal hmac object
     */
    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }
}
