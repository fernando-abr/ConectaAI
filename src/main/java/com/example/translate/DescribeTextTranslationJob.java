package com.example.translate;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.DescribeTextTranslationJobRequest;
import software.amazon.awssdk.services.translate.model.DescribeTextTranslationJobResponse;
import software.amazon.awssdk.services.translate.model.TranslateException;

public class DescribeTextTranslationJob {
    public static void main(String[] args) {
        final String usage = """

                Usage:
                    <id>\s

                Where:
                    id - A translation job ID value. You can obtain this value from the BatchTranslation example.
                """;

        if (args.length != 1) {
            System.out.println(usage);
            System.exit(1);
        }

        String id = args[0];
        Region region = Region.US_WEST_2;
        TranslateClient translateClient = TranslateClient.builder()
                .region(region)
                .build();

        describeTextTranslationJob(translateClient, id);
        translateClient.close();
    }

    public static void describeTextTranslationJob(TranslateClient translateClient, String id) {
        try {
            DescribeTextTranslationJobRequest textTranslationJobRequest = DescribeTextTranslationJobRequest.builder()
                    .jobId(id)
                    .build();

            DescribeTextTranslationJobResponse jobResponse = translateClient
                    .describeTextTranslationJob(textTranslationJobRequest);
            System.out.println("The job status is " + jobResponse.textTranslationJobProperties().jobStatus());
            System.out.println(
                    "The source language is " + jobResponse.textTranslationJobProperties().sourceLanguageCode());
            System.out.println(
                    "The target language is " + jobResponse.textTranslationJobProperties().targetLanguageCodes());

        } catch (TranslateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}