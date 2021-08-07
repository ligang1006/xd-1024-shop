package net.gaven.service;

import net.gaven.enums.SendCodeEnum;
import net.gaven.util.JsonData;

/**
 * @author: lee
 * @create: 2021/8/5 11:16 上午
 **/
public interface INotifyService {
    /**
     * 发送验证码
     *
     * @param sendCodeEnum
     * @param to
     * @return
     */
    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);

    /**
     * 校验验证码
     *
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    boolean checkCode(SendCodeEnum sendCodeEnum, String to, String code);

}
