package com.fh.util;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 科学计数法
 */
public class ScientificNotation {
    /**********************************************/
    public static final BigDecimal maxBigDecimal = new BigDecimal("100000000000000");
    public static final BigDecimal minBigDecimal = new BigDecimal("-100000000000000");
    /**********************************************/
    public static final int MAX_LENGTH_TXT = 80;
    public static final BigDecimal ZERO = new BigDecimal("0");
    public static final BigDecimal ONE = new BigDecimal("1");
    public static final BigDecimal _ONE = new BigDecimal("-1");
    public static final BigDecimal TEN = new BigDecimal("10");
    public static final BigDecimal _TEN = new BigDecimal("-10");
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    public static final BigDecimal _ONE_HUNDRED = new BigDecimal("-100");
    public static final BigDecimal ONE_THOUSAND = new BigDecimal("1000");
    public static final BigDecimal _ONE_THOUSAND = new BigDecimal("-1000");
    public static final BigDecimal TEN_THOUSAND = new BigDecimal("10000");
    public static final BigDecimal _TEN_THOUSAND = new BigDecimal("-10000");
    public static final BigDecimal ONE_HUNDRED_MILLION = new BigDecimal("100000000");
    public static final BigDecimal _ONE_HUNDRED_MILLION = new BigDecimal("-100000000");
    public static final BigDecimal MAX_VALUE = maxBigDecimal.divide(ONE_HUNDRED, 4);
    public static final BigDecimal MIN_VALUE = minBigDecimal.divide(ONE_HUNDRED, 4);
    /**********************************************/
    public static final BigDecimal MAX_TOTAL_VALUE = MAX_VALUE.multiply(ONE_HUNDRED);
    public static final BigDecimal MIN_TOTAL_VALUE = MIN_VALUE.multiply(ONE_HUNDRED);
    public static final BigDecimal MAX_TOTAL_VALUE2 = MAX_VALUE.multiply(ONE_THOUSAND);
    public static final BigDecimal MIN_TOTAL_VALUE2 = MIN_VALUE.multiply(ONE_THOUSAND);
    public static final BigDecimal MAX_DECIMAL = new BigDecimal("999999999999999.9999");
    public static final BigDecimal MIN_DECIMAL = new BigDecimal("-999999999999999.9999");
 /**
   * 判断字符串输入是否正确
   * amountText 要判断的字符串
   */
 public static boolean isCorrect(String amountText){
  if(amountText==null||"".equals(amountText))
   return false;
  String regxa = "[+-]?[0-9]+.[0-9]+[Ee][+-]?\\d+";
       Pattern patterna = Pattern.compile(regxa);
       Matcher matchera = patterna.matcher(amountText);
       
       String regxb = "[+-]?[0-9]+[Ee][+-]?\\d+";
       Pattern patternb = Pattern.compile(regxb);
       Matcher matcherb = patternb.matcher(amountText);
       
       String regx1 = "[+-]?[0-9]+.[0-9]?\\d+";
       Pattern pattern1 = Pattern.compile(regx1);
       Matcher matcher1 = pattern1.matcher(amountText);
       
       String regx2 = "[+-]?[0-9]?\\d+";
       Pattern pattern2 = Pattern.compile(regx2);
       Matcher matcher2 = pattern2.matcher(amountText);
       
       if(matchera.matches()||matcherb.matches()||matcher1.matches()||matcher2.matches()){
        return true;
       }else{
        return false;
       }
      
 }
 
 
 /**
  *根据科学计数法得到数字 
  *amountText 科学计数法字符串
  *scale  精度（小数点后位数）
  */
 public static BigDecimal getBDAmount(String amountText,int scale){
  BigDecimal amount = BigDecimal.ZERO;
  if(isCorrect(amountText)){
        BigDecimal amounta = new BigDecimal(amountText);
        amount = toBigDecimal(amounta, scale);
  }
  return amount;
 }
 
 
 /**
  *根据数字或者科学计数法的到科学计数法 
  *amountText 数字或者科学计数法符串
  *scale   精度
  */
 public static String getScientific(String amountText,int scale){
  String amount = "";
  if(isCorrect(amountText)){
        BigDecimal amounta = new BigDecimal(amountText);
        if(amounta.compareTo(BigDecimal.ZERO)!=0){
         amount = String.format("%1$1."+scale+"E",amounta);
        }
  }
  return amount;
 }
 
 /**
  *根据科学计数法得到数字（默认4为精度） 
  *amountText 科学计数法字符串
  */
 public static BigDecimal getBDAmount(String amountText){
  BigDecimal amount = BigDecimal.ZERO;
  if(isCorrect(amountText)){
        BigDecimal amounta = new BigDecimal(amountText);
        amount = toBigDecimal(amounta, 4);
  }
  return amount;
 }
 
 /**
  *根据数字或者科学计数法的到科学计数法（默认4为精度）
  *amountText 数字或者科学计数法符串
  */
 public static String getScientific(String amountText){
  String amount = "";
  if(isCorrect(amountText)){
        BigDecimal amounta = new BigDecimal(amountText);
        if(amounta.compareTo(BigDecimal.ZERO)!=0){
         amount = String.format("%1$1.4E",amounta);
        }
  }
  return amount;
 }
    public static BigDecimal toBigDecimal(Object obj, int scale){
        return toBigDecimal(obj).setScale(scale, 4);
    }
    public static BigDecimal toBigDecimal(Object obj){
        if(obj == null)
            return ZERO;
        if(obj instanceof BigDecimal)
            return (BigDecimal)obj;
        if(obj instanceof Integer)
            return new BigDecimal(((Integer)obj).toString());
        if(obj instanceof Long)
            return new BigDecimal(((Long)obj).toString());
        if(obj instanceof Double)
            return new BigDecimal(((Double)obj).doubleValue());
        if(obj.toString() == null)
            return ZERO;
        String str = obj.toString().trim();
        if(str.toLowerCase().indexOf("e") > -1)
            try
            {
                return new BigDecimal(str);
            }
            catch(NumberFormatException e)
            {
                return ZERO;
            }
        if(str.matches("^[+-]?\\d+[\\.\\d]?\\d*+$"))
            return new BigDecimal(str);
        else
            return ZERO;
    }
 public static void main(String[] args) {
  //判断字符串输入是否正确
  System.out.println(isCorrect("10.99999E+01"));
  //根据科学计数法得到数字 
  System.out.println(getBDAmount("0E-7",8));
  //根据数字或者科学计数法的到科学计数法 
  System.out.println(getScientific("4654644494794984",2));
  
 }
}