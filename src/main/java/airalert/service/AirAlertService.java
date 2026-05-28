package airalert.service;

import airalert.mapper.StateMapper;
import airalert.model.AllStateResponse;
import airalert.model.State;
import airalert.model.adsb.AircraftDetails;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Slf4j
public class AirAlertService {

    private OpenSkyApiClient openSkyApiClient = new OpenSkyApiClient();
    private AdsbClient adsbClient = new AdsbClient();
    private StateMapper stateMapper = new StateMapper();

    public Boolean executeService(Map<Object, Object> input){
        if(input != null && !input.isEmpty()){
            log.info("Input is not empty");
        }

        List<State> allState = fetchAllStateResponse();
        List<AircraftDetails> aircraftDetails = adsbClient.fetchAircraftDetails(allState);

        if(aircraftDetails.isEmpty()){
            log.info("No aircraft found!!!");
        }else{
            log.info("Aircraft found nearby!!!");
        }

        for(AircraftDetails aircraft : aircraftDetails){
            log.info("{}, {}, {}, {}", aircraft.getType(), aircraft.getType(), aircraft.getRegisteredOwner(), aircraft.getRegisteredOwnerCountryName());
        }

        return true;
    }

    public List<State> fetchAllStateResponse(){

        try {
            String token = openSkyApiClient.fetchBearerToken();

            String openSkyResponseString = openSkyApiClient.sendStateRequest(
                    "30.5",
                    "29.0",
                    "77.5",
                    "76.0",
                    token);

            ObjectMapper objectMapper = new ObjectMapper();
            AllStateResponse allStateResponse = objectMapper
                    .readValue(openSkyResponseString, AllStateResponse.class);

            List<State> stateResponse = stateMapper.mapObjectListToStateList(allStateResponse.getStates());

            return stateResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
