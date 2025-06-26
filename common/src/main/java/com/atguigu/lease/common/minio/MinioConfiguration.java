package com.atguigu.lease.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kdz
 * @create 2025-06-25-18:23
 */

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {
//    @Value("${minio.endpoint}")
//    private String endpoint;

    @Autowired
    private MinioProperties minioProperties;
    @Bean
   public MinioClient minioClient(){

        return MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey()).build();
   }
}
