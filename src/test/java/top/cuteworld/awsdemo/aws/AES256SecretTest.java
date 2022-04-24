package top.cuteworld.awsdemo.aws;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AES256SecretTest {

    @Test
    public void testAES256SSECKey() {
        AES256Secret aes256Secret = AES256Secret.SAMPLE_AES256_SECRET;
        assertEquals("TVV3Zy91dkcxa0lJdUJ5SlVJU0huQW04VEs0c0ErUFI=", aes256Secret.getSSECustomerKey());
    }

    @Test
    public void testAES256SSECMD5() {
        AES256Secret aes256Secret = AES256Secret.SAMPLE_AES256_SECRET;
        assertEquals("nnd7u25CkQx4oUdgF1strw==", aes256Secret.getSSECustomKeyMD5());
    }

    @Test
    public void testGeneratedKey() {
        try {
            AES256Secret aes256Secret = new AES256Secret();
            log.info(aes256Secret.getSSECustomerKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}