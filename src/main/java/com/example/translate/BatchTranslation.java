package com.example.translate;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.StartTextTranslationJobRequest;
import software.amazon.awssdk.services.translate.model.InputDataConfig;
import software.amazon.awssdk.services.translate.model.OutputDataConfig;
import software.amazon.awssdk.services.translate.model.StartTextTranslationJobResponse;
import software.amazon.awssdk.services.translate.model.DescribeTextTranslationJobRequest;
import software.amazon.awssdk.services.translate.model.DescribeTextTranslationJobResponse;
import software.amazon.awssdk.services.translate.model.TranslateException;

public class BatchTranslation {
    public static long sleepTime = 5;

    public static void main(String[] args) {
        final String usage = """

                Usage:
                    <s3Uri> <s3UriOut> <jobName> <dataAccessRoleArn>\s

                Where:
                    s3Uri - The URI of the Amazon S3 bucket where the documents to translate are located.\s
                    s3UriOut - The URI of the Amazon S3 bucket where the translated documents are saved to. \s
                    jobName - The job name.\s
                    dataAccessRoleArn - The Amazon Resource Name (ARN) value of the role required for translation jobs.
                """;

        if (args.length != 4) {
            System.out.println(usage);
            System.exit(1);
        }

        String s3Uri = args[0];
        String s3UriOut = args[1];
        String jobName = args[2];
        String dataAccessRoleArn = args[3];

        Region region = Region.US_WEST_2;
        TranslateClient translateClient = TranslateClient.builder()
                .region(region)
                .build();

        String id = translateDocuments(translateClient, s3Uri, s3UriOut, jobName, dataAccessRoleArn);
        System.out.println("Translation job " + id + " is completed");
        translateClient.close();
    }

    public static String translateDocuments(TranslateClient translateClient,
            String s3Uri,
            String s3UriOut,
            String jobName,
            String dataAccessRoleArn) {

        try {
            InputDataConfig dataConfig = InputDataConfig.builder()
                    .s3Uri(s3Uri)
                    .contentType("text/plain")
                    .build();

            OutputDataConfig outputDataConfig = OutputDataConfig.builder()
                    .s3Uri(s3UriOut)
                    .build();

            StartTextTranslationJobRequest textTranslationJobRequest = StartTextTranslationJobRequest.builder()
                    .jobName(jobName)
                    .dataAccessRoleArn(dataAccessRoleArn)
                    .inputDataConfig(dataConfig)
                    .outputDataConfig(outputDataConfig)
                    .sourceLanguageCode("en")
                    .targetLanguageCodes("fr")
                    .build();

            StartTextTranslationJobResponse textTranslationJobResponse = translateClient
                    .startTextTranslationJob(textTranslationJobRequest);

            boolean jobDone = false;
            String jobStatus;
            String jobId = textTranslationJobResponse.jobId();

            DescribeTextTranslationJobRequest jobRequest = DescribeTextTranslationJobRequest.builder()
                    .jobId(jobId)
                    .build();

            while (!jobDone) {

                DescribeTextTranslationJobResponse response = translateClient.describeTextTranslationJob(jobRequest);
                jobStatus = response.textTranslationJobProperties().jobStatusAsString();
                System.out.println(jobStatus);

                if (jobStatus.contains("COMPLETED"))
                    jobDone = true;
                else {
                    System.out.print(".");
                    Thread.sleep(sleepTime * 1000);
                }
            }
            return textTranslationJobResponse.jobId();

        } catch (TranslateException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
}