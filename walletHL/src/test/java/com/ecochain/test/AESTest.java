package com.ecochain.test;


import com.ecochain.util.AES;
import com.tfcc.core.Wallet;
import org.junit.Test;


public class AESTest {
    
    @Test
    public void testAes(){
        try {
            String value = "FE155D51D5E7D4F77B0743AEAA442C5FE40B45B11C49D1417EC5F8927D47435C";
            System.out.println(AES.decrypt(value));
            Wallet wallet = Wallet.fromSeedString(AES.decrypt(value));
            System.out.println(wallet);
            System.out.println(wallet);
            System.out.println(AES.encrypt("108650470365878924559519356195079918833907710797744364808042769542823359301559".getBytes()));
            /*String aesValue = AES.encrypt(value.getBytes("UTF-8"));
            System.out.println("===" + aesValue);
            String deAesValue = AES.decrypt(aesValue);
            System.out.println(deAesValue);*/
            //Assert.assertEquals(true, value.equals(deAesValue));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
