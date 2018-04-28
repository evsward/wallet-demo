package com.yhyy.wallet.service.impl;

import java.math.BigDecimal;

import com.yhyy.wallet.service.DepositService;
import org.springframework.stereotype.Component;

@Component
public class DepositServiceHystrix implements DepositService {

	@Override
	public String broadcastCallback(String address, int currencyType,
			String userName, String hxId, BigDecimal amount) {
		return "-9999";
	}

	@Override
	public String confirmCallback(String address, int currencyType,
			String userName, String hxId, BigDecimal amount) {
		return "-9999";
	}

}
