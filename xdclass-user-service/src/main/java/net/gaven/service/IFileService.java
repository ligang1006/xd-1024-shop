package net.gaven.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: lee
 * @create: 2021/8/7 10:25 上午
 **/
public interface IFileService {
    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    String uploadUserImage(MultipartFile file);
}
