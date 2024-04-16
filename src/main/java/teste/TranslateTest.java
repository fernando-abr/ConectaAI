package teste;

import com.example.translate.BatchTranslation;
import com.example.translate.DescribeTextTranslationJob;
import com.example.translate.ListTextTranslationJobs;
import com.example.translate.TranslateText;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.translate.TranslateClient;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.*;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TranslateTest {
    private static TranslateClient translateClient;
    private static Region region;
    private static String s3Uri = "";
    private static String s3UriOut = "";
    private static String jobName = "";
    private static String dataAccessRoleArn = "";
    private static String jobId = "";

    @BeforeAll
    public static void setUp() throws IOException {
        region = Region.US_WEST_2;
        translateClient = TranslateClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();




    }

    @Test
    @Tag("IntegrationTest")
    @Order(1)
    public void TranslateText() {
        assertDoesNotThrow(() -> TranslateText.textTranslate(translateClient));
        System.out.println("Test 1 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(2)
    public void BatchTranslation() {
        jobId = BatchTranslation.translateDocuments(translateClient, s3Uri, s3UriOut, jobName, dataAccessRoleArn);
        assertFalse(jobId.isEmpty());
        System.out.println("Test 2 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(3)
    public void ListTextTranslationJobs() {
        assertDoesNotThrow(() -> ListTextTranslationJobs.getTranslationJobs(translateClient));
        System.out.println("Test 3 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(4)
    public void DescribeTextTranslationJob() {
        assertDoesNotThrow(() -> DescribeTextTranslationJob.describeTextTranslationJob(translateClient, jobId));
        System.out.println("Test 4 passed");
    }

    private static String getSecretValues() {
        SecretsManagerClient secretClient = SecretsManagerClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        String secretName = "test/translate";

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse valueResponse = secretClient.getSecretValue(valueRequest);
        return valueResponse.secretString();
    }

    @Nested
    @DisplayName("A class used to get test values from test/translate (an AWS Secrets Manager secret)")
    class SecretValues {
        private String s3Uri;
        private String s3UriOut;
        private String jobName;

        private String dataAccessRoleArn;

        public String getS3UriOut() {
            return s3UriOut;
        }

        public String getJobName() {
            return jobName;
        }

        public String getS3Uri() {
            return s3Uri;
        }

        public String getDataAccessRoleArn() {
            return dataAccessRoleArn;
        }
    }
}