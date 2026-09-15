package org.occideas.anzscocoder.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.AbsCoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

/**
 * Client for the ABS Whole of Australian Government Occupation Coding Service
 * (public-coder.api.abs.gov.au) used to resolve an ANZSCO code from a free-text
 * job title and job description.
 */
@Component
public class AbsCoderClient {

    private static final Logger log = LogManager.getLogger(AbsCoderClient.class);

    // Refresh the token a little before its stated 60-minute expiry.
    private static final long TOKEN_REFRESH_MARGIN_SECONDS = 60;

    @Autowired
    private AbsCoderConfig config;

    @Autowired
    private RestTemplate restTemplate;

    private String cachedToken;
    private Instant tokenExpiry = Instant.MIN;

    public AbsCoderCodeResponse code(String occpText, String tasksText, int numberOfSuggestions) {
        String token = getAccessToken();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(config.getBaseUrl())
            .path("/topics/" + config.getTopic() + "/code");

        // ABS's API Gateway resource policy rejects the standard "Bearer <token>" scheme
        // prefix with an explicit deny — it expects the raw access token as the header value.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, token);

        AbsCoderCodeRequest body = new AbsCoderCodeRequest(occpText, tasksText, numberOfSuggestions);

        try {
            return restTemplate.exchange(builder.build().toUri(), HttpMethod.POST,
                new HttpEntity<>(body, headers), AbsCoderCodeResponse.class).getBody();
        } catch (HttpStatusCodeException e) {
            log.error("ABS Coder /code call failed: status={}, body={}", e.getStatusCode(),
                e.getResponseBodyAsString());
            throw new IllegalStateException("ABS Coder /code call failed (" + e.getStatusCode() + "): "
                + e.getResponseBodyAsString(), e);
        }
    }

    private synchronized String getAccessToken() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }

        String credentials = config.getClientId() + ":" + config.getClientSecret();
        String encodedCredentials = Base64.getEncoder()
            .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        log.debug("Requesting a new ABS Coder access token");
        AbsCoderTokenResponse tokenResponse;
        try {
            tokenResponse = restTemplate.exchange(config.getAuthUrl(), HttpMethod.POST,
                new HttpEntity<>(form, headers), AbsCoderTokenResponse.class).getBody();
        } catch (HttpStatusCodeException e) {
            log.error("ABS Coder token request failed: status={}, body={}", e.getStatusCode(),
                e.getResponseBodyAsString());
            throw new IllegalStateException("ABS Coder authentication failed (" + e.getStatusCode() + "): "
                + e.getResponseBodyAsString(), e);
        }

        if (tokenResponse == null || tokenResponse.getAccess_token() == null) {
            throw new IllegalStateException("ABS Coder authentication did not return an access token");
        }

        cachedToken = tokenResponse.getAccess_token();
        long expiresIn = tokenResponse.getExpires_in() > 0 ? tokenResponse.getExpires_in() : 3600;
        tokenExpiry = Instant.now().plusSeconds(Math.max(expiresIn - TOKEN_REFRESH_MARGIN_SECONDS, 0));

        return cachedToken;
    }
}
