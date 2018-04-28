package com.ecochain.wallet.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.EcoWalletApplication;
import com.ecochain.core.config.CoreMessageSource;
import com.ecochain.core.exception.RuntimeServiceException;
import com.ecochain.util.AES;
import com.ecochain.wallet.entity.EcoWallet;
import com.ecochain.wallet.entity.EcoWalletExample;
import com.ecochain.wallet.entity.UserWallet;
import com.ecochain.wallet.entity.WithdrawRecord;
import com.ecochain.wallet.mapper.EcoWalletMapper;
import com.ecochain.wallet.mapper.EcoWalletRecordMapper;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.util.WalletConstant;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

@Service
public class EcoWalletService {
    
    Logger log = LoggerFactory.getLogger(getClass());
    
    @Value("${wallet.rootPath}")
    private String wallertRootPath;
    
    @Autowired
    private CoreMessageSource messageSource;
    
    @Autowired
    private EcoWalletMapper mapper;
    
    @Autowired
    private EcoWalletRecordMapper recordMapper;
    
    @Autowired
    WithdrawRecordService withdrawRecordService;
    
    @Autowired
    UserWalletService userWalletService;
    
    @Autowired
    UserCoinMapper userCoinMapper;
    
    public EcoWallet findEcoWalletByUserName(String userName){
        EcoWalletExample example = new EcoWalletExample();
        example.createCriteria().andUserNameEqualTo(userName);
        List<EcoWallet> wallets = mapper.selectByExample(example );
        
        if (wallets != null && wallets.size() == 1){
            return wallets.get(0);
        }else{
            throw new RuntimeServiceException(messageSource.getMessage("cannotFindUser"));
        }
        
    }

    public EcoWallet findEcoWalletByUserId(Integer userId){
        EcoWalletExample example = new EcoWalletExample();
        example.createCriteria().andIdEqualTo(userId);
        List<EcoWallet> wallets = mapper.selectByExample(example );

        if (wallets != null && wallets.size() == 1){
            return wallets.get(0);
        }else{
            throw new RuntimeServiceException(messageSource.getMessage("cannotFindUser"));
        }

    }
    
    /**
     * 获取所有钱包地址
     * @return
     */
    public List<EcoWallet> listAllEcoWallets(){
        EcoWalletExample example = new EcoWalletExample();
        example.createCriteria().andKeyTypeEqualTo("1");
        return mapper.selectByExample(example);
    }
    
    /**
     * 检查该用户是否已存在钱包地址
     * @param userName
     */
    private void checkExistAddress(String userName){
        EcoWalletExample example = new EcoWalletExample();
        example.createCriteria().andUserNameEqualTo(userName).andKeyTypeEqualTo("1");
        List<EcoWallet> wallets = mapper.selectByExample(example );
        
        if (wallets != null && wallets.size() > 0) {
            throw new RuntimeServiceException(messageSource.getMessage("existAddress", wallets.get(0).getWalletAddress()));
        }
        
    }

    /**
     * 检查该用户是否已存在钱包地址
     * @param address
     */
    public EcoWallet findWalletByAddress(String address){
        EcoWalletExample example = new EcoWalletExample();
        example.createCriteria().andWalletAddressEqualTo(address);
        List<EcoWallet> wallets = mapper.selectByExample(example );

        if (wallets != null && wallets.size() > 0) {
            return wallets.get(0);
        }
        return null;

    }
    
    /**
     * 创建钱包地址
     * @param params params // First we configure the network we want to use.
        // The available options are:
        <p> - MainNetParams
        <p> - TestNet3Params
        <p> - RegTestParams
     * @param userName 用户名
     * @param passWord 钱包的支付密码
     * @return 钱包的地址
     */
    public String createWallet(NetworkParameters params, String userName, String passWord){
        
        checkExistAddress(userName);
        
        ECKey key = new ECKey();
        
        Address address = key.toAddress(params);
        log.info("address = " + address.toString() + ",key=" + key.getPrivateKeyEncoded(params));
        
        EcoWallet ecoWallet = new EcoWallet();
        ecoWallet.setCreateTime(new Date());
        ecoWallet.setPassWord(passWord);
        ecoWallet.setUserName(userName);
        ecoWallet.setWalletAddress(address.toBase58());
        ecoWallet.setPrivateKey(AES.encrypt(key.getPrivateKeyEncoded(params).toString().getBytes()));
        EcoWalletApplication.kit.wallet().importKey(key);
        
        mapper.insertSelective(ecoWallet);
        
        return address.toBase58();
    }
    
