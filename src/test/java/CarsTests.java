import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@JsonAutoDetect
public class CarsTests {

    private static final String BASE_URL = "https://retoolapi.dev/KCuEUu/cars";

    @Test
    public void createCarResponseIsSuccess() {
        String expectedResponse = "\"Brand\": \"Subaru\",\n" + "  \"price\": \"42\"";

        createCarScenario(201, "", expectedResponse);
    }

    @Test
    public void createCarWitDuplicatedIdFails() {
        String expectedResponse = "Insert failed, duplicate id";

        createCarScenario(500, "1", expectedResponse);

    }

    private Map<String, String> createCarParams(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("Brand", "Subaru");
        map.put("price", "42");
        map.put("id", id);
        return map;
    }

    private void createCarScenario(int statusCode, String carId, String responseToCheck) {
        given()
                .contentType(ContentType.JSON)
                .body(createCarParams(carId))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat().statusCode(statusCode)
                .and()
                .body(containsString(responseToCheck));
    }

}
