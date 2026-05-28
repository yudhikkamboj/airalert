package airalert.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class State {
    private String icao24;
    private String callsign;
    private String origin_country;
    private Integer time_position;
    private Integer last_contact;
    private Double longitude;
    private Double latitude;

    @JsonProperty("baro_altitude")
    private Double baroAltitude;

    @JsonProperty("on_ground")
    private Boolean onGround;
    private Double velocity;

    @JsonProperty("true_track")
    private Double trueTrack;

    @JsonProperty("vertical_rate")
    private Double verticalRate;
    private List<Integer> sensors;

    @JsonProperty("geo_altitude")
    private Double geoAltitude;
    private String squawk;
    private Boolean spi;

    @JsonProperty("position_source")
    private Integer positionSource;
    private Integer category;
}
