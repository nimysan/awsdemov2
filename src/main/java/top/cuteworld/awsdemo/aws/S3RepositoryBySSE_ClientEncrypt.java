package top.cuteworld.awsdemo.aws;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3RepositoryBySSE_ClientEncrypt {

    public void put(){
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().build();

        //完全自己加密完了， 加密完可以在service端继续做sse-s3加密
    }
}
