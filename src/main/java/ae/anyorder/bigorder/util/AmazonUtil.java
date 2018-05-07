package ae.anyorder.bigorder.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Frank on 4/5/2018.
 */
@Scope(value = "singleton")
@Component
public class AmazonUtil {
    public static final Logger log = Logger.getLogger(AmazonUtil.class);

    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    MessageBundle messageBundle;

    private final String S3_ENDPOINT = "s3.amazonaws.com";
    private final String BUCKET = System.getProperty("S3_BUCKET");
    private final String CLOUD_FRONT = System.getProperty("CLOUD_FRONT");

    private PutObjectResult upload(String filePath, String uploadKey) throws FileNotFoundException {
        return upload(new FileInputStream(filePath), uploadKey);
    }

    private PutObjectResult upload(InputStream inputStream, String uploadKey) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, uploadKey, inputStream, new ObjectMetadata());

        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

        IOUtils.closeQuietly(inputStream);

        return putObjectResult;
    }
    private AWSCredentials credentials;

    public AWSCredentials getCredentials() {
        if (credentials == null) {
            String accessKey = messageBundle.getMessage("accessKey", "AwsCredentials.properties");
            String secretKey = messageBundle.getMessage("secretKey", "AwsCredentials.properties");
            credentials = new BasicAWSCredentials(accessKey, secretKey);
        }
        return credentials;
    }

    public void testUpload(File file, String path, String filename, boolean deleteOriginal){
        ///AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        String key = filename;
        if (path != null && !path.equals(""))
            key = path + filename;
        amazonS3.putObject(new PutObjectRequest(
                BUCKET, key, file));
    }

    public String uploadFileToS3(File file, String path, String filename, String oldFileName) throws InterruptedException {
        log.info("Uploading file " + filename + " to bucket... ");
        String key = filename;
        String oldkey = oldFileName;
        if (path != null && !path.equals("")) {
            key = path + filename;
            oldkey = path + oldFileName;
        }
        if(oldFileName!=null)
            deleteFileFromBucket(oldkey);
        amazonS3.putObject(new PutObjectRequest(BUCKET, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
        amazonS3.setObjectAcl(BUCKET, key, CannedAccessControlList.PublicRead);
        return BUCKET + "." + S3_ENDPOINT + "/" + key;
    }

    public String cacheImage(String url) {
        String bucketUrl = BUCKET + "." + S3_ENDPOINT;
        //return url.replaceAll(bucketUrl,MessageBundle.getPushMessage("cloud_front", "constants.properties"));
        return url.replaceAll(bucketUrl, CLOUD_FRONT);

    }

    public void deleteFileFromBucket(String key) throws AmazonClientException {
        log.info("Deleting " + key + " from S3 Bucket");
        //Key containing directory/directory/file
        amazonS3.deleteObject(BUCKET, key);
    }

    public void deleteObjectsInFolder(String folderPath) {
        if (folderPath == null || folderPath.equals(""))
            throw new RuntimeException("BUCKET FolderPath cannot be empty");

        for (S3ObjectSummary file : amazonS3.listObjects(BUCKET, folderPath).getObjectSummaries()) {
            amazonS3.deleteObject(BUCKET, file.getKey());
        }

    }

    public String getAmazonS3Key(String fullPath) {
        // URL May (http://example.com/Cat_Icons/hello.png) ||  example.com/Cat_Icons/hello.png
        //Checking both condition url
        if (fullPath.contains("://")) {
            fullPath = fullPath.substring(fullPath.indexOf('/', 7) + 1);
            //url = url.substring(url.indexOf("/") + 1);
        } else {
            fullPath = fullPath.substring(fullPath.indexOf("/") + 1);
        }
        return fullPath;
    }

}
