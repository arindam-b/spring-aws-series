package fr.arindam.aws.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;
    
    @Value("${aws.bucketName}")
    private String bucketName;
    
    @Value("${aws.accessKey}")
    private String accessKey;
    
    @Value("${aws.secretKey}")
    private String secretKey;
    
    @Bean
    public AmazonS3 awsS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        
        return AmazonS3ClientBuilder
        		.standard()
        		.withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
