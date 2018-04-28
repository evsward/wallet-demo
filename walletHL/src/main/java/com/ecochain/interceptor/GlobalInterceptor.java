package com.ecochain.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.annotation.AppLoginVerify;
import com.ecochain.annotation.LoginVerify;
import com.ecochain.constant.CodeConstant;
import com.ecochain.util.Const;
import com.ecochain.wallet.controller.AjaxResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    private static Logger log = Logger.getLogger(GlobalInterceptor.class);

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        log.info("RequestURL==" + request.getRequestURI());
        String path = request.getServletPath();

        if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
            return true;
        }else{
    		//验证登录
    		if(handler instanceof HandlerMethod){
    			HandlerMethod method = (HandlerMethod)handler;
    			if(method != null){
    				LoginVerify loginVerify = method.getMethod().getAnnotation(LoginVerify.class);
    				if(loginVerify != null){
    				    JSONObject user = null;
				        user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
				        log.info("======current user=========" + user);
    					if(user == null){
                            response.getWriter().print(JSONObject.toJSONString(AjaxResponse.falied("未登录", CodeConstant.UNLOGIN)));
                            response.getWriter().close();
                            return false;
                        }
                    } else {
                        AppLoginVerify apploginVerify = method.getMethod().getAnnotation(AppLoginVerify.class);
                        if (apploginVerify != null) {

                            if (redisTemplate == null) {
                                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                                redisTemplate = (RedisTemplate<String, Object>) factory.getBean("redisTemplate");
                            }

                            String token = request.getHeader("x-auth-token");

                            if (token != null) {
                                Map userInfo = (Map) redisTemplate.opsForValue().get("app:usertoken:" + token);
                                request.getSession().setAttribute("User", userInfo);
                            }


                            JSONObject user = null;
                            user = JSONObject.parseObject(JSON.toJSONString(request.getSession().getAttribute("User")));
                            if (user == null) {
                                response.getWriter().print(JSONObject.toJSONString(AjaxResponse.falied("未登录", CodeConstant.APPUNLOGIN)));
                                response.getWriter().close();
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
