package top.cuteworld.awsdemo.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;
import top.cuteworld.awsdemo.aws.S3RepositoryBySSE_C;

import java.util.Map;


@RestController
@RequestMapping(value = "aws")
@Slf4j
@AllArgsConstructor
public class HelloController {

    private StsClient stsClient;
    private S3RepositoryBySSE_C s3Repository;

    @GetMapping
    public String health() {
        String user = System.getenv("USER");
        if ("ec2-user".equals(user)) {
            return "ec2 + " + user;
        } else {
            return "normal " + user;
        }
    }

    @GetMapping("sts")
    public String stsInfo() {
        GetCallerIdentityResponse callerIdentity = stsClient.getCallerIdentity();
        log.info("arn {} account {} userId {}", callerIdentity.arn(), callerIdentity.account(), callerIdentity.userId());
        return callerIdentity.toString();
    }

    @GetMapping("s3/sse_c_put")
    public void s3(String filename, String s3Key, String s3BucketName) throws Exception {
        s3Repository.uploadWithSSEC(filename, s3Key, s3BucketName);
    }

    @GetMapping("s3/sse_c_get")
    public Map<String, String> s3get(String s3Key, String s3BucketName) throws Exception {
        return Map.of("key", s3Key, "objectContent", s3Repository.getObjectWithSSE_C(s3Key, s3BucketName));
    }

}

