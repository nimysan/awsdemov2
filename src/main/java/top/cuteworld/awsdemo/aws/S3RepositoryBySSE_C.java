package top.cuteworld.awsdemo.aws;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.io.File;

/**
 * 通过这个访问S3对象（含上传S3）
 */
@AllArgsConstructor
@Slf4j
@Service
public class S3RepositoryBySSE_C {

    private final S3Client s3Client;

    private final KmsClient kmsClient;

    private final AES256Secret aes256Secret;

    private final SSE_C_KEY sse_c_key;


    /**
     * 完全通过客户自管的KEY去加解密， 这个客户自管的KEY通过 {@link SSE_C_KEY} 实现；
     *
     * @param filename
     * @param s3Key
     * @param s3Bucket
     * @throws Exception
     */
    public void uploadWithSSEC(String filename, String s3Key, String s3Bucket) throws Exception {
//        SSE_C_KEY sse_c_key = new SSE_C_KEY();
//        log.info("Try to put object  {} to {}/{} with kms {} with md5 {}", filename, s3Bucket, s3Key, sseCSecretKey.getSSECustomerKey(), sseCSecretKey.getSSECustomKeyMD5());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(s3Key)
                /* 构建SSE-CMK */
                .sseCustomerAlgorithm("AES256")
                .sseCustomerKey(sse_c_key.getSSECustomKey())
                .sseCustomerKeyMD5(sse_c_key.getSSECustomKeyMD5())
                /* 构建SSE-CMK */
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromFile(new File(filename)));
//        putObjectResponse.toString();
        log.info("The response is" + putObjectResponse.toString());
    }


    public String getObjectWithSSE_C(String s3key, String s3Bucket) throws Exception {
//        SSE_C_KEY sse_c_key = new SSE_C_KEY();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                /* sse-c set up */
                .sseCustomerAlgorithm(AES256Secret.AES256_NAME)
                .sseCustomerKey(sse_c_key.getSSECustomKey())
                .sseCustomerKeyMD5(sse_c_key.getSSECustomKeyMD5())
                /* */
                .bucket(s3Bucket)
                .key(s3key)
                .build();

        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
        //InputStreamReader inputStreamReader = new InputStreamReader(object);
        String objectContent = IoUtils.toUtf8String(object);
        log.info("The response is : {} ", objectContent);
        return objectContent;
    }
}

