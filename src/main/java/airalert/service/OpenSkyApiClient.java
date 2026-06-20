package airalert.service;

import airalert.constants.OpenSkyApiConstants;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class OpenSkyApiClient {

    private static final SsmClient ssmClient = SsmClient.create();
    private static final String clientId = fetchSsmParameter(OpenSkyApiConstants.CLIENT_ID_SSM_PATH);
    private static final String clientSecret = fetchSsmParameter(OpenSkyApiConstants.CLIENT_SECRET_SSM_PATH);

    private static String fetchSsmParameter(String name) {
        return ssmClient.getParameter(GetParameterRequest.builder()
                .name(name)
                .withDecryption(true)
                .build())
                .parameter().value();
    }

    private static String buildBasicAuthHeader() {
        String credentials = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public String sendStateRequest(String lamax, String lamin, String lomax, String lomin) throws IOException, InterruptedException {

        String url = generateOpenSkyStateUrl(lamin, lamax, lomin, lomax);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(OpenSkyApiConstants.AUTHORIZATION, buildBasicAuthHeader())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("OpenSky API error. Status: " + response.statusCode());
        }
    }

    public String generateOpenSkyStateUrl(String lamin, String lamax, String lomin, String lomax) {
        return OpenSkyApiConstants.OPENSKY_API_ENDPOINT_STATES
                + "?lamin=" + lamin + "&lamax=" + lamax
                + "&lomin=" + lomin + "&lomax=" + lomax;
    }
}
