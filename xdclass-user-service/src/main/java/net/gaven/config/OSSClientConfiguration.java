package net.gaven.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lee
 * @create: 2021/8/7 9:30 上午
 **/
@Data
@Configuration
public class OSSClientConfiguration {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketname}")
    private String bucketname;


//
//    // 创建ClientConfiguration实例，按照您的需要修改默认参数。
//    ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
//    // 关闭CNAME选项。
//    conf.setSupportCname(false);
//
//    // 创建OSSClient实例。
//    OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
//
//    // 关闭OSSClient。
//    ossClient.shutdown();
}
