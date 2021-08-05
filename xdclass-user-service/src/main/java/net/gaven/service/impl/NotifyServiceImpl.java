package net.gaven.service.impl;

import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.SendCodeEnum;
import net.gaven.service.IMailService;
import net.gaven.service.INotifyService;
import net.gaven.util.CheckUtil;
import net.gaven.util.JsonData;
import net.gaven.util.RandomNumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.gaven.constant.Constant.E_MAIL_CONTENT;
import static net.gaven.constant.Constant.E_MAIL_SUBJECT;

/**
 * @author: lee
 * @create: 2021/8/5 12:45 下午
 **/
@Service
public class NotifyServiceImpl implements INotifyService {

    @Autowired
    private IMailService mailService;

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        //发邮件
        if (CheckUtil.isEmail(to)) {
            String randomNum = RandomNumUtil.getRandomNum(6);
            //发送随机数作为验证码
            mailService.sendMsg(to, E_MAIL_SUBJECT, E_MAIL_CONTENT + randomNum);
//            mailService.sendMsg(to, E_MAIL_SUBJECT, String.format(E_MAIL_CONTENT, randomNum));
            return JsonData.buildSuccess("send e-mail success!");
        } else if (CheckUtil.isPhone(to)) {

        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
