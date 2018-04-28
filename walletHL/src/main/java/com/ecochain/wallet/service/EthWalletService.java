package com.ecochain.wallet.service;

import com.ecochain.client.Web3JClient;
import com.ecochain.constant.Constant;
import com.ecochain.core.exception.RuntimeServiceException;
import com.ecochain.task.Ethereum.EtherHotWalletTool;
import com.ecochain.util.AES;
import com.ecochain.util.EtherWalletUtil;
import com.ecochain.util.EtherWalletUtil.EtherWallet;
import com.ecochain.wallet.entity.*;
import com.ecochain.wallet.mapper.EcoWalletMapper;
import com.ecochain.wallet.mapper.UserCoinMapper;
import com.ecochain.wallet.util.WalletConstant;
import com.github.pagehelper.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
/**
 * @author dxm
 * @ClassName: EthWalletService
 * @Description: TODO(以太坊钱包service)
 * @date 2017年8月18日 上午11:03:31
 */
@Service
public class EthWalletService {

    
    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private EcoWalletMapper ecoWalletMapper;
    
    @Autowired
    private UserCoinMapper userCoinMapper;
    
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    
    @Autowired
    private UserWalletService userWalletService;
    
    @Autowired
    private EcoWalletService ecoWalletService;
    
    @Autowired
    private SysGencodeService sysGencodeService;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private EtherHotWalletTool etherHotWalletTool;

//    @Value("${ETH.wallet.hot.address}")
//	private String ethWalletHotAddress;
//
//	@Value("${ETH.wallet.cold.address}")
//	private String ethWalletColdAddress;

	@Value("${ETH.wallet.hot.AmountLimit}")
	private String hotAmountLimit;

	@Value("${ETH.wallet.hot.password}")
	private String password;

    @Value("${ETH.withdrawFee}")
    private String ethWithdrawFee;

    @Value("${OMG.withdrawFee}")
    private String omgWithdrawFee;

    @Value("${OMG.tokenAddress}")
    private String tokenAddress;

    private static String REDIS_ETH_MEMBER_ADDRESS = "ETH_MEMBER_ADDRESS";
    
    private static Web3j web3j = Web3JClient.getClient();
    
    @Autowired
    private UsersDetailsService userDetailsService;
    
