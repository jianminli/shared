package com.baihang.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Map;

public class RequestParameter {
      private String secretKey;
      private Head head;
      private Request request;

      public String getSecretKey() {
            return secretKey;
      }

      public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
      }

      public Head getHead() {
            return head;
      }

      public void setHead(Head head) {
            this.head = head;
      }

      public Request getRequest() {
            return request;
      }

      public void setRequest(Request request) {
            this.request = request;
      }

      @Override
      public String toString() {
            return "{" + head + "," + request + "}";
      }

      public String toEncryptString(String keyType) throws Exception {
            return "{" + head + "," + request.toEncryptString(keyType) + "}";
      }

      public class Head {
            private String requestRefId;
            private String secretId;
            private String signature;

            public Head(String requestRefId, String secretId, String signature) {
                  this.requestRefId = requestRefId;
                  this.secretId = secretId;
                  this.signature = signature;
            }

            public String getRequestRefId() {
                  return requestRefId;
            }

            public void setRequestRefId(String requestRefId) {
                  this.requestRefId = requestRefId;
            }

            public String getSecretId() {
                  return secretId;
            }

            public void setSecretId(String secretId) {
                  this.secretId = secretId;
            }

            public String getSignature() {
                  return signature;
            }

            public void setSignature(String signature) {
                  this.signature = signature;
            }

            @Override
            public String toString() {
                  return "\"head\":{" +
                          "\"requestRefId\":\"" + requestRefId + "\"" +
                          ", \"secretId\":\"" + secretId + "\"" +
                          ", \"signature\":\"" + signature + "\"" +
                          "}";
            }
      }

      public class Request {
            private Map<String, String> param;

            public Request(Map<String, String> param) {
                  this.param = param;
            }

            public String toEncryptString(String keyType) throws Exception {
                  String raw = this.getMapString();
                  byte[] b = null;

                  if (keyType.toLowerCase().equals("3des") || keyType.toLowerCase().equals("desede")) {

                        byte[] keyBytes = new BASE64Decoder().decodeBuffer(secretKey);

                        DESedeKeySpec dks = new DESedeKeySpec(keyBytes);
                        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                        SecretKey secretKey = keyFactory.generateSecret(dks);

                        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        b = cipher.doFinal(raw.getBytes("UTF-8"));
                  }

                  return "\"request\":\"" + new BASE64Encoder().encode(b) + "\"";
            }

            public Map<String, String> getParam() {
                  return param;
            }

            public void setParam(Map<String, String> param) {
                  this.param = param;
            }

            @Override
            public String toString() {
                  return "\"request\":" + this.getMapString();
            }

            private String getMapString() {
                  StringBuffer sb = new StringBuffer("{\"param\":{");
                  for (String key : param.keySet()) {
                        sb.append("\"").append(key).append("\":\"").append(param.get(key)).append("\",");
                  }
                  return sb.substring(0, sb.length() - 1) + "}}";
            }
      }

}
