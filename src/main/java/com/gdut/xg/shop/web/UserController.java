package com.gdut.xg.shop.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.xg.shop.VO.ResultVO;
import com.gdut.xg.shop.VO.UserVO;
import com.gdut.xg.shop.entity.User;
import com.gdut.xg.shop.enums.ErrorEnum;
import com.gdut.xg.shop.service.IUserService;
import com.gdut.xg.shop.util.CommonUtil;
import com.gdut.xg.shop.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
@Autowired
private StringRedisTemplate redisTemplate;
    @GetMapping("/whetherLogin")
    @ResponseBody
    public ResultVO whetherLogin(HttpServletRequest req) {
        Cookie cookie = null;
        if ((cookie = CommonUtil.getCookie(req, CommonUtil.USERCOOKIE)) != null) {
            User u = userService.lambdaQuery().eq(User::getToken, cookie.getValue()).one();
            if (u != null && !CommonUtil.OFF.equals(u.getToken()) && u.getToken().equals(cookie.getValue())) {
                return ResultVOUtil.success(null, true, u.getAccount() + "-" + u.getUserId());
            }
        }
        return ResultVOUtil.error(ErrorEnum.NOTLOGIN);

    }

    @GetMapping("/getSelf")
    @ResponseBody

    public ResultVO getSelf(HttpServletRequest req) {
        Cookie cookie = null;
        if ((cookie = CommonUtil.getCookie(req, CommonUtil.USERCOOKIE)) != null) {
            User u = userService.lambdaQuery().eq(User::getToken, cookie.getValue()).one();
            if (u != null && !CommonUtil.OFF.equals(u.getToken()) && u.getToken().equals(cookie.getValue())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user", new UserVO(u));
                return ResultVOUtil.success(map, true);

            }
        }
        return ResultVOUtil.error(ErrorEnum.NOTLOGIN);

    }

    @GetMapping("/getUser")    @ResponseBody

    public ResultVO getUser(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize,
                            @RequestParam("account") String account
    ) {

        IPage<User> page = userService.lambdaQuery().like(User::getAccount, account).page(new Page(pageIndex, pageSize));
        List<UserVO> list = page.getRecords().
                stream().map(e -> {
            UserVO u = new UserVO(e);
            return u;
        }).collect(Collectors.toList());
        Map map = new HashMap();
        map.put("list", list);
        map.put("page", page.getPages());
        map.put("pageIndex", pageIndex);
        return ResultVOUtil.success(map);

    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (null == cookies) {
        } else {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);// 立即销毁cookie
                cookie.setPath("/");
                resp.addCookie(cookie);
            }
        }
        req.getSession().invalidate();
        return "redirect:http://localhost:8080/admin/index.html";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (null == cookies) {
        } else {
            for (Cookie cookie : cookies) {
                if(CommonUtil.CARTCOOKIE.equals(cookie.getName())){
                    redisTemplate.delete(cookie.getValue());
                }
                cookie.setValue(null);
                cookie.setMaxAge(0);// 立即销毁cookie
                cookie.setPath("/");
                resp.addCookie(cookie);
            }
        }
        req.getSession().invalidate();
        return "redirect:http://localhost:8080/user/product.html";
    }

    @PostMapping("/sign")    @ResponseBody

    public ResultVO sign(@RequestParam("password") String password,
                         @RequestParam("account") String account) {
        User user = new User(null, account, password, 0);
        user.setStatus(1).setToken(CommonUtil.OFF);
        return ResultVOUtil.success(null, userService.save(user));
    }

    @PostMapping("/update")    @ResponseBody

    public ResultVO update(@RequestParam("account") String account, @RequestParam("password") String password,
                           @RequestParam(value = "address", required = false) String address,
                           @RequestParam(value = "mobile", required = false) String mobile, @RequestParam(value = "email", required = false) String email,
                           @RequestParam("userId") Integer userId) {
        User user = new User(userId, account, password, address, mobile, email);
        return ResultVOUtil.success(null, userService.updateById(user));
    }

    @PostMapping("/admin/changeStatus")    @ResponseBody

    public ResultVO changeStatus(@RequestParam("status") Integer status, @RequestParam("userId") Integer userId) {
        return ResultVOUtil.success(null, userService.updateById(new User().setUserId(userId).setStatus(status)));
    }

    @PostMapping("/login")    @ResponseBody

    public ResultVO login(@RequestParam("account") String account, @RequestParam("password") String password, @RequestParam("role") Integer role, HttpServletResponse response) {
        User u = userService.lambdaQuery().eq(User::getAccount, account).one();
        if (Objects.isNull(u)) {
            return ResultVOUtil.error(ErrorEnum.USENOTEXISTS);
        }
        if (!u.getPassword().equals(password)) {
            return ResultVOUtil.error(ErrorEnum.PASSWORDERRO);
        }
        if (u.getStatus() == 0) {
            return ResultVOUtil.error(ErrorEnum.USERFROZEN);
        }
        if (role.equals(u.getRole())) {
            String cookieValue = UUID.randomUUID().toString().replaceAll("-", "");
            u.setToken(cookieValue);
            userService.updateById(u);
            Cookie cookie = new Cookie(CommonUtil.USERCOOKIE, cookieValue);
            cookie.setMaxAge(CommonUtil.MAXAGE);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResultVOUtil.success(null, true, u.getUserId().toString());
        } else {
            return ResultVOUtil.error(ErrorEnum.ROLEERROR);
        }
    }


}

