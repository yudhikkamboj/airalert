package airalert.constants;

public class OpenSkyApiConstants {

    public static final String OPENSKY_API_URL = "https://opensky-network.org/api/";
    public static final String OPENSKY_API_ENDPOINT_STATES = OPENSKY_API_URL + "states/all";

    public static final String LAMIN = "lamin";
    public static final String LAMAX = "lamax";
    public static final String LOMIN = "lomin";
    public static final String LOMAX = "lomax";

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CLIENT_ID_SSM_PATH = "/airalert/client_id";

    public static final String CLIENT_SECRET_SSM_PATH = "/airalert/client_secret";
}
