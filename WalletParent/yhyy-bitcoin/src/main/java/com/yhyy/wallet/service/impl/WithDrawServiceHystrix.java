package com.yhyy.wallet.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.yhyy.wallet.service.WithDrawService;

@Component
public class WithDrawServiceHystrix implements WithDrawService {

	@Override
	public String confirmCallback(String address, int currencyType,
			String userName, String hxId, BigDecimal amount) {
		return "-9999";
	}

}
