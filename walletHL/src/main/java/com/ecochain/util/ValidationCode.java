package com.ecochain.util;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证码图片封装
 */
public class ValidationCode implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7202799126028965830L;
    public String code;
    public String mobile;
    public long deadTime;
    public Date createTime;

    public String getCode() {
        return code;
    }

    public boolean validate(String vcode) {
        return !vcode.equalsIgnoreCase(code) ? false : (DateUtils.getDateTime().getTime() > deadTime ? false : true);
    }

    public boolean validate(String vmobile, String vcode) {

        return !mobile.equals(vmobile) ? false : validate(vcode);
    }

    public boolean getAgain() {
        return createTime.before(DateUtils.getDateByMinute(createTime, 1));
    }
}