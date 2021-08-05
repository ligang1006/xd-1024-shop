package net.gaven.service;

/**
 * @author: lee
 * @create: 2021/8/5 9:38 上午
 **/
public interface IMailService {
    /**
     * 发送邮箱
     *
     * @param to      发送方
     * @param subject 主题
     * @param content 内容
     */
    void sendMsg(String to, String subject, String content);
}