    /**
     * 
    * @Title: createEthWallet 
    * @Description: TODO(创建以太坊钱包，并返回用户生成的钱包地址) 
    * @param  userName 用户名
    * @param  passWord 密码
    * @return String    钱包地址
    * @throws 
    * @author Anne
     */
    public  String createEthWallet(String userName, String passWord){
		String address = null;
		SetOperations<String, Object> addressSet = redisTemplate.opsForSet();
		try {
			if(this.checkExistAddress(userName)){
				log.error("userName:{} createEthWallet fail, because userName is exist!");
				return null;
			}
			/*//TODO 配置参数wallertRootPath=C:\\geth\\chain\\keystore
			File destinationDirectory = getKeyStorePath();
			String directory = WalletUtils.generateFullNewWalletFile(
					passWord, destinationDirectory);
			log.info("directory="+directory);
			String source = destinationDirectory.getAbsolutePath()+File.separator+directory;
			log.error("source = "+source);
			Credentials credentials = WalletUtils.loadCredentials(passWord, source);*/
			EtherWallet etherWallet = EtherWalletUtil.createWallet();
			if(etherWallet != null){
				if(WalletUtils.isValidAddress(etherWallet.getAddress())){
					address = etherWallet.getAddress();
					if(StringUtil.isNotEmpty(address)){
						addressSet.add(REDIS_ETH_MEMBER_ADDRESS, address);
						EcoWallet ecoWallet = new EcoWallet();
						ecoWallet.setUserName(userName);
						ecoWallet.setKeyType("2");
						ecoWallet.setWalletAddress(address);
						ecoWallet.setPrivateKey(AES.encrypt(etherWallet.getPrivateKeys().toString().getBytes()));
						ecoWallet.setCreateTime(new Date());
						ecoWallet.setUpdateTime(new Date());
						int ecoInsertCount = ecoWalletMapper.insert(ecoWallet);
						log.info("ecoInsertCount="+ecoInsertCount);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("createEthWallet error:{}", e.getMessage());
		}
		return address;
	}
    
    /**
     *
    * @Title: getAccountKeyByPassword
    * @Description: TODO(根据交易密码和钱包文件地址获取钱包地址以及公钥和私钥信息)
    * @param @param password 交易密码
    * @param @param source 钱包文件地址
    * @return HashMap<String,Object>    返回钱包信息
    * @throws
    * @author Anne
     */
    public static HashMap<String, Object> getAccountKeyByPassword(String password, String source){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String address = null;
		Credentials credentials;
		try {
			credentials = WalletUtils.loadCredentials(password, source);
			if(credentials != null){
				if(WalletUtils.isValidAddress(credentials.getAddress())){
					address = credentials.getAddress();
					map.put("address", address);
				}
				ECKeyPair ecKeyPair = credentials.getEcKeyPair();
				if(ecKeyPair != null){
					map.put("ecKeyPair", ecKeyPair);
				}
			}
		} catch (IOException | CipherException e) {
			e.printStackTrace();
		}
		return map;
	}
    
    /**
     *
    * @Title: getEthWalletBalance
    * @Description: TODO(通过用户ID获取以太坊钱包余额，单位是ETHER)
    * @param  userid 用户ID
    * @return HashMap<String,Object>    返回调用状态以及余额
    * @throws
    * @author Anne
     */
    public HashMap<String, Object> getEthWalletBalance(String userid){
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	String msg = "success";
    	try {
    		String  address = userCoinMapper.getUserCoinByUserid(userid);
    		if(StringUtil.isEmpty(address)){
    			msg = "暂无此用户以太坊钱包地址！";
    			resultMap.put("code", "000");
    			resultMap.put("msg", msg);
    			return resultMap;
    		}
			EthGetBalance ethGetBalance =  web3j.ethGetBalance(address, 
					DefaultBlockParameterName.LATEST).send();
			if(ethGetBalance != null){
				BigDecimal balance = Convert.fromWei(ethGetBalance.getBalance().toString(), 
						Convert.Unit.ETHER);
				msg = "success";
				resultMap.put("balance", balance);
				resultMap.put("code", "999");
    			resultMap.put("msg", msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return resultMap;
    }

    /**
     *
    * @Title: getNonceValue
    * @Description: TODO(为每一个交易获取一个nonce值)
    * @param @param address 转账钱包地址
    * @return BigInteger    返回nonce值，用于标识交易的唯一
    * @throws
    * @author Anne
     */
    public static BigInteger getNonceValue(String address){
		EthGetTransactionCount ethGetTransactionCount = null;
		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(
			         address, DefaultBlockParameterName.LATEST).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	     BigInteger nonce = ethGetTransactionCount.getTransactionCount();
	     return nonce;
	}
    
    /**
     *
    * @Title: getGasPrice
    * @Description: TODO(获取gasPrice值)
    * @return BigInteger    返回gasPrice值
    * @throws
    * @author Anne
     */
    public static BigInteger getGasPrice(){
    	EthGasPrice ethGasPrice = null;
		try {
			ethGasPrice = web3j.ethGasPrice().send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger gasPrice = ethGasPrice.getGasPrice();
		return gasPrice;
    }

    /**
     * 检查该用户是否已存在钱包地址
     *
     * @param userName
     * @return true：存在;false：不存在
     */
    private boolean checkExistAddress(String userName) {
        EcoWalletExample example = new EcoWalletExample();
        example.createCriteria().andUserNameEqualTo(userName).andKeyTypeEqualTo("2");
        List<EcoWallet> wallets = ecoWalletMapper.selectByExample(example);
        if (wallets != null && wallets.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 *
	* @Title: sendTransactionFromWallet
	* @Description: TODO(主要用于公司钱包账户地址向用户地址转账交易，热钱包转向用户钱包)
	* @param @param fromAddress 公司钱包账户地址，比如主账户，热钱包地址，冷钱包地址等
	* @param @param toAddress 转账目标钱包地址
	* @param @param password 交易密码
	* @param @param amount 交易金额
	* @return String    返回交易hash
	* @throws
	* @author Anne
	 */
    public String sendTransactionFromWallet(String fromAddress, String toAddress, String password,
    		String amount){
    	BigInteger nonceValue = getNonceValue(Numeric.prependHexPrefix(fromAddress));
    	EtherWallet etherWallet = this.getHotwallet();
    	if(etherWallet == null){
    		log.error("Hot wallets are not initialized or not saved to the database!");
    		return null;
    	}
    	String publicKey = etherWallet.getPublicKey();
    	String privateKey = etherWallet.getPrivateKeys();
    	ECKeyPair ecKeyPair = new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey));
    	Credentials credentials = Credentials.create(ecKeyPair);
    	String txHash = doSendTransaction(credentials, nonceValue, getGasPrice(),
				Transfer.GAS_LIMIT, toAddress, new BigInteger(amount));
		return txHash;
    }

    /**
     *
    * @Title: sendTransactionFromUser
    * @Description: TODO(主要用于由用户向公司钱包交易，充值)
    * @param @param fromAddress 转账钱包地址
    * @param @param toAddress 转账目标钱包地址
    * @param @param amount 交易金额
    * @return String    返回交易hash
    * @throws
    * @author Anne
     */
	public String sendTransactionFromUser(String fromAddress, String toAddress,String amount){
		UserCoin userCoin =userCoinMapper.getUserCoinByAddress(fromAddress);
		if(userCoin == null || userCoin.getUserId() == null){
			log.error("fromAddress:{} not find userCoin data!", fromAddress);
			return null;
		}
		Map userInfo = userDetailsService.getUserInfoByUserId(userCoin.getUserId());
		if(userInfo == null){
			log.error("userId:{} not find usersDetails data!", userCoin.getUserId());
			return null;
		}
		EcoWalletExample example = new EcoWalletExample();
		example.createCriteria().andUserNameEqualTo((String)userInfo.get("user_name")).andKeyTypeEqualTo("2");
		List<EcoWallet> wallets = ecoWalletMapper.selectByExample(example);
		if(wallets == null){
			log.error("fromAddress:{} userId:{} not find ecoWallet data!", fromAddress, userCoin.getUserId());
			return null;
		}
		EcoWallet ecoWallet = wallets.get(0);
		String privateKey = AES.decrypt(ecoWallet.getPrivateKey());
		
		BigInteger pub = Sign.publicKeyFromPrivate(new BigInteger(privateKey));
        
		ECKeyPair ecKeyPair = new ECKeyPair(new BigInteger(privateKey), pub);
		
		System.out.println("解密后privateKey："+privateKey);
		Credentials credentials = Credentials.create(ecKeyPair);
		String txHash = sendTx(credentials, fromAddress, toAddress, new BigDecimal(amount));
		log.info("txHash = "+txHash);
		return txHash;
	}
    
	/**
	 *
	* @Title: doSendTransaction
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param  credentials 钱包凭证
	* @param  nonce 是一个递增的数字值，用于标识唯一交易
	* @param  gasPrice 每个ETHER兑换多少个GAS
	* @param  gasLimit 执行交易的总花费
	* @param  to 转账目标钱包地址
	* @param  value 交易数量
	* @return String    返回交易hash
	* @throws
	* @author Anne
	 */
    public String doSendTransaction(Credentials credentials, BigInteger nonce,
			BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value){
    	String txHash = null;
		// create our transaction
		RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
				nonce, gasPrice, gasLimit, to, value);
		// sign & send our transaction
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);
		try {
			EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(
					hexValue).sendAsync().get();
			if(ethSendTransaction != null){
				txHash = ethSendTransaction.getTransactionHash();
				log.info("txHash="+txHash);
				// poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)
				EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(
						txHash).sendAsync().get();
				if (ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
					//通过打包对象TransactionReceipt来确认交易是否被广播并确认
					TransactionReceipt transactionReceipt = ethGetTransactionReceipt
							.getTransactionReceipt().get();
					if(transactionReceipt != null){
						txHash = transactionReceipt.getTransactionHash();
					}
				}else{
					// try again, ad infinitum
				}
			}
		} catch (InterruptedException | ExecutionException  e) {
			e.printStackTrace();
		}
		return txHash;
	}
    
    /**
	 * 
	* @Title: sendTx 
	* @Description: TODO(发送交易，用于通过临时钱包向其他钱包转账) 
	* @param credentials 对临时钱包私钥key进行密码文Hex编码
	* @param from fromAddress转出钱包地址
	* @param to toAddress转入钱包地址
	* @param val 交易金额
	* @return boolean    交易成功:返回交易哈希，否则返回空
	* @throws 
	* @author Anne
	 */
	private String sendTx(Credentials credentials, String from, String to, BigDecimal val) {
		try {
		     BigInteger nonce = getNonceValue(from);
			// create our transaction
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
					nonce, getGasPrice(), Transfer.GAS_LIMIT, to, val.toBigInteger());
			log.info("ecKeyPair = " + credentials.getEcKeyPair()
					+ ",privateKey = "
					+ credentials.getEcKeyPair().getPrivateKey());
			// sign & send our transaction
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,
					credentials);
			String hexValue = Numeric.toHexString(signedMessage);
			EthSendTransaction ethSendTransaction = web3j
					.ethSendRawTransaction(hexValue).sendAsync().get();
			if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
				String txHash = ethSendTransaction.getTransactionHash();
				log.info("txHash=" + txHash);
				if (txHash != null) {
					// poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)
					EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(
							txHash).sendAsync().get();
					if (!ethGetTransactionReceipt.hasError() && ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
						//通过打包对象TransactionReceipt来确认交易是否被广播并确认
						TransactionReceipt transactionReceipt = ethGetTransactionReceipt
								.getTransactionReceipt().get();
						if(transactionReceipt != null){
							String txHashVal = transactionReceipt.getTransactionHash();
							String blockHash = transactionReceipt.getBlockHash();
							BigInteger blockNumber = transactionReceipt.getBlockNumber();
							String fromAddress = transactionReceipt.getFrom();
							String toAddress = transactionReceipt.getTo();
							BigInteger txIndex = transactionReceipt.getTransactionIndex();
							BigInteger gasUsed = transactionReceipt.getGasUsed();
							System.out.println(blockHash+","+blockNumber+","+fromAddress+","+toAddress
									+","+txIndex+","+gasUsed);
							log.info("from:" + from + "\tto:" + to + "\tvalue:" + val
									+ "! sendTx success,tx:" + txHashVal);
						}
					}
					return txHash;
				} else {
					log.error("from:" + from + "\tto:" + to + "\tvalue:" + val
							+ "! sendTx failed: txHashVal is null");
				}
			}else{
				log.error("from:" + from + "\tto:" + to + "\tvalue:" + val
						+ "! sendTx failed: ethSendTransaction.hasError(), error: "+ethSendTransaction.getError().getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("from:" + from + "\tto:" + to + "\tvalue:" + val
					+ "! sendTx failed:" + e.toString());
		}
		return null;
	}

	/**
	 * 
	 * @Title: sendToOuterUser
	 * @Description: TODO(发送交易到用户，提币)
	 * @param toAddress
	 *            钱包地址
	 * @param amount
	 *            交易金额
	 * @throws Exception 
	 * @throws DecoderException 
	 * @throws
	 * @author Anne
	 */
	public String sendToOuterUser(String toAddress, BigDecimal amount) throws DecoderException, Exception {
		String txHashVal = null;
		EtherWallet info = this.getHotwallet();
		ECKeyPair ecKeyPair = new ECKeyPair(new BigInteger(info.getPrivateKeys()),
				new BigInteger(info.getPublicKey()));
		Credentials credentials = Credentials.create(ecKeyPair);
		txHashVal = this.sendTx(credentials, info.getAddress(), toAddress, amount);
		log.info("sendTx, hotWallet:{} to userWallet:{} txHashVal:{}",info.getAddress(),
				toAddress, txHashVal);
		return txHashVal;
	}
    
    /**
	 * 获取主账户地址
	 */
    public String getMainAddress(){
    	String fromAddress = null;
    	try {
    		fromAddress = web3j.ethCoinbase().send().getAddress();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return fromAddress;
    }

    @Transactional
    public synchronized void sendMoney(WithdrawRecord record) throws ExecutionException, InterruptedException {

        UserWallet userWallet = userWalletService.getWalletByUserId(record.getUserId());

        if (userWallet == null || !WalletConstant.APPROVE.equals(record.getStatus())) {
            throw new RuntimeServiceException("用户信息异常。");//参数错误
        }
        record.setUpdateTime(new Date());
//        BigDecimal balance = this.getBalanceEther(this.getHotwallet().getAddress());
//
//        if (balance == null || balance.subtract(record.getMoney()).compareTo(new BigDecimal(this.ethWithdrawFee)) < 0) {
//            throw new RuntimeServiceException("提币账户余额不足");
//        }
        String txHash = "";
        BigDecimal transMoney = record.getMoney();//.subtract(new BigDecimal(this.ethWithdrawFee));
        if (this.etherHotWalletTool.isExistForDepositAddr(record.getAddress()) == false) {
            try {
                txHash = this.sendToOuterUser(record.getAddress(), transMoney);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeServiceException("转账失败");
            }
            log.info("coins sent. transaction hash: " + txHash);
            if (StringUtils.isNotEmpty(txHash)) {
                EthGetTransactionReceipt ethGetTransactionReceipt = null;
                try {
                    ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txHash).sendAsync().get();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                if (!ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
                    log.info("is not Present");
                    throw new RuntimeServiceException("账户余额不足");
                } else {
                    record.setTxHash(txHash);
                    record.setStatus(WalletConstant.SEND);
                    BigInteger gasUsed = ethGetTransactionReceipt.getResult().getGasUsed();
                    record.setFree(new BigDecimal(gasUsed));
                }
            }
        } else {
            record.setTxHash("***********************************");
            record.setStatus(WalletConstant.SEND);
        }
        if (StringUtils.isNotEmpty(txHash)) {
        	//更新提现记录
        	try {
        		withdrawRecordService.updateTxHashById(record);
        	} catch (Exception e) {
        		throw e;
        	}
        }
	}

	/**
	 *
	* @Title: getBalanceEther
	* @Description: TODO(根据地址获取以太坊余额，单位是ETHER)
	* @param address 钱包地址
	* @return BigDecimal    返回钱包余额，单位是ETHER
	* @throws
	* @author Anne
	 */
	public BigDecimal getBalanceEther(String address){
        try {
            EthGetBalance ethGetBalance =  web3j.ethGetBalance(address
            		, DefaultBlockParameterName.LATEST).send();
            if(ethGetBalance!=null){
                return Convert.fromWei(new BigDecimal(ethGetBalance.getBalance()), Convert.Unit.ETHER);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

	/**
	 *
	* @Title: getBalanceWei
	* @Description: TODO(根据钱包地址获取以太坊余额，单位是WEI)
	* @param address 钱包地址
	* @return BigDecimal    返回钱包余额，单位是WEI
	* @throws
	* @author Anne
	 */
	public BigDecimal getBalanceWei(String address){
        try {
            EthGetBalance ethGetBalance =  web3j.ethGetBalance(address
            		, DefaultBlockParameterName.LATEST).send();
            if(ethGetBalance!=null){
//                return Convert.toWei(new BigDecimal(ethGetBalance.getBalance()), Convert.Unit.ETHER);
                return new BigDecimal(ethGetBalance.getBalance());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param txHash 交易hash
     * @return Transaction    返回交易详情
     * @throws
     * @Title: getTransactionInfo
     * @Description: TODO(根据交易hash获取交易信息)
     * @author Anne
     */
    public Transaction getTransactionInfo(String txHash) {
        Transaction transaction = null;
        try {
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txHash).send();
            if (ethTransaction == null || ethTransaction.getError() != null) {
                log.error("getTransactionInfo by txHash ethTransaction object is null, "
                        + "errorMassage:{}", ethTransaction.getError().getMessage());
                return null;
            }
            transaction = ethTransaction.getTransaction().get();
            if (transaction == null) {
                log.warn("getTransactionInfo by txHash transaction object is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transaction;
    }


    public synchronized void sendTokenTx(WithdrawRecord record) {
        BigDecimal sendWei = Convert.toWei(record.getMoney(), Convert.Unit.ETHER);
        BigDecimal actualTxFee = sendWei.multiply(new BigDecimal(this.omgWithdrawFee));
        BigDecimal actualWei = sendWei.subtract(actualTxFee);

        //prepare
        Address dstAddress = new Address(record.getAddress());
        BigInteger sendAmountWei = Convert.toWei(actualWei, Convert.Unit.ETHER).toBigInteger();
        Uint256 amount = new Uint256(sendAmountWei);
        Function function = new Function("transfer", Arrays.<Type>asList(dstAddress, amount), Collections.<TypeReference<?>>emptyList());
        String data = FunctionEncoder.encode(function);
        BigDecimal etherzero = BigDecimal.ZERO;

        //issue sendtx
        String privateKey;
        privateKey = this.getHotwallet().getPrivateKeys();
        Credentials credential = EtherWalletUtil.getCredentials(AES.decrypt(privateKey));
        if (this.etherHotWalletTool.isExistForDepositAddr(record.getAddress()) == false) {
            EthSendTransaction transactionResponse = null;
            try {
                transactionResponse = this.sendContractTx(credential, this.tokenAddress, etherzero, Convert.Unit.ETHER, data);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            } catch (TransactionTimeoutException e1) {
                e1.printStackTrace();
            }
            log.info("recordId:" + record.getId() + " sendTx whether success:" + transactionResponse.hasError());
            //check tx isvalid or occure error
            if (!transactionResponse.hasError()) {
                String txHashVal = transactionResponse.getTransactionHash();
                try {
                    record.setTxHash(txHashVal);
                    record.setStatus(WalletConstant.SEND);
                    log.info("Withdraw request token: OOMG amount:" + record.getMoney() + " txHash：" + record.getTxHash() + " for order:" + record.getId() + " success.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("record:{}  Error processing transaction request:{}", record.getId(), transactionResponse.getError().getMessage());
            }
        } else {
            record.setTxHash("***********************************");
            record.setStatus(WalletConstant.SEND);
        }
        try {
            withdrawRecordService.updateTxHashById(record);
        } catch (Exception e) {
            throw e;
        }

    }

    public EthSendTransaction sendContractTx(Credentials credentials, String toAddress, BigDecimal value,
                                             Convert.Unit unit, String data) throws InterruptedException, ExecutionException,
            TransactionTimeoutException {
        RawTransactionManager transactionManager = new RawTransactionManager(
                web3j, credentials);

        BigDecimal weiValue = Convert.toWei(value, unit);

        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: " + value + " "
                            + unit.toString() + " = " + weiValue + " Wei");
        } else {

            Transfer transfer = new Transfer(web3j, transactionManager);
            BigInteger gasPrice = transfer.GAS_PRICE;
            EthSendTransaction ethSendTransaction = null;
            try {
                ethSendTransaction = transactionManager.sendTransaction(gasPrice, Transfer.GAS_LIMIT, toAddress, data, weiValue.toBigIntegerExact());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ethSendTransaction;
        }
    }
    
    /**
	 * 
	* @Title: getHotwallet 
	* @Description: TODO(获取热钱包信息)
	* @return EtherWallet    返回热钱包 
	* @throws 
	* @author Anne
	 */
	public EtherWallet getHotwallet(){
		EtherWallet hotWallet = new EtherWallet();
		List<Map<String, String>> list = sysGencodeService.findByGroupCode(
				Constant.HOT_WALLET_CODE_GROUP);
		if(list == null || list.size() == 0){
			log.error("hot wallet uninitialized!");
			return null;
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		for(Map<String, String> map : list){
			paramMap.put(map.get("code_name"), map.get("code_value"));
		}
		if(paramMap.size()>0){
			String address = paramMap.get("hotWaletAddress");
			String privateKey = paramMap.get("hotWaletPrivateKey");
			String publicKey = paramMap.get("hotWaletPublicKey");
			hotWallet.setAddress(address);
			hotWallet.setPrivateKeys(privateKey);;
			hotWallet.setPublicKey(publicKey);
		}
		return hotWallet;
	}
	
	
	public boolean isValidAddress(String address){
		return EtherWalletUtil.isValidAddress(address);
	}
}
