package com.example.translate;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.ListTextTranslationJobsRequest;
import software.amazon.awssdk.services.translate.model.ListTextTranslationJobsResponse;
import software.amazon.awssdk.services.translate.model.TextTranslationJobProperties;
import software.amazon.awssdk.services.translate.model.TranslateException;


import java.util.List;

public class ListTextTranslationJobs {
    public static void main(String[] args) {
        Region region = Region.US_WEST_2;
        TranslateClient translateClient = TranslateClient.builder()
                .region(region)
                .build();

        getTranslationJobs(translateClient);
        translateClient.close();
    }

    public static void getTranslationJobs(TranslateClient translateClient) {
        try {
            ListTextTranslationJobsRequest textTranslationJobsRequest = ListTextTranslationJobsRequest.builder()
                    .maxResults(10)
                    .build();

            ListTextTranslationJobsResponse jobsResponse = translateClient
                    .listTextTranslationJobs(textTranslationJobsRequest);
            List<TextTranslationJobProperties> props = jobsResponse.textTranslationJobPropertiesList();
            for (TextTranslationJobProperties prop : props) {
                System.out.println("The job name is: " + prop.jobName());
                System.out.println("The job id is: " + prop.jobId());
            }

        } catch (TranslateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}