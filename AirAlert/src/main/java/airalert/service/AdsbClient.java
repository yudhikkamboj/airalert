package airalert.service;

import airalert.model.State;
import airalert.model.adsb.AdsbApiResponse;
import airalert.model.adsb.AircraftDetails;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AdsbClient {


    public AircraftDetails getAircraftFromAdsb(String icao) throws IOException, InterruptedException {
        String url = "https://api.adsbdb.com/v0/aircraft/" + icao;
        AircraftDetails aircraftDetails = null;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            AdsbApiResponse responseBody = mapper.readValue(response.body(), AdsbApiResponse.class);
            if (responseBody.getResponse() != null) {
                aircraftDetails = responseBody.getResponse().getAircraft();
            }
        }else if(response.statusCode() == 404){
            log.info("No aircraft found for ICAO: " + icao);
            aircraftDetails = new AircraftDetails();
            aircraftDetails.setType("Unknown aircraft");
        }

        return aircraftDetails;
    }

    public List<AircraftDetails> fetchAircraftDetails(List<State> flightStates) {

        List<AircraftDetails> aircraftList = new ArrayList<>();

        flightStates.stream().filter(s -> s != null && !s.getOnGround()).forEach(state -> {
            try {
                AircraftDetails aircraftDetail = getAircraftFromAdsb(state.getIcao24());

                if (aircraftDetail == null) {
                    aircraftDetail = new AircraftDetails();
                    aircraftDetail.setType("Unknown Aircraft");
                }
                aircraftList.add(aircraftDetail);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return aircraftList;
    }
}
