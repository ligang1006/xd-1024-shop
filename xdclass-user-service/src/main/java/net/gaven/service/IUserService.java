package net.gaven.service;


import com.baomidou.mybatisplus.extension.service.IService;
import net.gaven.model.UserDO;
import net.gaven.request.UserLoginRequest;
import net.gaven.request.UserRegisterRequest;
import net.gaven.util.JsonData;
import net.gaven.vo.UserVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2021-08-03
 */
public interface IUserService {
    /**
     * 用户注册
     * <p>
     * 邮箱验证码验证
     * 密码加密
     * 账号唯一性检查
     * 插入数据库
     * 新注册用户福利发放
     *
     * @param userRegisterRequest
     * @return
     */
    JsonData registerUser(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param loginRequest
     * @return
     */
    JsonData login(UserLoginRequest loginRequest);

    /**
     * 获取用户详情信息
     *
     * @return
     */
    UserVO getUserDetail();

}
