package com.example.translate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;
import software.amazon.awssdk.services.translate.model.TranslateException;

public class TranslationApp extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton translateButton;

    private TranslateClient translateClient;

    public TranslationApp() {
        super("Text Translation");

        // Configuring AWS translation client
        Region region = Region.US_WEST_2;
        translateClient = TranslateClient.builder()
                .region(region)
                .build();

        // Configuring GUI
        inputField = new JTextField();
        outputArea = new JTextArea();
        translateButton = new JButton("Translate");

        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateText(inputField.getText());
            }
        });

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(inputField);
        panel.add(translateButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setVisible(true);
    }

    private void translateText(String text) {
        try {
          

            TranslateTextRequest translateTextRequest = TranslateTextRequest.builder()
                    .sourceLanguageCode("en")
                    .targetLanguageCode("pt")
                    .text(text)
                    .build();

            TranslateTextResponse response = translateClient.translateText(translateTextRequest);
            outputArea.setText(response.translatedText());

        } catch (TranslateException e) {
            outputArea.setText("Error translating text: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TranslationApp();
            }
        });
    }
}
