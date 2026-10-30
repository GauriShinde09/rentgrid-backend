
package com.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.app.dto.DamageAssessmentDto;
import com.app.dto.VehicleRequirementDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GroqAiService {

    private final RestClient restClient;
    private final String model;
    private final ObjectMapper objectMapper;

    public GroqAiService(
            @Value("${groq.api.key}") String apiKey,
            @Value("${groq.api.url}") String apiUrl,
            @Value("${groq.api.model}") String model,
            ObjectMapper objectMapper) {

        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(
                        "Authorization", "Bearer " + apiKey)
                .build();

        this.model = model;
        this.objectMapper = objectMapper;
    }

    // Existing AI test method
    public String askAI(String message) {

        String systemPrompt =
                "You are a helpful assistant for RentGrid, "
                + "a vehicle rental system.";

        return callGroq(message, systemPrompt);
    }


public VehicleRequirementDto extractRequirements(String message) {

    String systemPrompt = """
        You are a vehicle rental requirement extractor for RentGrid.
        Extract these fields:
        vehicleType, seatingCapacity, maxPricePerDay, pickupDate, returnDate.

        Return ONLY one valid JSON object.
        Do not include Markdown or code fences.
        Use null for fields not mentioned.
        seatingCapacity must be an integer or null.
        maxPricePerDay must be a number or null.
        Dates must use YYYY-MM-DD format or null.

        Example:
        {
          "vehicleType": "SUV",
          "seatingCapacity": 5,
          "maxPricePerDay": 3000,
          "pickupDate": "2026-11-01",
          "returnDate": "2026-11-03"
        }
        """;

    String aiResponse = callGroq(message, systemPrompt);

    try {
        System.out.println("Groq AI response: " + aiResponse);

        // Remove Markdown code fences if present
        aiResponse = aiResponse.trim();

        if (aiResponse.startsWith("```")) {
            aiResponse = aiResponse.replaceAll("^```(?:json)?\\s*", "");
            aiResponse = aiResponse.replaceAll("\\s*```$", "");
        }

        return objectMapper.readValue(
                aiResponse,
                VehicleRequirementDto.class
        );

    } catch (Exception e) {
        e.printStackTrace();

        throw new IllegalArgumentException(
                "Could not understand vehicle requirements. Please try again."
        );
    }
}

    // Common method to call Groq API
    private String callGroq(
            String message,
            String systemPrompt) {

        Map<String, Object> request = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", systemPrompt
                        ),
                        Map.of(
                                "role", "user",
                                "content", message
                        )
                ),
                "temperature", 0
        );

        Map response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);

        if (response == null
                || response.get("choices") == null) {
            throw new IllegalStateException(
                    "No response received from Groq AI");
        }

        List choices = (List) response.get("choices");

        if (choices.isEmpty()) {
            throw new IllegalStateException(
                    "Groq AI returned no response");
        }

        Map firstChoice = (Map) choices.get(0);
        Map responseMessage = (Map) firstChoice.get("message");

        Object content = responseMessage.get("content");

        if (content == null) {
            throw new IllegalStateException(
                    "Groq AI returned empty content");
        }

        return content.toString();
    }

public DamageAssessmentDto assessDamage(String description) {

    if (description == null || description.isBlank()) {
        throw new IllegalArgumentException(
                "Damage description cannot be empty");
    }

    String systemPrompt = """
        You are a vehicle damage assessment assistant for RentGrid.

        Analyze the damage description and return only a valid JSON object
        with these fields:
        description, damageCategory, severity,
        estimatedRepairCost, recommendation.

        damageCategory should be a short category such as
        Body Damage, Glass Damage, Tyre Damage or Mechanical Damage.

        severity must be Minor, Moderate or Severe.

        estimatedRepairCost must be a numeric amount in INR.
        Treat this as a preliminary estimate, not a confirmed repair cost.
        If the description does not provide enough information,
        use 0 for estimatedRepairCost and explain the uncertainty
        in recommendation.

        Do not use Markdown or code fences.
        """;

    String aiResponse = callGroq(description, systemPrompt);

    try {
        aiResponse = aiResponse.trim();

        if (aiResponse.startsWith("```")) {
            aiResponse = aiResponse.replaceAll(
                    "^```(?:json)?\\s*", "");
            aiResponse = aiResponse.replaceAll("\\s*```$", "");
        }

        return objectMapper.readValue(
                aiResponse, DamageAssessmentDto.class);

    } catch (Exception e) {
        throw new IllegalArgumentException(
                "Could not assess vehicle damage. Please try again.");
    }
}

}
