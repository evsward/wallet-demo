package com.ecochain.util;

import java.awt.image.BufferedImage;

/**
 * 验证码图片封装
 */
public class ValidationCodeWrap {
    public BufferedImage image;
    private ValidationCode vc;

    public ValidationCode getVc() {
        return vc;
    }

    public void setVc(ValidationCode vc) {
        this.vc = vc;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }


}