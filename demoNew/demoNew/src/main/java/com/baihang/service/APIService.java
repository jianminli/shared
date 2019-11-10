package com.baihang.service;


import com.baihang.util.RequestParameter;
import com.baihang.vo.UserParam;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class APIService {

    private static  Logger logger = LoggerFactory.getLogger(APIService.class);
    @Value("${openapi.secretId}")
    private String secretId;

    @Value("${openapi.secretKey}")
    private String secretKey;

    @Value("${openapi.restURL}")
    private String restURL;

    @Value("${openapi.responseCode}")
    private String responseCode;

    @Value("${openapi.response}")
    private String openapiResponse;

    @Value("${openapi.responseMsg}")
    private String responseMsg;


    private static final String SUCC_RESP_CODE ="0000";

     @Autowired
    private ReportService reportService;

    public String queryData(Map<String, String> param, String productCode) throws Exception {

        RequestParameter requestParameter= reportService.buildReqParam(param,productCode);
        logger.info("<<<请求：" + requestParameter.toString() + ">>>");
        String rawString = reportService.makePostCall(requestParameter, restURL+productCode);
        logger.info("<<<返回：" + rawString + ">>>");
        String data = null;
        String respCode = JsonPath.read(rawString, responseCode);
        String respMsg = JsonPath.read(rawString, responseMsg);
        if (respCode.equals(SUCC_RESP_CODE)) {
            String responseStr = JsonPath.read(rawString, openapiResponse);
            data = reportService.decryptResponse(responseStr, "3des", secretKey);
        }
        StringBuffer rs = new StringBuffer(respCode).append(", ").append(respMsg).append(", ").append(data);
        return rs.toString();

    }

}
