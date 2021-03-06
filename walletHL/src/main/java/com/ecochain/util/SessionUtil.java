//package com.ecochain.util;
//
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SessionUtil{
//
//    @Autowired
//    private RedisClient redisClient;
//    //用户超时分钟
//	private Integer sexp = 30*60;
//	
//	//验证码超时时间
//	private Integer cexp = 10;
//	
//	//把用户名入到 Redis中   1小时
//	public void setAttributeForUser(String name, String value) {
//	    if(name.contains("app")){
//	        redisClient.set(name+":"+LoginConstant.USER_NAME, value, sexp*60*2*24*30);
//	    }else{
//	        redisClient.set(name+":"+LoginConstant.USER_NAME, value, sexp*60*2*24*30);
//	    }
//	}
//
//	//取用户信息
//    public String getAttibuteForUser(String name) {
//        if(!redisClient.exists(name + ":" + LoginConstant.USER_NAME)){
//            return "";
//        }
//        String value = (String)redisClient.get(name + ":" + LoginConstant.USER_NAME);
//        if(name.contains("app")){
//          //重新设置时间 
//            if(null != value){
//                //一个月
//                redisClient.set(name+":"+LoginConstant.USER_NAME, value, sexp*60*2*24*30);
//            } 
//        }else{
//            //重新设置时间 
//            if(null != value){
//                //30分钟
//                redisClient.set(name+":"+LoginConstant.USER_NAME, value, sexp*60);
//            } 
//        }
//        return value;
//    }
//    //删除用户信息
//    public void delAttibuteForUser(String name) {
//        if(redisClient.exists(name + ":" + LoginConstant.USER_NAME)){
//            redisClient.del(name + ":" + LoginConstant.USER_NAME);
//        }
//    }
//    
//	public void setAPPForUser(String name, String value) {
//	    redisClient.set(name+":"+LoginConstant.APP_USER_NAME, value, sexp*60*2*24*30);
//    }
//	
//	//取用户信息
//    public String getAPPForUser(String name) {
//        if(!redisClient.exists(name + ":" + LoginConstant.APP_USER_NAME)){
//            return "";
//        }
//        String value = (String)redisClient.get(name + ":" + LoginConstant.APP_USER_NAME);
//        //重新设置时间 
//        if(null != value){
//            //30分钟
//            redisClient.set(name+":"+LoginConstant.APP_USER_NAME, value, sexp*60*2*24*30);
//        }
//        return value;
//    }
//    //删除用户信息
//    public void delAPPForUser(String name) {
//        if(redisClient.exists(name + ":" + LoginConstant.APP_USER_NAME)){
//            redisClient.del(name + ":" + LoginConstant.APP_USER_NAME);
//        }
//    }
//	//把验证码放到Redis中  10分钟
//	public void setAttributeForCode(String name, String value) {
//	    redisClient.set(name+":"+LoginConstant.LOGIN_CODE, value, cexp*60);
//	}
//
//
//	
//	//取验证码
//	public  String getAttibuteForCode(String name) {
//		String vcode = (String)redisClient.get(name + ":" + LoginConstant.LOGIN_CODE);
//		return vcode;
//	}
//	public  void delAttibuteForCode(String name) {
//	    redisClient.del(name + ":" + LoginConstant.LOGIN_CODE);
//    }
//
//	//校验移动端token
//	public  boolean CheckToken(String token) {
//		if(StringUtil.isEmpty(token)){
//			return false;
//		}
//		String bt = new String(Base64.decodeBase64(token));
//		if(StringUtil.isEmpty(bt)){
//			return false;
//		}
//		String[] bts = bt.split(":");
//		if(bts.length > 0){
//			String account = bts[0];
//			String tomeOutStr = bts[1];
//			if(StringUtil.isEmpty(account) || StringUtil.isEmpty(tomeOutStr)){
//				return false;
//			}
//			long currentTime = System.currentTimeMillis();
//			if((Long.valueOf(tomeOutStr) - currentTime) < 0){
//				return false;
//			}
//            String serverToken = (String)redisClient.get(TokenConstant.TOKEN_CONSTANT+account);
//			if(StringUtil.isEmpty(serverToken)){
//				return false;
//			}
//			boolean equals = token.equals(serverToken);
//			return equals;
//		}
//		return false;
//	}
//
//	/*//生成移动端token并存放到redis
//	public  String setAttributeForToken(String accname, String pwd, long expiryOutBySeconds) {
//		long loginTime = System.currentTimeMillis();
//		String cookieValue = LoginAuthHelper.genAuthCookieValue(accname, pwd, TokenConstant.TOKEN_EXPIRYTIME*1000+loginTime, expiryOutBySeconds);
//		System.out.println(cookieValue);
////		redisStringTemplate.setex(TokenConstant.TOKEN_CONSTANT+accname, TokenConstant.TOKEN_EXPIRYTIME.intValue(), cookieValue);
//		JedisUtil.set(TokenConstant.TOKEN_CONSTANT+accname, cookieValue, TokenConstant.TOKEN_EXPIRYTIME.intValue());
//		return cookieValue;
//	}*/
//}
