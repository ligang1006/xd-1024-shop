package net.gaven.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.gaven.mapper.UserMapper;
import net.gaven.service.IUserService;
import net.xdclass.model.UserDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 二当家小D
 * @since 2021-08-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

}
