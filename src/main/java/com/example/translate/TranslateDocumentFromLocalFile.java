package com.example.translate;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.translate.*;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class TranslateDocumentFromLocalFile {

    public static void main(String[] args) {
        // Crie um cliente para o serviço de tradução
        TranslateClient translateClient = TranslateClient.builder().build();

        // Caminho do arquivo de entrada e saída
        String inputFile = "C:\\Users\\LENOVO\\Downloads\\teste.pdf";
        String outputFile = "C:\\Users\\LENOVO\\Downloads\\teste.pdf";

        // Traduza o documento
        TranslateTextResponse response = translateDocument(translateClient, inputFile);

        // Salve o documento traduzido em um arquivo local
        saveTranslatedDocumentToFile(response.translatedText(), outputFile);

        // Feche o cliente
        translateClient.close();
    }

    private static TranslateTextResponse translateDocument(TranslateClient translateClient, String inputFile) {
        // Ler o conteúdo do arquivo de entrada
        String content = readDocumentFromFile(inputFile);

        // Criar uma solicitação de tradução
        TranslateTextRequest request = TranslateTextRequest.builder()
                .sourceLanguageCode("en") // Código de idioma de origem
                .targetLanguageCode("pt") // Código de idioma de destino
                .text(content) // Conteúdo a ser traduzido
                .build();

        // Traduzir o documento
        return translateClient.translateText(request);
    }

    private static String readDocumentFromFile(String inputFile) {
        // Ler o conteúdo do arquivo e retorná-lo como uma String
        try {
            Path path = Paths.get(inputFile);
            byte[] contentBytes = Files.readAllBytes(path);
            return new String(contentBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveTranslatedDocumentToFile(String translatedText, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(translatedText);
            System.out.println("Documento traduzido salvo em: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}