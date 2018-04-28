//package com.ecochain.test;
//import java.util.HashMap;
//import java.util.Map;
////对接口进行测试
//public class TestMain {
//    private String url = "https://127.0.0.1:443/";
//    private String charset = "utf-8";
//    private HttpClientUtil httpClientUtil = null;
//
//    public TestMain(){
//        httpClientUtil = new HttpClientUtil();
//    }
//
//    public void test(){
//        String httpOrgCreateTest = url + "/login";
//        Map<String,String> createMap = new HashMap<String,String>();
//        createMap.put("username","admin");
//        createMap.put("password","123456");
//        String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,createMap,charset);
//        System.out.println("result:"+httpOrgCreateTestRtn);
//    }
//
//    public static void main(String[] args){
//        TestMain main = new TestMain();
//        main.test();
//    }
//}