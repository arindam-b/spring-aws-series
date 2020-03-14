package fr.arindam.aws.sqs.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@RestController
@RequestMapping("/sqs")
public class SQSApi {
	
	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Value("${aws.end-point.sqs-url}")
	private String sqsEndPoint;
	
	@Value("${aws.end-point.fifoqueue-url}")
	private String fifoqueueEndPoint;
		
	/**
	 * Send message to SQS Queue
	 * @param message
	 * @return
	 */
	@GetMapping("/sendMessageToSQSQueue/{message}")
	public String sendMessageToSQSQueue(@PathVariable String message) {
		queueMessagingTemplate.send(sqsEndPoint, MessageBuilder.withPayload(message).build());
		return "Message sent successfully";
	}
	
	/**
	 * Send message to FIFO Queue
	 * @param message
	 * @return
	 */
	@GetMapping("/sendMessageToFIFOQueue/{messageGroupId}/{message}")
	public String sendMessageToFIFOQueue(@PathVariable String messageGroupId,
								@PathVariable String message) {
		final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(new SendMessageRequest(fifoqueueEndPoint, message).withMessageGroupId(messageGroupId));
		return "Message sent successfully";
	}
	
	/**
	 * Here is consumer of the message for SQS Queue	
	 * @param message
	 */
	@SqsListener(value = "my-first-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveSQS(String message)
	{
	    System.out.println("my-first-queue has received a new message: " + message);
	}
	
	/**
	 * Here is consumer of the message for FIFO Queue
	 * @param message
	 */
	@SqsListener(value = "my-first-fifoqueue.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveFIFOQueue(String message)
	{
	    System.out.println("my-first-fifoqueue.fifo has received a new message: " + message);
	}
}
