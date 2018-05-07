package ae.anyorder.bigorder.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AWSConfig {
    private String accessKey="AKIAIUKKL7BK2WE73DHQ";
    private String secretKey="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";
    private String region="ap-southeast-1";

    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
        builder.setRegion(region);
        AmazonS3 amazonS3 = builder.build();
        return amazonS3;
    }
}
