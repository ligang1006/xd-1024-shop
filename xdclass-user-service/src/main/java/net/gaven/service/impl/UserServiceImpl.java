package net.gaven.service.impl;

import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.SendCodeEnum;
import net.gaven.mapper.UserMapper;
import net.gaven.model.UserDO;
import net.gaven.request.UserRegisterRequest;
import net.gaven.service.INotifyService;
import net.gaven.service.IUserService;
import net.gaven.util.JsonData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: lee
 * @create: 2021/8/7 3:00 下午
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private INotifyService notifyService;
    @Autowired
    private UserMapper userMapper;


    /**
     * 邮箱验证码验证
     * 密码加密（TODO）
     * 账号唯一性检查(TODO)
     * 插入数据库
     * 新注册用户福利发放(TODO)
     *
     * @param userRegisterRequest
     * @return
     */
    @Override
    public JsonData registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return JsonData.buildError("userRegisterRequest is not allow null");
        }
        boolean checkResult = false;
        boolean checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER,
                userRegisterRequest.getMail(), userRegisterRequest.getCode());
        if (!checkCode) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        //密码加密（TODO）

        //账号唯一性检查(TODO)
        //入库
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userRegisterRequest, userDO);
        userDO.setSlogan("ni hao 每一天");
        userDO.setCreateTime(new Date());
        userMapper.insert(userDO);
        //新注册用户福利发放(TODO)
        return JsonData.buildSuccess();
    }
}
