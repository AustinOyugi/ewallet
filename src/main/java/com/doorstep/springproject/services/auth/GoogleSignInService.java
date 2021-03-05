package com.doorstep.springproject.services.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Service
public class GoogleSignInService{

    private final RestTemplate restTemplate;

    @Value("${app.google.clientID}")
    private String CLIENT_ID;

    @Autowired
    public GoogleSignInService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {

        GoogleIdTokenVerifier googleIdTokenVerifier =
                new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);

        if (idToken == null)
            throw new Exception();

        return idToken.getPayload();
    }

    public Map<?,?> verifyTokenUsingRest(String token){

        return restTemplate.getForObject(
                "https://oauth2.googleapis.com/tokeninfo?id_token=".concat(token),
                Map.class
        );

    }
}
