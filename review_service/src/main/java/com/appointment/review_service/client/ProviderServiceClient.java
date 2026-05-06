package com.appointment.review_service.client;

import com.appointment.review_service.dto.ProviderRatingUpdateRequest;
import com.appointment.review_service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProviderServiceClient {

    private final RestTemplate restTemplate;

    @Value("${provider.service.base-url}")
    private String providerServiceBaseUrl;

    public ProviderServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void updateRating(Long providerId, Double avgRating) {
        String url = providerServiceBaseUrl + "/providers/" + providerId + "/rating";

        try {
            ProviderRatingUpdateRequest request = new ProviderRatingUpdateRequest();
            request.setAvgRating(avgRating);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ProviderRatingUpdateRequest> entity =
                    new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            System.out.println("Provider rating update success. Status: " + response.getStatusCode());
        } catch (HttpStatusCodeException ex) {
            System.out.println("Provider-service error status: " + ex.getStatusCode());
            System.out.println("Provider-service error body: " + ex.getResponseBodyAsString());
            throw new BadRequestException(
                    "Provider-service call failed: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException("Unable to update provider rating in provider-service: " + ex.getMessage());
        }
    }
}