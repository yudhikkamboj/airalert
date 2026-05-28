package airalert.mapper;

import airalert.model.State;

import java.util.ArrayList;
import java.util.List;

public class StateMapper {

    public List<State> mapObjectListToStateList(List<List<Object>> objectList) {

        List<State> states = new ArrayList<>();

        if (objectList == null || objectList.isEmpty()) {
            return states;
        }

        for (List<Object> obj : objectList) {

            State state = new State();

            state.setIcao24((String) obj.get(0));
            state.setCallsign((String) obj.get(1));
            state.setOrigin_country((String) obj.get(2));

            state.setTime_position(
                    obj.get(3) != null ? ((Number) obj.get(3)).intValue() : null
            );

            state.setLast_contact(
                    obj.get(4) != null ? ((Number) obj.get(4)).intValue() : null
            );

            state.setLongitude(
                    obj.get(5) != null ? ((Number) obj.get(5)).doubleValue() : null
            );

            state.setLatitude(
                    obj.get(6) != null ? ((Number) obj.get(6)).doubleValue() : null
            );

            state.setBaroAltitude(
                    obj.get(7) != null ? ((Number) obj.get(7)).doubleValue() : null
            );

            state.setOnGround(
                    obj.get(8) != null ? (Boolean) obj.get(8) : null
            );

            state.setVelocity(
                    obj.get(9) != null ? ((Number) obj.get(9)).doubleValue() : null
            );

            state.setTrueTrack(
                    obj.get(10) != null ? (Double) obj.get(10) : null
            );

//            Datatype inconsistency in response
//            state.setVerticalRate(
//                    obj.get(11) != null ? Double.valueOf((String)obj.get(11))  : null
//            );

            state.setSensors(
                    obj.get(12) != null
                            ? ((List<?>) obj.get(12))
                            .stream()
                            .map(sensor -> ((Number) sensor).intValue())
                            .toList()
                            : null
            );

            state.setGeoAltitude(
                    obj.get(13) != null ? ((Number) obj.get(13)).doubleValue() : null
            );

            state.setSquawk(
                    obj.get(14) != null ? (String) obj.get(14) : null
            );

            state.setSpi(
                    obj.get(15) != null ? (Boolean) obj.get(15) : null
            );

            state.setPositionSource(
                    obj.get(16) != null ? ((Number) obj.get(16)).intValue() : null
            );

            state.setCategory(
                    obj.size() > 17 && obj.get(17) != null
                            ? ((Number) obj.get(17)).intValue()
                            : null
            );

            states.add(state);
        }

        return states;
    }
}
