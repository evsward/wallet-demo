package com.yhyy.wallet.service;
import java.math.BigDecimal;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.yhyy.wallet.service.impl.WithDrawServiceHystrix;

@FeignClient(value = "coin-account", fallback = WithDrawServiceHystrix.class)
public interface WithDrawService{

	/**
	 * 提币确认回调接口
	 * @param address
	 * @param currencyType
	 * @param userName
	 * @param hxId
	 * @param amount
	 * @return
	 */
	@RequestMapping(value="/client/withdraw/confirmCallBack",method=RequestMethod.POST)
	public String confirmCallback(@RequestParam("address") String address,@RequestParam("currencyType") int currencyType,
			@RequestParam("userName")String userName,@RequestParam("hxId")String hxId,@RequestParam("amount")BigDecimal amount);
}
