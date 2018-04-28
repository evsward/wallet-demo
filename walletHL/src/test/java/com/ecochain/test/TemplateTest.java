package com.ecochain.test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ecochain.EcoWalletApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoWalletApplication.class)
public class TemplateTest {
    @Autowired
    TemplateEngine templateEngine;
    @Test
    public void testTemplate() {
        
        Locale locale = LocaleContextHolder.getLocale();
        final Context ctx = new Context(locale);
        
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("content", "中文");
        ctx.setVariables(content);
        String text = templateEngine.process("commomTemplate", ctx);
        System.out.println(text);
    }
}
