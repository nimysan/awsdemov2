# S3的服务器端加密和客户端加密的理解

## 服务器端加密

* SSE-S3

* SSE-KMS

* SSE-C

### SSE-S3 - (Amazon S3-managed keys (SSE-S3))

不关心密钥在哪是什么, S3完全内部处理

```bash
#使用托管的aws/kms
aws s3api put-object --bucket emr.cuteworld.top --key sse/sses3.obj --body a.txt --server-side-encryption AES256
#output
{
    "ETag": "\"4a470309809c421ed970b16fcd921974\"",
    "ServerSideEncryption": "AES256"
}

```

### SSE-KMS - AWS Key Management Service key (SSE-KMS)

> 这里需要跟AWS KMS的customer managed keys区分开来. 无论是使用aws/kms这种服务托管的key, 还是采用 AWS Kms托管的CMK, 这种都是 SSE-KMS.

```bash
#使用托管的aws/kms
aws s3api put-object --bucket emr.cuteworld.top --key sse/ssekms.obj --body a.txt --server-side-encryption aws:kms

#指定keyId
aws s3api put-object --bucket emr.cuteworld.top --key sse/x --server-side-encryption aws:kms --ssekms-key-id mrk-f424d5d9d32f42db945378266270a6b3
{
    "ETag": "\"0ef02c50759a4584128b3c26ce655393\"",
    "ServerSideEncryption": "aws:kms",
    "SSEKMSKeyId": "arn:aws:kms:us-east-2:390468416359:key/mrk-f424d5d9d32f42db945378266270a6b3",
    "BucketKeyEnabled": true
}


aws s3api put-object --bucket emr.cuteworld.top --key sse/ssecms.obj --body a.txt --sse-customer-algorithm AES256 --sse-customer-key MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR
# 

aws s3api put-object --acl public-read --sse-customer-algorithm=AES256 --sse-customer-key=MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR --body ~/Downloads/copied/CNC.mp4 --bucket decipherers --key gmdata-dev/hugeVideo.mp4 

```



### SSE-C  不借助任何AWS的KEY服务, 完全自己生成和保管加密需要的AES256的加密密钥

自己提供一个完全与AWS KMS服务无关的符合条件的 AES256的secret key. 在put-object和get-object的时候, 都必须带着这个secret key.

>  *MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR*  这是一个可以工作的sse-customer-key. 到底是需要什么格式的

```bash
#上传
aws s3api put-object --bucket emr.cuteworld.top --key sse/sse-c.obj --body a.txt --sse-customer-algorithm AES256 --sse-customer-key MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR

#返回 
{
    "ETag": "\"b97b50b7c9805150068e3f36afd44302\"",
    "SSECustomerAlgorithm": "AES256",
    "SSECustomerKeyMD5": "nnd7u25CkQx4oUdgF1strw=="
}

# 获取
aws s3api get-object --bucket emr.cuteworld.top --key sse/sse-c.obj --sse-customer-algorithm AES256 --sse-customer-key MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR hello

获取内容并写入到名为hello的文件里面去

# 通过 --debug 可以看到 https 请求

PUT
/emr.cuteworld.top/sse/sse-c.obj

content-md5:SkcDCYCcQh7ZcLFvzZIZdA==
host:s3.us-east-2.amazonaws.com
x-amz-content-sha256:UNSIGNED-PAYLOAD
x-amz-date:20220424T140353Z
x-amz-server-side-encryption-customer-algorithm:AES256
x-amz-server-side-encryption-customer-key:TVV3Zy91dkcxa0lJdUJ5SlVJU0huQW04VEs0c0ErUFI=
x-amz-server-side-encryption-customer-key-md5:nnd7u25CkQx4oUdgF1strw==

```

这里注意:

|命令参数|命令参数值|https headers值|
|--|--|--|
| --sse-customer-key | MUwg/uvG1kIIuByJUISHnAm8TK4sA+PR | x-amz-server-side-encryption-customer-key:TVV3Zy91dkcxa0lJdUJ5SlVJU0huQW04VEs0c0ErUFI= |
| --sse-customer-algorithm | AES256 | x-amz-server-side-encryption-customer-algorithm:AES256 |
| AWS CLI自动完成 key的md5值 |  | nnd7u25CkQx4oUdgF1strw== |

## 纯客户端加密

在上传之前完成数据加密, 并将加密后的数据存入S3.

### 完整的客户端加密

[借AWS KMS做客户端加密](https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/UsingClientSideEncryption.html)
