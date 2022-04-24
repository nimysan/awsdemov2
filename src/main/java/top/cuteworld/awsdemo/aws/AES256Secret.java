package top.cuteworld.awsdemo.aws;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.utils.Md5Utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 在与S3的服务器端加密一起使用的时候， 一定要注意。
 * SSE-C方式指定的密钥就是一个AWS平台独立的满足AES256要求的密钥串即可；
 * <p>
 * 三个值
 *
 * <ul>
 *     <li>MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR</li>
 *     <li>TVV3Zy91dkcxa0lJdUJ5SlVJU0huQW04VEs0c0ErUFI=</li>
 *     <li>nnd7u25CkQx4oUdgF1strw==</li>
 * </ul>
 */
@Slf4j
public class AES256Secret {

    public static final String AES256_NAME = "AES256";

    public static final String SAMPLE_SECRET = "MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR";

    private String secret;

    public static final AES256Secret SAMPLE_AES256_SECRET = new AES256Secret(SAMPLE_SECRET);

    public AES256Secret(String secret) {
        this.secret = secret;
    }

    public AES256Secret() throws NoSuchAlgorithmException {
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(256);
        SecretKey secretKey = aes.generateKey();
        log.info(" format {} and algorithm {}   ", secretKey.getFormat(), secretKey.getAlgorithm());
        this.secret = new String(secretKey.getEncoded(), StandardCharsets.UTF_8);
    }

    public String getSSECustomerKey() {
        return Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }


    public String getSSECustomKeyMD5() {
        return Md5Utils.md5AsBase64(secret.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {

    }

}
