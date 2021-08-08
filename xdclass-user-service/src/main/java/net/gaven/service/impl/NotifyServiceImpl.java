package net.gaven.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.SendCodeEnum;
import net.gaven.service.IMailService;
import net.gaven.service.INotifyService;
import net.gaven.util.CheckUtil;
import net.gaven.util.JsonData;
import net.gaven.util.MyRedisTemplate;
import net.gaven.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static net.gaven.constant.CacheKey.CHECK_CODE_KEY;
import static net.gaven.constant.Constant.E_MAIL_CONTENT;
import static net.gaven.constant.Constant.E_MAIL_SUBJECT;

/**
 * @author: lee
 * @create: 2021/8/5 12:45 下午
 **/
@Slf4j
@Service
public class NotifyServiceImpl implements INotifyService {
    @Autowired
    private MyRedisTemplate redisTemplate;
    @Autowired
    private IMailService mailService;

    /**
     * 前置：判断是否重复发送
     * <p>
     * 1、存储验证码到缓存，进行校验
     * 2、发送邮箱验证码
     * <p>
     * 后置：存储发送记录
     * <p>
     * 这里存在原子性问题
     * 和重复存储问题
     *
     * @param sendCodeEnum
     * @param to
     * @return
     */
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        //这里通过时间戳的方法，进行实现
        String cacheKey = String.format(CHECK_CODE_KEY, sendCodeEnum.name(), to);
        //校验是否发送过验证码   xx_时间戳
        String cacheValue = redisTemplate.getValue(cacheKey);
        //之前发送过,校验时间60s内不能发送
        if (StringUtils.isNotEmpty(cacheValue)) {
            Long firstStore = Long.parseLong(cacheValue.split("_")[1]);
            if ((System.currentTimeMillis() - firstStore) < 60 * 1000) {
                log.info("1分钟内不能重复获取验证码 {} s", (System.currentTimeMillis() - firstStore) / 1000);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        String randomNum = RandomUtil.getRandomNum(6);
        //777777_20210807
        String value = randomNum + "_" + System.currentTimeMillis();
        //保存key
        redisTemplate.set(cacheKey, value, 300, TimeUnit.SECONDS);
        //发邮件
        if (CheckUtil.isEmail(to)) {
            //发送随机数作为验证码
            mailService.sendMsg(to, E_MAIL_SUBJECT, E_MAIL_CONTENT + randomNum);
//            mailService.sendMsg(to, E_MAIL_SUBJECT, String.format(E_MAIL_CONTENT, randomNum));
            return JsonData.buildSuccess("send e-mail success!");
        } else if (CheckUtil.isPhone(to)) {

        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }

    /**
     * 校验验证码
     * 到redis中校验该code是否正确
     *
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String to, String code) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(to)) {
            return false;
        }
        // CHECK_CODE_KEY+ SendCodeEnum.name+to
        String cacheKey = String.format(CHECK_CODE_KEY, sendCodeEnum.name(), to);
        //上面的存储方法，value的格式777777_20210807
        String value = (String) redisTemplate.get(cacheKey);
        if (StringUtils.isNotEmpty(value)) {
            String cacheValue = value.split("_")[0];
            if (cacheValue.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
