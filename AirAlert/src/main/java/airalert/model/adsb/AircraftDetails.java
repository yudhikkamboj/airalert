package airalert.model.adsb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AircraftDetails {

    private String type;

    @JsonProperty("icao_type")
    private String icaoType;

    private String manufacturer;

    @JsonProperty("mode_s")
    private String modeS;

    private String registration;

    @JsonProperty("registered_owner_country_iso_name")
    private String registeredOwnerCountryIsoName;

    @JsonProperty("registered_owner_country_name")
    private String registeredOwnerCountryName;

    @JsonProperty("registered_owner_operator_flag_code")
    private String registeredOwnerOperatorFlagCode;

    @JsonProperty("registered_owner")
    private String registeredOwner;

    @JsonProperty("url_photo")
    private String urlPhoto;

    @JsonProperty("url_photo_thumbnail")
    private String urlPhotoThumbnail;
}
