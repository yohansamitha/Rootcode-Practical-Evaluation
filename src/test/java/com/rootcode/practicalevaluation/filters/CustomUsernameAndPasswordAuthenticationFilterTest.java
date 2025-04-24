package com.rootcode.practicalevaluation.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomUsernameAndPasswordAuthenticationFilterTest {

    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/library/login";
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String username = "yohansamitha@gmail.com";
        String password = "password123123"; // correct password here
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", encodedPassword);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), getJsonHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("jwtToken", "userEmail", "userId");
    }

    @Test
    public void testInvalidCredentials() throws Exception {
        String username = "yohansamitha@gmail.com";
        String password = "wrongpassword"; // wrong password
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", encodedPassword);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), getJsonHeaders());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), entity, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).contains("code", "message", "Invalid username or password.");
            fail("Expected HttpClientErrorException.UNAUTHORIZED but none was thrown");
        } catch (HttpClientErrorException e) {
            // Log the response body for debugging
            System.out.println("Response Body: " + e.getResponseBodyAsString());
        }
    }

}
