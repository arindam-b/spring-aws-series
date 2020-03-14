package fr.arindam.aws.s3.api;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@RestController
@RequestMapping("/storage")
public class S3BucketController {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.bucketName}")
	private String bucketName;
	
	/**
	 * This will not retrieve the file, because if file size is big, recommened practice is presigned url
	 * @param fileName
	 * @return
	 */
	@GetMapping("/getFileInfo/{fileName}")
	public S3ObjectInputStream getFileInfo(@PathVariable String fileName) {
		S3Object s3Object;
        try {
            s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, fileName));                      
        }catch (AmazonServiceException serviceException) {
            throw new RuntimeException("Error while streaming File.");
        } catch (AmazonClientException exception) {
            throw new RuntimeException("Error while streaming File.");
        }
        return s3Object.getObjectContent();
	}

	/**
	 * This method will not only upload file but create folder also, if you pass the argument
	 * @param fileStream - File to be uploaded here
	 * @param folderName - Name of the folder if any to be mentioned also
	 * @return
	 */
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("upload") MultipartFile fileStream, 
				@RequestParam (required=false) String folderName) {

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(fileStream.getContentType());
		objectMetadata.setContentLength(fileStream.getSize());

		try {
			this.amazonS3.putObject(bucketName, 
					getFullObjectKey(fileStream.getOriginalFilename(), folderName), 
					fileStream.getInputStream(), 
					objectMetadata);
			
		} catch(AmazonClientException | IOException exception) {
			throw new RuntimeException("Error while uploading file.");
		}

		return this.amazonS3.getUrl(bucketName, fileStream.getOriginalFilename()).toString();	
	}

	@DeleteMapping("/deleteFile/{fileName}")
	public void deleteFile(@PathVariable String fileName, 
						   @RequestParam (required=false) String folderName) {
		
		this.amazonS3.deleteObject(bucketName, getFullObjectKey(fileName, folderName));
		
	}
		
	/**
	 * This method generates presigned url of the object stored in s3
	 * @param fileName - Mandatory parameter, file name in S3
	 * @param folderName - Optional parameter, foldername in S3
	 * @param expirationLimit - Optional parameter, if not mentioned the presigned url will be valid for 1 hour only
	 * 		  value should be in minutes
	 * @return
	 */
	@GetMapping("/getPresignedUrl/{fileName}")
	public String generatePresignedUrl(@PathVariable String fileName,
									   @RequestParam (required=false) String folderName,	
									   @RequestParam (required=false, defaultValue = "0") int expirationLimit) {
		
		
		
		// Set the presigned URL to expire after one hour.
        Date expiration = new Date();
        
        long expTimeMillis = expiration.getTime();
        if (expirationLimit == 0)
        	expTimeMillis += 1000 * 60 * 60;
        else
        	expTimeMillis += 1000 * 60 * expirationLimit;
        
        expiration.setTime(expTimeMillis);
        
        // Generate the presigned URL.
        
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, getFullObjectKey(fileName, folderName))
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        
        URL url = this.amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        System.out.println("Pre-Signed URL: " + url.toString()+" valid for "+expTimeMillis+" Mili Seconds");
		
        return url.toString();
	}

	
	private String getFullObjectKey(String fileName, String folderName) {
		if(Strings.isBlank(folderName))
			return fileName;
		else	
			return folderName+"/"+fileName;
	}
}
