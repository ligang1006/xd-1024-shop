package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.SendCodeEnum;
import net.gaven.feign.CouponFeignService;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.UserMapper;
import net.gaven.model.LoginUser;
import net.gaven.model.UserDO;
import net.gaven.request.NewUserCouponRequest;
import net.gaven.request.UserLoginRequest;
import net.gaven.request.UserRegisterRequest;
import net.gaven.service.INotifyService;
import net.gaven.service.IUserService;
import net.gaven.util.JsonData;
import net.gaven.util.MD5Util;
import net.gaven.util.RandomUtil;
import net.gaven.util.yonyou.JWTUtil;
import net.gaven.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
    @Resource
    private UserMapper userMapper;
    @Autowired
    private CouponFeignService couponFeignService;


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
    @Transactional(rollbackFor = Exception.class)
    @Override
//    @GlobalTransactional
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
        //账号唯一性检查
        boolean checkUnion = checkUnion(userRegisterRequest.getMail());
        if (checkUnion) {
            UserDO userDO = saveUserDO(userRegisterRequest);
            userRegisterRequest.setId(userDO.getId());
            //新注册用户福利发放
            addCoupon(userRegisterRequest);
            //TODO 这里是模拟出错，分布式事务回滚
//            int i = 1 / 0;
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }

    }

    private void addCoupon(UserRegisterRequest userRegisterRequest) {
        NewUserCouponRequest request = new NewUserCouponRequest();
        request.setUserId(userRegisterRequest.getId());
        request.setName(userRegisterRequest.getName());
        JsonData newCoupon = couponFeignService.getNewCoupon(request);
        if (newCoupon.getCode() != 0) {
            throw new RuntimeException("用户注册，发放优惠卷失败");
        }
        log.info("获取优惠卷成功{},{}", newCoupon.toString());
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

    private UserDO saveUserDO(UserRegisterRequest userRegisterRequest) {
        //密码加密（TODO）
        String salt = "$1$" + RandomUtil.getStringNumRandom(8);
        String cryptPwd = MD5Util.getCryptPwd(userRegisterRequest.getPwd(), salt);
        //账号唯一性检查(TODO)
        //入库
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userRegisterRequest, userDO);
        userDO.setSlogan("ni hao 每一天");
        userDO.setCreateTime(new Date());
        userDO.setSecret(salt);
        userDO.setPwd(cryptPwd);
        userDO.setHeadImg(userRegisterRequest.getHeadImg());
        int insert = userMapper.insert(userDO);
        return userDO;
    }

    /**
     * 1、拿到mail到数据库进行匹配,如果存储在
     * 2、拿到对应到盐，通过pwd+盐 ==>校验与数据库到pwd是否相同
     * <p>
     * token的刷新。前端存储token和expireTime和re
     * <p>
     * 加强校验 ip进行校验
     *
     * @param loginRequest
     * @return
     */
    @Override
    public JsonData login(UserLoginRequest loginRequest) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail", loginRequest.getMail());
        List<UserDO> userDOS = userMapper.selectList(queryWrapper);
        if (userDOS != null && userDOS.size() == 1) {
            /*注册过*/
            //获取盐
            String pwd = loginRequest.getPwd();
            String salt = userDOS.get(0).getSecret();
            String dbPwd = userDOS.get(0).getPwd();
            //加盐获取加盐后的pwd
            String cryptPwd = MD5Util.getCryptPwd(pwd, salt);
            //与数据库的pwd进行比较，增加更强的校验，传ip ip也进行校验
            if (cryptPwd.equals(dbPwd)) {
                String token = generateJWT(userDOS.get(0));
                log.info("token =={}", token);
                //生成token TODO
                return JsonData.buildSuccess(token);
            } else {
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        } else {
            /*未注册过*/
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }

    }

    private String generateJWT(UserDO userDO) {
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userDO, loginUser);
        String token = JWTUtil.generateJsonWebToken(loginUser);
        return token;
    }

    @Override
    public UserVO getUserDetail() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (loginUser != null) {
            Long userId = loginUser.getId();
            UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("id", userId));
            if (userDO != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userDO, userVO);
                return userVO;
            }
            return null;
        } else {
            return null;
        }

    }
}
