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

public class TranslateTextGUI extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private final TranslateClient translateClient;
    private final JTextField inputText;
    private final JLabel originalLabel;
    private final JLabel translatedLabel;
    private final JComboBox<String> sourceLanguageComboBox;
    private final JComboBox<String> targetLanguageComboBox;

    private final String[] languages = {"en", "es", "fr", "pt", "de", "ar"}; 

    public TranslateTextGUI() {
        super("Text Translator");

        Region region = Region.US_WEST_2;
        translateClient = TranslateClient.builder()
                .region(region)
                .build();

        inputText = new JTextField(20);

        JButton translateButton = new JButton("Translate");
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateText();
            }
        });

        // Labels to display original and translated text
        originalLabel = new JLabel("Original text:");
        translatedLabel = new JLabel("Translated text:");

        // Set font for labels
        Font timesFont = new Font("Times New Roman", Font.PLAIN, 14);
        originalLabel.setFont(timesFont);
        translatedLabel.setFont(timesFont);

        sourceLanguageComboBox = new JComboBox<>(languages);
        sourceLanguageComboBox.setSelectedItem("en"); 
        targetLanguageComboBox = new JComboBox<>(languages);
        targetLanguageComboBox.setSelectedItem("pt"); 

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.add(originalLabel);
        panel.add(translatedLabel);
        panel.add(sourceLanguageComboBox);
        panel.add(targetLanguageComboBox);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputText);
        inputPanel.add(translateButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void translateText() {
        String textToTranslate = inputText.getText();
        String sourceLanguage = (String) sourceLanguageComboBox.getSelectedItem();
        String targetLanguage = (String) targetLanguageComboBox.getSelectedItem();

        try {
            TranslateTextRequest translateTextRequest = TranslateTextRequest.builder()
                    .sourceLanguageCode(sourceLanguage)
                    .targetLanguageCode(targetLanguage)
                    .text(textToTranslate)
                    .build();

            TranslateTextResponse response = translateClient.translateText(translateTextRequest);
            translatedLabel.setText("Translated text: " + response.translatedText());
        } catch (TranslateException e) {
            JOptionPane.showMessageDialog(this, "Error occurred during translation: " + e.getMessage(),
                    "Translation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TranslateTextGUI();
            }
        });
    }
}
