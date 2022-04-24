package top.cuteworld.awsdemo.aws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.GetAccountAuthorizationDetailsResponse;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class Config {

    /**
     * 默认采用本地配置的gws profile
     *
     * @return
     */
    @Bean
    public AwsCredentialsProvider buildCnCredential() {
        return ProfileCredentialsProvider.builder().profileName("gws").build();
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
        return S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.US_EAST_2).build();
    }
    git filter-branch --index-filter \
            'git rm -rf --cached --ignore-unmatch src/main/java/top/cuteworld/awsdemo/aws/Config.java' HEAD

    @Bean
    public KmsClient kmsClient(AwsCredentialsProvider awsCredentialsProvider) {
        return KmsClient.builder().credentialsProvider(awsCredentialsProvider).build();
    }

    @Bean
    public StsClient stsClient(AwsCredentialsProvider awsCredentialsProvider) {
        return StsClient.builder().credentialsProvider(awsCredentialsProvider).build();
    }

    @Bean
    public AES256Secret aes256Secret() {
        return AES256Secret.SAMPLE_AES256_SECRET;
    }

    @Bean
    public SSE_C_KEY sse_c_key() {
        return new SSE_C_KEY();
    }
}
