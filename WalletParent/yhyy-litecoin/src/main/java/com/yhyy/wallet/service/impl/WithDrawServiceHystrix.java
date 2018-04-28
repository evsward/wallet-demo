package com.yhyy.wallet.service.impl;

import com.yhyy.wallet.service.WithDrawService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithDrawServiceHystrix implements WithDrawService {

	@Override
	public String confirmCallback(String address, int currencyType,
			String userName, String hxId, BigDecimal amount) {
		return "-9999";
	}

}
