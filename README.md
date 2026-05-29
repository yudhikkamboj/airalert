# AirAlert

A serverless application that detects aircraft flying in a defined airspace and logs their details. It runs on a scheduled basis using AWS EventBridge Scheduler and is built with Java 21 on AWS Lambda.

---

## How It Works

1. EventBridge Scheduler triggers the Lambda every 5 minutes
2. Lambda authenticates with the OpenSky Network API using OAuth2 client credentials
3. Fetches all live aircraft states within a bounding box (currently over northern India: lat 29.0–30.5, lon 76.0–77.5)
4. For each airborne aircraft, queries the ADSB DB API using the ICAO24 code to get aircraft details
5. Logs aircraft type, registration, owner, and country

---

## Architecture

```
EventBridge Scheduler
        │
        ▼
  AWS Lambda (Java 21)
        │
        ├──▶ OpenSky Network API  (fetch live flight states)
        │         └── OAuth2 token via Keycloak
        │
        └──▶ ADSB DB API  (fetch aircraft details by ICAO24)
```

---

## Project Structure

```
AirAlert/
├── src/
│   └── main/java/airalert/
│       ├── handler/        # Lambda entry point
│       ├── service/        # OpenSkyApiClient, AdsbClient, AirAlertService
│       ├── mapper/         # Maps raw OpenSky state arrays to State objects
│       ├── model/          # POJOs: State, AllStateResponse, AuthResponse
│       │   └── adsb/       # AircraftDetails, AdsbResponse, AdsbApiResponse
│       └── constants/      # OpenSkyApiConstants
├── pom.xml
├── template.yaml           # SAM template (Lambda + Scheduler + IAM)
├── samconfig.toml          # SAM deploy config
└── SAM_DEPLOY.md           # Build and deploy instructions
```

---

## Key Components

### LambdaFunctionHandler
Entry point implementing `RequestHandler`. Delegates to `AirAlertService` and returns `"success"` or `"failed"`.

### AirAlertService
Orchestrates the flow:
- Calls `OpenSkyApiClient` to fetch a bearer token and then live flight states
- Passes airborne states to `AdsbClient` to enrich with aircraft details
- Logs results

### OpenSkyApiClient
- `fetchBearerToken()` — authenticates via OpenSky Keycloak using OAuth2 client credentials flow
- `sendStateRequest()` — queries the OpenSky `/states/all` endpoint with a lat/lon bounding box
- Credentials (`client_id`, `client_secret`) are fetched from AWS SSM Parameter Store at cold start

### AdsbClient
- `getAircraftFromAdsb(icao)` — calls `https://api.adsbdb.com/v0/aircraft/{icao}` to get aircraft metadata
- `fetchAircraftDetails(states)` — filters out grounded aircraft and enriches each with ADSB data

### StateMapper
Maps the raw OpenSky response (a list of arrays) to typed `State` objects.

---

## External APIs

| API | Purpose | Auth |
|-----|---------|------|
| [OpenSky Network](https://opensky-network.org/apidoc/) | Live flight states | OAuth2 client credentials |
| [ADSB DB](https://www.adsbdb.com/) | Aircraft details by ICAO24 | None (public) |

---

## AWS Resources

| Resource | Type | Purpose |
|----------|------|---------|
| `AirAlertFunction` | AWS Lambda (Java 21) | Core application logic |
| `AirAlertScheduler` | EventBridge Scheduler | Triggers Lambda every 5 minutes |
| `AirAlertSchedulerRole` | IAM Role | Grants scheduler permission to invoke Lambda |

---

## Configuration

Credentials are stored as `SecureString` in AWS SSM Parameter Store:

| SSM Parameter | Description |
|---------------|-------------|
| `/airalert/client_id` | OpenSky Network OAuth2 client ID |
| `/airalert/client_secret` | OpenSky Network OAuth2 client secret |

To create them:
```bash
aws ssm put-parameter --name "/airalert/client_id" --value "<your-client-id>" --type SecureString
aws ssm put-parameter --name "/airalert/client_secret" --value "<your-client-secret>" --type SecureString
```

---

## Tech Stack

- Java 21
- AWS Lambda
- AWS SAM
- AWS EventBridge Scheduler
- AWS SSM Parameter Store
- Maven + maven-shade-plugin (fat JAR)
- Lombok
- Jackson (tools.jackson 3.x)
- OpenSky Network API
- ADSB DB API

---

For build and deploy instructions see [SAM_DEPLOY.md](SAM_DEPLOY.md).