    /**
     * 查看钱包所有信息
     * @param userName 
     * @param password 
     * @return
     */
    public Wallet dumpWallet(String userName, String password){
        
        log.info("dumpWallet: userName = " + userName + ", address = " + password);
        return EcoWalletApplication.kit.wallet();
    }
    
    /**
     * 提现
     * 在params网络里，用户 userName 发送coin钱给receiverAddress
     * @param params
     * @param userId 用户ID
     * @param id 记录ID
     * @param coin
     * @param receiverAddress
     */
    @Transactional
    public synchronized void  sendMoney(NetworkParameters params, WithdrawRecord record){

        /**
         * 原来的判断逻辑复制到这里。原来的逻辑改为提币审核操作。
         * 
         * 1、通过用户名取出用户id。
         * 通过用户id取user_wallet里的数据
         * 判断用户钱包里的钱是否够用，等等一系列操作。
         * 扣除费用。
         * 
         */
        
        UserWallet userWallet = userWalletService.getWalletByUserId(record.getUserId());
        
        if (userWallet == null || !WalletConstant.APPROVE.equals(record.getStatus())){
            throw new RuntimeServiceException("用户信息异常。");//参数错误
        }
        //提现申请时，已做过判断，并且扣除了用户的比特币，所以这里不能再做判断   （by add zhangchunming）  
        /*if(record.getMoney().compareTo(userWallet.getBtcAmnt()) > 0){//余额不足
            throw new RuntimeServiceException(messageSource.getMessage("balanceError"));
        }*/
        
        WalletAppKit kit = EcoWalletApplication.kit;

        log.info("Send money to: " + kit.wallet().currentReceiveAddress().toString());

        Address to = Address.fromBase58(params, record.getAddress());

        Coin coin = Coin.parseCoin(record.getMoney().toPlainString());
        
        try {
            record.setUpdateTime(new Date());
            
            String userId = userCoinMapper.getUserIdByAddress(record.getAddress());
            
            //如果是平台内部转账。
            if (StringUtils.isNotBlank(userId)){
                
                record.setTxHash(UUID.randomUUID().toString());
                record.setStatus(WalletConstant.FINISH);
                record.setFree(BigDecimal.ZERO);
                
                //提现记录
                withdrawRecordService.updateTxHashById(record);
                //给另一个用户加钱
                userWalletService.addBTCWallet(new BigDecimal(coin.toPlainString()), userId);
                
                return;
            }
            
            
//            SendRequest req = SendRequest.to(to, coin);
//            req.feePerKb = Coin.parseCoin("0.0005");
//            Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), req);
//            Transaction createdTx = result.tx;
            
            Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), to, coin);
            log.info("coins sent. transaction hash: " + result.tx.getHashAsString());
            
            record.setTxHash(result.tx.getHashAsString());
            record.setStatus(WalletConstant.SEND);
            record.setFree(new BigDecimal(result.tx.getFee().toPlainString()));
            
            //更新提现记录
            withdrawRecordService.updateTxHashById(record);
            
            // you can use a block explorer like https://www.biteasy.com/ to inspect the transaction with the printed transaction hash. 
        } catch (InsufficientMoneyException e) {
            log.info("Not enough coins in your wallet. Missing " + e.missing.getValue() + " satoshis are missing (including fees)");
            log.info("Send money to: " + kit.wallet().currentReceiveAddress().toString());

            // Bitcoinj allows you to define a BalanceFuture to execute a callback once your wallet has a certain balance.
            // Here we wait until the we have enough balance and display a notice.
            // Bitcoinj is using the ListenableFutures of the Guava library. Have a look here for more information: https://github.com/google/guava/wiki/ListenableFutureExplained
            ListenableFuture<Coin> balanceFuture = kit.wallet().getBalanceFuture(coin, BalanceType.AVAILABLE);
            FutureCallback<Coin> callback = new FutureCallback<Coin>() {
                @Override
                public void onSuccess(Coin balance) {
                    log.info("coins arrived and the wallet now has enough balance");
                }

                @Override
                public void onFailure(Throwable t) {
                    log.info("something went wrong");
                }
            };
            Futures.addCallback(balanceFuture, callback);
        }

        // shutting down 
//        kit.stopAsync();
//        kit.awaitTerminated();
    
    }
    
    
    
}
