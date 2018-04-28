package com.ecochain;


import java.io.File;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ecochain.task.Ethereum.EtherHotWalletTool;
import com.ecochain.wallet.util.WalletAsync;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableRedisHttpSession
//@EnableDiscoveryClient
//@EnableFeignClients
public class EcoWalletApplication {
    
    static private String wallertRootPath = "C:\\ecowallet";
    
    static Logger log = LoggerFactory.getLogger(EcoWalletApplication.class);
    
    public static NetworkParameters params = MainNetParams.get();
    
    static String userName ="eco";

    @Autowired
    private WalletAsync walletAsync;

    @Autowired
    private EtherHotWalletTool etherHotWalletTool;
    
    public static WalletAppKit kit = new WalletAppKit(params, 
            new File(wallertRootPath + File.separatorChar + userName), userName );
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EcoWalletApplication.class, args);
        log.info("EcoWalletApplication-----started--//TODO  共享session所有方法全部加上security的登陆验证");
        
//        EcoWalletService ecoWalletService = context.getBean(EcoWalletService.class);
//        EcoWalletMapper mapper = context.getBean(EcoWalletMapper.class);
//        
//        List<EcoWallet> ecoWallets = ecoWalletService.listAllEcoWallets();
//        if (ecoWallets != null ){
//            for (EcoWallet ecoWallet : ecoWallets) {
//                ecoWallet.setPrivateKey(AES.encrypt(ecoWallet.getPrivateKey().getBytes()));
//                mapper.updateByPrimaryKey(ecoWallet);
//            }
//        }
        
        EcoWalletApplication.kit.startAsync();
        EcoWalletApplication.kit.awaitRunning();

        WalletAsync walletAsync = context.getBean(WalletAsync.class);
        walletAsync.run();

        System.out.println("==========EcoWalletApplication start==="
                + "提币手续费：三界宝的0.1%每次，莱特币0.03个LTC/次。"
                + "比特币0.002个BTC/次。以太坊0.01个ETH/次。========");

       /* SyncLedgerTxnsThread syncLedgerTxnsThread = (SyncLedgerTxnsThread) context.getBean("syncLedgerTxnsThread");
        AutomaticWithDrawThread automaticWithDrawThread = (AutomaticWithDrawThread) context.getBean("automaticWithDrawThread");
        UnconfirmTxnsCheckThread unconfirmTxnsCheckThread = (UnconfirmTxnsCheckThread) context.getBean("unconfirmTxnsCheckThread");
        AutoSanGatherThread autoSanGatherThread = (AutoSanGatherThread) context.getBean("autoSanGatherThread");

        ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
        service.scheduleWithFixedDelay(automaticWithDrawThread,0L, 60, TimeUnit.SECONDS);
        service.scheduleWithFixedDelay(syncLedgerTxnsThread,0L, 5, TimeUnit.SECONDS);
        service.scheduleWithFixedDelay(unconfirmTxnsCheckThread,0L, 120, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(autoSanGatherThread,0L, 120, TimeUnit.MINUTES);*/
    }
    
    /*@Bean
    public RedisTemplate<Object, Object> redisTemplate(){
        return new RedisTemplate<Object, Object>();
    }*/
    
    private CorsConfiguration buildConfig() {  
        CorsConfiguration corsConfiguration = new CorsConfiguration();  
        corsConfiguration.addAllowedOrigin("*");  
        corsConfiguration.addAllowedHeader("*");  
        corsConfiguration.addAllowedMethod("*");  
        return corsConfiguration;  
    }  
      
    /** 
     * 跨域过滤器 
     * @return 
     */  
    @Bean  
    public CorsFilter corsFilter() {  
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);  
    } 
    
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }
    
}


