package com.baihang.service;

import com.baihang.util.MyHttpsClient;
import com.baihang.util.RequestParameter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class ReportService {
    private static Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Value("${openapi.secretId}")
    private String secretId;

    @Value("${openapi.secretKey}")
    private String secretKey;

    public final static String HTTPS = "https";

    public String makePostCall(RequestParameter requestParameter, String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        String queryString = requestParameter.toString();
        queryString = requestParameter.toEncryptString("3DES");
        StringEntity entity = new StringEntity(queryString, "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 支持https请求  开始
        CloseableHttpClient httpClient = null;
        if(!StringUtils.isEmpty(url)&&url.startsWith(HTTPS)){
            httpClient = httpClient = MyHttpsClient.createSSLInsecureClient();
        }else{
            httpClient = HttpClients.createDefault();
        }
        CloseableHttpResponse resp = httpClient.execute(httpPost);

        return EntityUtils.toString(resp.getEntity(), "utf-8");
    }

    public String decryptResponse(String response, String keyType, String key) throws Exception {

        if (keyType.toLowerCase().equals("3des") || keyType.toLowerCase().equals("desede")) {
            DESedeKeySpec dks = new DESedeKeySpec(new BASE64Decoder().decodeBuffer(key));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] b = cipher.doFinal(new BASE64Decoder().decodeBuffer(response));
            String param = new String(b, "utf-8");

            return param;
        }
        return null;
    }

    private String invokeService(RequestParameter requestParameter, ReportService reportService, String uri) throws Exception {
        logger.info("<<<请求：" + requestParameter.toString() + ">>>");
        String rawString = reportService.makePostCall(requestParameter, uri);
        logger.info("<<<返回：" + rawString + ">>>");
        return rawString;
    }

    /**
     * 构建请求参数对象
     *
     * @param paraMap
     * @param productCode
     * @return
     * @throws Exception
     */
    public RequestParameter buildReqParam(Map<String, String> paraMap, String productCode) throws Exception {
        RequestParameter requestParameter = new RequestParameter();
        String requestRefId =
                "CMBREQ_" + new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + (int) ((Math.random() * 9 + 1) * 100000); // 用户查询请求标示，要求每次（接入方自设）
        String beforeSign = "requestRefId=" + requestRefId + "&secretId=" + secretId;
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(secretKey);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(beforeSign.getBytes());
        String signature = new BASE64Encoder().encode(rawHmac);
        RequestParameter.Head head = requestParameter.new Head(requestRefId, secretId, signature);
        RequestParameter.Request request = requestParameter.new Request(paraMap);
        requestParameter.setSecretKey(secretKey);
        requestParameter.setHead(head);
        requestParameter.setRequest(request);
        return requestParameter;
    }

    /**
     * 签名
     * @param requestRefId
     * @param secretId
     * @param secretKey
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public  String  makeSign(String requestRefId,String secretId,String secretKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(secretKey);
        String beforeSign = "requestRefId=" + requestRefId + "&secretId=" + secretId;
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(beforeSign.getBytes());
        String localSignature = new BASE64Encoder().encode(rawHmac);
        return localSignature;

    }

}
