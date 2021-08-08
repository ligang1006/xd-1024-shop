package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.SendCodeEnum;
import net.gaven.mapper.UserMapper;
import net.gaven.model.UserDO;
import net.gaven.request.UserRegisterRequest;
import net.gaven.service.INotifyService;
import net.gaven.service.IUserService;
import net.gaven.util.JsonData;
import net.gaven.util.MD5Util;
import net.gaven.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: lee
 * @create: 2021/8/7 3:00 下午
 **/
@Slf4j
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
        log.info("register user start");
        if (userRegisterRequest == null) {
            return JsonData.buildError("userRegisterRequest is not allow null");
        }

        boolean checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER,
                userRegisterRequest.getMail(), userRegisterRequest.getCode());
        if (!checkCode) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        //账号唯一性检查(TODO)
        boolean checkUnion = checkUnion(userRegisterRequest.getMail());
        if (checkUnion) {
            saveUserDO(userRegisterRequest);
            //新注册用户福利发放(TODO)
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }

    }

    /**
     * 校验用户是否唯一
     *
     * @param mail
     * @return
     */
    private boolean checkUnion(String mail) {
        //高并发有问题
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail", mail);
        List<UserDO> userDOList = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userDOList)) {
            return true;
        }
        return false;
    }

    private void saveUserDO(UserRegisterRequest userRegisterRequest) {
        //密码加密（TODO）
        String cryptPwd = MD5Util.getCryptPwd(userRegisterRequest.getPwd(), "$1$" + RandomUtil.getStringNumRandom(8));
        //账号唯一性检查(TODO)
        //入库
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userRegisterRequest, userDO);
        userDO.setSlogan("ni hao 每一天");
        userDO.setCreateTime(new Date());
        userDO.setSecret(cryptPwd);
        userDO.setHeadImg(userRegisterRequest.getHeadImg());
        userMapper.insert(userDO);
    }
}
