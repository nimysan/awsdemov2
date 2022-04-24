package top.cuteworld.awsdemo.aws;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.utils.Md5Utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class SSE_C_KEY {

    public KeyGenerator KEY_GENERATOR;

    private SecretKey secretKey;

    private String base64EncodedString;

    private String base64DecodedStringMD5;

    public SSE_C_KEY() {
        try {
            KEY_GENERATOR = KeyGenerator.getInstance("AES");
            KEY_GENERATOR.init(256, new SecureRandom());
            secretKey = KEY_GENERATOR.generateKey();

            this.base64EncodedString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            this.base64DecodedStringMD5 = Md5Utils.md5AsBase64(secretKey.getEncoded());

            log.info(secretKey.getFormat() + " -- " + secretKey.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public String getSSECustomKey() {
        return this.base64EncodedString;
    }


    public String getSSECustomKeyMD5() {
        return this.base64DecodedStringMD5;
    }


    private String utf8String(byte[] decode) {
        return new String(decode, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SSE_C_KEY sseCustomerKey = new SSE_C_KEY();
        log.info(sseCustomerKey.getSSECustomKey());
    }
}
