package com.example.translate;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;
import software.amazon.awssdk.services.translate.model.TranslateException;


public class TranslateText {
    public static void main(String[] args) {
        Region region = Region.US_WEST_2;
        TranslateClient translateClient = TranslateClient.builder()
                .region(region)
                .build();

        textTranslate(translateClient);
        translateClient.close();
    }

    public static void textTranslate(TranslateClient translateClient) {
        try {
            TranslateTextRequest translateTextRequest = TranslateTextRequest.builder()
                    .sourceLanguageCode("en")
                    .targetLanguageCode("pt")
                    .text("Digite seu texto aqui.")
                    .build();

            TranslateTextResponse resposta = translateClient.translateText(translateTextRequest);
            System.out.println(resposta.translatedText());

        } catch (TranslateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}