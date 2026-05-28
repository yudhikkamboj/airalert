package airalert.service;

import airalert.constants.OpenSkyApiConstants;
import airalert.model.AuthResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenSkyApiClient {

    public String fetchOpenSkyResponse(){
        return "response";
    }

    public String fethOpenSkyResponse(String lamin, String lamax, String lomin, String lomax){
        return "response lamex";
    }

    public String fetchBearerToken() throws IOException, InterruptedException {

        String clientId = System.getenv(OpenSkyApiConstants.CLIENT_ID);
        String clientSecret = System.getenv(OpenSkyApiConstants.CLIENT_SECRET);

        System.out.println("clientId:clientSecret :: " + clientId + ":" + clientSecret);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header(OpenSkyApiConstants.CONTENT_TYPE, OpenSkyApiConstants.FORM_URL_ENCODED)
                .uri(URI.create(OpenSkyApiConstants.AUTH_API_URL))
                .POST(HttpRequest.BodyPublishers.ofString(
                        OpenSkyApiConstants.GRANT_TYPE + "=client_credentials&" +
                        OpenSkyApiConstants.CLIENT_ID + "=" + clientId +"&" +
                        OpenSkyApiConstants.CLIENT_SECRET + "=" + clientSecret))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return extractToken(response.body());
    }

    public String extractToken(String response){

        ObjectMapper objectMapper = new ObjectMapper();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);

        if(authResponse.getError() != null){
            throw new RuntimeException("Error in fetching bearer token. Error: " + authResponse.getError()
            + " Description: " + authResponse.getErrorDescription());
        }

        return authResponse.getAccessToken();
    }

    public String sendStateRequest(String lamax, String lamin, String lomax, String lomin, String token) throws IOException, InterruptedException {

        String url = generateOpenSkyStateUrl(lamin, lamax, lomin, lomax);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(OpenSkyApiConstants.AUTHORIZATION, OpenSkyApiConstants.BEARER + token)
                .setHeader("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            return response.body();
        }else{
            return "Error occurred with status code: " + response.statusCode();
        }
    }

    public String generateOpenSkyStateUrl(String lamin, String lamax, String lomin, String lomax){
        return OpenSkyApiConstants.OPENSKY_API_ENDPOINT_STATES
                + "?lamin=" + lamin + "&lamax=" + lamax
                + "&lomin=" + lomin + "&lomax=" + lomax;
    }
}
