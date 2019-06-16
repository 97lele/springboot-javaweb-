package com.gdut.xg.shop.service.impl;

import com.gdut.xg.shop.entity.User;
import com.gdut.xg.shop.dao.UserDao;
import com.gdut.xg.shop.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

}
