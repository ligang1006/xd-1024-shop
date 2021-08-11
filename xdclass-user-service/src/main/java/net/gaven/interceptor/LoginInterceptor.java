package net.gaven.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.model.LoginUser;
import net.gaven.util.CommonUtil;
import net.gaven.util.yonyou.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录的拦截器
 *
 * @author: lee
 * @create: 2021/8/10 9:23 上午
 **/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    /**
     * 通过token校验用户是否登录
     * case1：token==null    false 并且打印
     * case2：token!=null    是否能够校验通过 通过返回true并将用户信息解析出来
     * case3：不能通过返回false 打印
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("token");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = request.getParameter("token");
        }
        //未登录
        if (StringUtils.isEmpty(accessToken)) {
            CommonUtil.sendJsonMessage(response, BizCodeEnum.ACCOUNT_UNLOGIN.getMessage());
            return false;

        } else {
            //登录了，解析用户数据
            Claims claims = JWTUtil.checkToken(accessToken);
            if (claims != null) {
                Long id = Long.valueOf(claims.get("id").toString());
                String mail = claims.get("mail").toString();
                String headImg = claims.get("head_img").toString();
                String name = claims.get("name").toString();
                LoginUser loginUser = new LoginUser();
                loginUser.setName(name);
                loginUser.setHeadImg(headImg);
                loginUser.setId(id);
                loginUser.setMail(mail);

                /*1、通过request传递
                 * 2、通过threadLocal获取   */

                //传递loginUser 通过threadLocal获取
                threadLocal.set(loginUser);
//                request.setAttribute("loginUser",loginUser);
                return true;
            } else {

            }

        }
        CommonUtil.sendJsonMessage(response, BizCodeEnum.ACCOUNT_UNLOGIN.getMessage());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
