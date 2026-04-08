package com.tom.utils;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class AliasGenerator {
    public static String generate(String link) {
        Client client = Client.builder().apiKey("AIzaSyAT117FkNFjQVd9MKMQ5Hcr4Q26m9KCB8U").build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3-flash-preview",
                        "No other explanation and no space between words, apply '-' to link words, just list 3 short aliases based on the following link: " + link,
                        null);

        return response.text();
    }
}
