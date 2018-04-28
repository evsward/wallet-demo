package com.ecochain.util;

public class RegexpConstant {
    /**
     * 18位身份证|15位身份证
     */
    //public static final String IDCRAD_PATTERN = "(^(\\d{17})(\\d|[xX])$)?";
	//public static final String IDCRAD_PATTERN = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[X])$)$";
	public static final String IDCRAD_PATTERN = "^(\\d{15}|\\d{17}[\\dX])$";
	//public static final String IDCRAD_PATTERN = "^([0-9]{17}([0-9]|X))|([0-9]{15})$";

    /**
     * 邮箱
     */
    public static final String EMAIL_PATTERN = "^(\\w)+(\\.\\w+)*@gomeholdings\\.com$";

    /**
     * 手机号
     */
    public static final String MOBILE_PATTERN = "^(1[3|4|5|7|8])[0-9]{9}$";
    /**
     * 密码（包含数字和字母）
     */
    public static final String PASSWORD_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";

    /**
     * 密码（包含数字、字母和任意特殊字符两种及以上）
     */
    public static final String PASSWORD_PATTERN2 = "^(?![A-z]+$)(?!\\d+$)(?![\\W]+$)\\S{8,20}$";

    /**
     * 密码（包含数字、字母和（~!@#$%^&*()_~！@#￥%……&*（）——）中的特殊字符中的两种及以上）
     */
    public static final String PASSWORD_PATTERN3 = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![~!@#$%^&*()_~！@#￥%……&*（）——]+$)[A-z\\d~!@#$%^&*()_~！@#￥%……&*（）——]{8,20}$";

    /**
     * 密码（包含数字、字母和（~!@#$%^&*()_~！@#￥%……&*（）——）中的特殊字符三种以上)
     */
    public static final String PASSWORD_PATTERN4 = "(?=.*[A-z])(?=.*\\d)(?=.*[~!@#$%^&*()_~！@#￥%……&*（）——])[A-z\\d~!@#$%^&*()_~！@#￥%……&*（）——]{8,20}$";


    //public static final String PASSWORD_PATTERN5 = "^(?![0-9]+$)(?![A-z]+$)(?![^A-z0-9]+$)^.{8,20}$";

    /**
     * 不能含有特殊字符
     */
    public static final String NAME_PATTERN = "^([a-zA-Z0-9\u4e00-\u9fa5()\\-（）——]*)$";

}
