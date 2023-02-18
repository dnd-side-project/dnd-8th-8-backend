package com.dnd.wedding.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

  @Value("${aws.s3.access-key}")
  private String s3AccessKey;

  @Value("${aws.s3.secret-key}")
  private String s3SecretKey;

  @Value("${aws.s3.region}")
  private String region;

  @Bean
  public AmazonS3 amazonS3() {
    AWSCredentials s3Credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(s3Credentials))
        .build();
  }
}
