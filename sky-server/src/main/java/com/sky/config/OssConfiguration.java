//package com.sky.config;
//
//import com.aliyun.oss.internal.OSSUtils;
//import com.sky.properties.AliOssProperties;
//import com.sky.utils.AliOssUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//@Configuration
//@Slf4j
//public class OssConfiguration {
//
//    @Autowired
//    private AliOssProperties properties;
//
//
//    @Bean
//    @ConditionalOnMissingBean
//    public AliOssUtil aliOssUtil(AliOssProperties properties){
//        log.info("properties {}", properties);
//        return new AliOssUtil(properties.getEndpoint(),
//                properties.getAccessKeyId(),
//                properties.getAccessKeySecret(),
//                properties.getBucketName());
//
//    }
//}
