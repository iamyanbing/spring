package com.iamyanbing.dao.manager.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zcy
 * @package com.creativearts.common.msg.msgservice.dao.utils
 * @date 2019/07/02 15:03
 */
@Component
public class DataBaseEncryptUtils {

    private static final String FIX_KEY_INDEX = "zy_business_key";

    private static final String SYS_NAME = "msg-dal";



    @Value("${local.database.encrypt.switch:ON}")
    private String encryptSwitch;

    public String decrypt(String decyptStr) {
        try {
            if (!"ON".equalsIgnoreCase(encryptSwitch)){
                return decyptStr;
            }
            if (StringUtils.isBlank(decyptStr)){
                return "";
            }

//            return securityFacade.decrypt(reqDto);//通过DUBBO请求加解密
            return "";
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public String encrypt(String encryptStr){
        try {
            if (!"ON".equalsIgnoreCase(encryptSwitch)){
                return encryptStr;
            }
            if (StringUtils.isBlank(encryptStr)){
                return "";
            }
            return "";
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public List<String> batchEncrypt(List<String> encryptStrs) {
        try {
            if (!"ON".equalsIgnoreCase(encryptSwitch)) {
                return encryptStrs;
            }
            if (encryptStrs == null) {
                return new ArrayList<>();
            }
            if (encryptStrs.size() == 0) {
                return encryptStrs;
            }
            List<String> list=new ArrayList<>();
            return list;
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }
}
