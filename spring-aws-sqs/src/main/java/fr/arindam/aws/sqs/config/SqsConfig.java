package fr.arindam.aws.sqs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class SqsConfig {

	@Value("${aws.region}")
    private String region;
	
	@Value("${aws.accessKey}")
    private String accessKey;
    
    @Value("${aws.secretKey}")
    private String secretKey;
    
    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
    	return new QueueMessagingTemplate(amazonSQSAsync());
    }
    
    public AmazonSQSAsync amazonSQSAsync() {
    	return AmazonSQSAsyncClientBuilder
    			.standard()
    			.withRegion(region)
    			.withCredentials(
    					new AWSStaticCredentialsProvider(
    							new BasicAWSCredentials(this.accessKey, this.secretKey)))
    			.build();
    }    
}
