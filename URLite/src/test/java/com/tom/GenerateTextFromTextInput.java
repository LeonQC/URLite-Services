package com.tom;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GenerateTextFromTextInput {
    public static void main(String[] args) {
        Client client = Client.builder().apiKey("AIzaSyAT117FkNFjQVd9MKMQ5Hcr4Q26m9KCB8U").build();
        String link = "https://www.cnn.com/2026/04/03/politics/hegseth-trump-us-fighter-jet-iran";

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3-flash-preview",
                        "No other explanation and no space between words, apply '-' to link words, just list 3 short aliases based on the following link: " + link,
                        null);

        System.out.println(response.text());
    }
}
