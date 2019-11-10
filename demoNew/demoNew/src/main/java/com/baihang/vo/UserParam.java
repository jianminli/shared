package com.baihang.vo;

import java.io.Serializable;
import java.util.Map;

public class UserParam implements Serializable {

   private String mobile;
   private String certType;
   private String cerNo;
   private String name;
   private String cardNo;

   private Map<String,String> userInfoMap;

   public Map<String, String> getUserInfoMap() {
      return userInfoMap;
   }

   public void setUserInfoMap(Map<String, String> userInfoMap) {
      this.userInfoMap = userInfoMap;
   }

   // TODO
   private Map<String,String> expectedResu;

   public Map<String, String> getExpectedResu() {
      return expectedResu;
   }

   public void setExpectedResu(Map<String, String> expectedResu) {
      this.expectedResu = expectedResu;
   }

   public String getMobile() {
      return mobile;
   }

   public void setMobile(String mobile) {
      this.mobile = mobile;
   }

   public String getCertType() {
      return certType;
   }

   public void setCertType(String certType) {
      this.certType = certType;
   }

   public String getCerNo() {
      return cerNo;
   }

   public void setCerNo(String cerNo) {
      this.cerNo = cerNo;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCardNo() {
      return cardNo;
   }

   public void setCardNo(String cardNo) {
      this.cardNo = cardNo;
   }
}
