package net.gaven.service.impl;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import net.gaven.config.OSSClientConfiguration;
import net.gaven.service.IFileService;
import net.gaven.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: lee
 * @create: 2021/8/7 10:26 上午
 **/
@Slf4j
@Service
public class FileServiceImpl implements IFileService {
    @Autowired
    private OSSClientConfiguration ossClientConfiguration;


    @Override

    public String uploadUserImage(MultipartFile file) {
        //获取相关配置
        // 创建ClientConfiguration实例，按照您的需要修改默认参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 关闭CNAME选项。
        conf.setSupportCname(false);
        String accessKeyId = ossClientConfiguration.getAccessKeyId();
        String accessKeySecret = ossClientConfiguration.getAccessKeySecret();
        String bucketName = ossClientConfiguration.getBucketname();
        String endpoint = ossClientConfiguration.getEndpoint();
        //创建oss对象
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);

        //JDK8新特性写法，构建路径 文件名格式  时间+文件名（uuid）+
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //文件夹
        String folder = dtf.format(localDateTime);
        String originalFilename = file.getOriginalFilename();
        //后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = CommonUtil.generateUUID();
        //在oss上创建文件夹test路径

        String newFileName = "user/" + folder + "/" + fileName + extension;
        //连接
        //    public PutObjectResult putObject(String bucketName, String key, InputStream input)
        //
        // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        try {
            log.info("upload file to aliyun oss system starting..." + newFileName);
            PutObjectResult putObjectResult
                    = ossClient.putObject(bucketName, newFileName, file.getInputStream());
            if (putObjectResult != null) {
                //https://lee-beijing.oss-cn-beijing.aliyuncs.com/test/1.jpg
                String url = "https://" + bucketName + "." + endpoint + "/" + newFileName;
                return url;
            }
        } catch (IOException e) {
            log.error("upload file fail please check you file" + e);
            //关闭OSS,不如会造成内存泄漏
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}
