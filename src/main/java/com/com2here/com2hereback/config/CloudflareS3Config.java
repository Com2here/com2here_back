package com.com2here.com2hereback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class CloudflareS3Config {
    @Value("${cloudflare.account.id}")
    private String accountId;

    @Value("${cloudflare.access.key}")
    private String accessKey;

    @Value("${cloudflare.secret.key}")
    private String secretKey;

    @Value("${cloudflare.endpoint}")
    private String endpoint;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .endpointOverride(URI.create(endpoint))
            .build();
    }
}
